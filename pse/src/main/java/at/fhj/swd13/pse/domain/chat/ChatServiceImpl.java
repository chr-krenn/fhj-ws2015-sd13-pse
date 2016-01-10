package at.fhj.swd13.pse.domain.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.service.DuplicateEntityException;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * Chat service that provides all functionality for chatting and administration
 * of chats
 *
 */
@Stateless
public class ChatServiceImpl extends ServiceBase implements ChatService {

	@Inject
	private Logger logger;

	/**
	 * Create an instance of the chat service
	 */
	public ChatServiceImpl() {}

	public ChatServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	/**
	 * Returns community found by communityId
	 */
	@Override
	public Community getCommunity(final int communityId) {
		try {
			return dbContext.getCommunityDAO().get(communityId);
		} catch (Throwable ex) {
			logger.info("[ChatService] getCommunity failed for communityId " + communityId + " : " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/**
	 * Returns community found by communityName
	 */
	@Override
	public Community getCommunity(final String communityName) {
		try {
			return dbContext.getCommunityDAO().getByName(communityName);
		} catch (Throwable ex) {
			logger.info("[ChatService] getCommunity failed for communityName " + communityName + " : " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.service.ChatService#createChatCommunity(java.lang.
	 * String, java.lang.String, boolean, at.fhj.swd13.pse.db.DbContext)
	 */
	@Override
	public Community createChatCommunity(final String creatorUsername, final String communityName, final boolean invitationOnly) {
		try {
			Person creator = dbContext.getPersonDAO().getByUsername(creatorUsername, true);

			if (creator.getIsActive()) {
				Community community = new Community(communityName, creator);
				community.setInvitationOnly(invitationOnly);
				return createCommunity(creator, community);
			}
		} catch (Throwable ex) {
			logger.info("[ChatService] getCommunity failed for communityName " + communityName + " : " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}

		throw new ServiceException("User is not active and can therefore not create communities: " + creatorUsername);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.service.ChatService#getUnconfirmedCommunities(at.fhj.
	 * swd13.pse.db.entity.Person, at.fhj.swd13.pse.db.DbContext)
	 */
	@Override
	public List<Community> getUnconfirmedCommunities() {
		try {
			return dbContext.getCommunityDAO().getUnconfirmedCommunites();
		} catch (Throwable ex) {
			logger.info("[ChatService] getUnconfirmedCommunities failed: " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<Community> getAllCommunities() {
		try {
			return dbContext.getCommunityDAO().getAllCommunities();
		} catch (Throwable ex) {
			logger.info("[ChatService] getAllCommunities failed: " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<CommunityMember> getAllUnconfirmedCommunityMembers() {
		try {
			List<CommunityMember> memberrequests = new LinkedList<CommunityMember>();
			List<Community> communities = getAllCommunities();
			for (int i = 0; i < communities.size(); i++) {
				if (!communities.get(i).isPrivateChannel() && communities.get(i).getInvitationOnly()) {
					List<CommunityMember> mlist = communities.get(i).getCommunityMembers();
					for (int j = 0; j < mlist.size(); j++) {
						if (mlist.get(j).getConfirmer() == null)
							memberrequests.add(mlist.get(j));
					}
				}
			}
			return memberrequests;
		} catch (Throwable ex) {
			logger.info("[ChatService] getAllUnconfirmedCommunityMembers failed: " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
		
	}

	/**
	 * Internal Helper to create a community and when the creator is admin set
	 * it to confirmed
	 * 
	 * also creates an admin membership for the creator
	 * 
	 * @param creator
	 * @param community
	 * @param dbContext
	 * 
	 * @return the created, persisted but not committed community
	 *
	 */
	protected Community createCommunity(final Person creator, final Community community) {

		CommunityDAO communityDao = dbContext.getCommunityDAO();

		if (communityDao.getByName(community.getName()) == null) {

			if (creator.isAdmin()) {
				community.setConfirmedBy(creator);
			} else {
				community.setConfirmedBy(null);
			}

			try {
				communityDao.insert(community);
				CommunityMember memberShip = community.addMember(creator, true);

				if (memberShip != null) {
					dbContext.persist(memberShip);
				}
			} catch (ConstraintViolationException x) {
				throw new DuplicateEntityException("Community already exists: " + community.getName(), x);

			}
			return community;
		} else {
			throw new DuplicateEntityException("Community already exists: " + community.getName());
		}
	}

	public CommunityMember createCommunityMember(final Person person, final Community community) {
		try {
			Person p = dbContext.getPersonDAO().getById(person.getPersonId());
			Community c = dbContext.getCommunityDAO().get(community.getCommunityId());
			CommunityMember member = c.addMember(p, false);
			dbContext.persist(member);
			return member;
		} catch (Throwable ex) {
			logger.info("[ChatService] createCommunityMember failed for Person " + person + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	public Boolean isPersonMemberOfCommunity(final Person person, final Community community) {
		try {
			Person p = dbContext.getPersonDAO().getById(person.getPersonId());
			Community c = dbContext.getCommunityDAO().get(community.getCommunityId());
			return c.isMember(p);
		} catch (Throwable ex) {
			logger.info("[ChatService] isPersonMemberOfCommunity failed for Person " + person + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	public List<CommunityMember> getCommunityMembersList(final Community community) {
		try {
			return dbContext.getCommunityDAO().getCommunityMembers(community);
		} catch (Throwable ex) {
			logger.info("[ChatService] getCommunityMembersList failed for Community " + community + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	public CommunityMember getCommunityMember(final Community community, final Person person) {
		try {
			return dbContext.getCommunityDAO().getCommunityMemberByCommunityAndPerson(community, person);
		} catch (Throwable ex) {
			logger.info("[ChatService] getCommunityMemberByCommunityAndPerson failed for Community " + community + " and Person " + person + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.service.ChatService#confirmCommunity(at.fhj.swd13.pse.db
	 * .entity.Person, at.fhj.swd13.pse.db.entity.Community)
	 */

	@Override
	public void confirmCommunity(final Person adminPerson, Community unconfirmed) {
		if (adminPerson == null || !adminPerson.getIsActive() || !adminPerson.isAdmin()) {
			throw new ServiceException("Person confirming the community is either not active or not an admin: " + adminPerson.getUserName());
		}
		
		try {
			adminPerson.addConfirmedCommunities(unconfirmed);
			Community c = dbContext.getCommunityDAO().get(unconfirmed.getCommunityId());
			c.setConfirmedBy(adminPerson);
			dbContext.persist(c);
		} catch (Throwable ex) {
			logger.info("[ChatService] confirmCommunity failed for Community " + unconfirmed + " and Person " + adminPerson + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}

	}

	@Override
	public void confirmCommunityMember(final Person adminPerson, CommunityMember unconfirmed) {
		if (adminPerson == null || !adminPerson.getIsActive() || !adminPerson.isAdmin()) {
			throw new ServiceException("Person confirming the member request is either not active or not an admin: " + adminPerson.getUserName());
		}
		
		try {
			CommunityMember c = dbContext.getCommunityDAO().getCommunityMemberByCommunityAndPerson(unconfirmed.getCommunity(), unconfirmed.getMember());
			c.setConfirmer(adminPerson);
			dbContext.persist(c);
		} catch (Throwable ex) {
			logger.info("[ChatService] confirmCommunityMember failed for CommunityMember " + unconfirmed + " and Person " + adminPerson + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void declineCommunity(final Person adminPerson, Community unconfirmed) {
		if (adminPerson == null || !adminPerson.getIsActive() || !adminPerson.isAdmin()) {
			throw new ServiceException("Person declining the member request is either not active or not an admin: " + adminPerson.getUserName());
		}

		try {
			Community c = dbContext.getCommunityDAO().get(unconfirmed.getCommunityId());
			dbContext.remove(c);
		} catch (Throwable ex) {
			logger.info("[ChatService] declineCommunity failed for CommunityMember " + unconfirmed + " and Person " + adminPerson + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void declineCommunityMember(final Person adminPerson, CommunityMember unconfirmed) {
		if (adminPerson == null || !adminPerson.getIsActive() || !adminPerson.isAdmin()) {
			throw new ServiceException("Person declining the member request is either not active or not an admin: " + adminPerson.getUserName());
		}

		try {
			CommunityMember c = dbContext.getCommunityDAO().getCommunityMemberByCommunityAndPerson(unconfirmed.getCommunity(), unconfirmed.getMember());
			dbContext.remove(c);
		} catch (Throwable ex) {
			logger.info("[ChatService] declineCommunityMember failed for CommunityMember " + unconfirmed + " and Person " + adminPerson + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.domain.chat.ChatService#getPossibleTargetCommunities(
	 * java.lang.String, java.lang.String)
	 */
	public List<Community> getPossibleTargetCommunities(final String username, final String needle) {
		try {
			return dbContext.getCommunityDAO().getMatchingCommunities(username, needle);
		} catch (Throwable ex) {
			logger.info("[ChatService] declineCommunityMember failed for User " + username + " and Needle " + needle + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/**
	 * Creates the user private communities (every user has a community onto
	 * which his private messages are pushed)
	 * 
	 * @return the number of created communities
	 */
	public int createAllPrivateCommunities() {
		try {
			int createdCommunities = 0;

			final List<Person> allPersons = dbContext.getPersonDAO().getAllPersons();

			for (Person user : allPersons) {
				final String privateCommunityName = Community.PRIVATE_PREFIX + user.getUserName();
				boolean foundPrivateCommunity = false;

				for (Community community : user.getCreatedCommunities()) {

					if (community.getName().equals(privateCommunityName)) {
						foundPrivateCommunity = true;
						break;
					}
				}

				if (!foundPrivateCommunity) {
					++createdCommunities;
					createPrivateCommunity(user);
				}
			}

			return createdCommunities;

		} catch (Throwable ex) {
			logger.info("[ChatService] createAllPrivateCommunities failed : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/**
	 * create a private community for the creating user
	 * 
	 * @param creator
	 * 
	 * @param community
	 * 
	 * @return
	 */
	private Community createPrivateCommunity(final Person creator) {

		CommunityDAO communityDao = dbContext.getCommunityDAO();

		Community community = new Community(Community.PRIVATE_PREFIX + creator.getUserName());

		if (communityDao.getByName(community.getName()) == null) {

			community.setConfirmedBy(creator);
			community.setPrivateUser(creator);
			community.setCreatedBy(creator);
			community.setInvitationOnly(true);

			try {
				communityDao.insert(community);
				CommunityMember memberShip = community.addMember(creator, true);

				if (memberShip != null) {
					dbContext.persist(memberShip);
				}
			} catch (ConstraintViolationException x) {
				throw new DuplicateEntityException("Community already exists: " + community.getName(), x);
			}

			return community;
		} else {
			throw new DuplicateEntityException("Community already exists: " + community.getName());
		}
	}

	@Override
	public List<Community> getAllAccessibleCommunities() {
		try {
			return dbContext.getCommunityDAO().getAllAccessibleCommunities();
		} catch (Throwable ex) {
			logger.info("[ChatService] getAllAccessibleCommunities failed : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<Community> getAllAccessibleCommunities(String searchfieldText) {
		try {
			return dbContext.getCommunityDAO().getAllAccessibleCommunities(searchfieldText);
		} catch (Throwable ex) {
			logger.info("[ChatService] getAllAccessibleCommunities failed : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	public String resolveReceipientsMail(final Message message) {
		try {
			StringBuilder builder = new StringBuilder();

			if (message.getCommunities() != null) {

				for (Community community : message.getCommunities()) {
					if (community.isPrivateChannel()) {
						if (builder.length() > 0) {
							builder.append(",");
						}
						builder.append(community.getPrivateUser().getEmailAddress());
					}
				}
			}
			return builder.toString();
		} catch (Throwable ex) {
			logger.info("[ChatService] resolveReceipientsMail failed : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/**
	 * Get the privat community for a person
	 * 
	 * @param person
	 * 
	 * @return the number of created communities
	 */
	@Override
	public Community getPrivateCommunity(Person person) {
		try {
			return dbContext.getCommunityDAO().getPrivateCommunity(person);
		} catch (Throwable ex) {
			logger.info("[ChatService] getPrivateCommunity failed for Person " + person + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public MessageDTO addComment(final String username, final int commentedMessageId, final String headline, final String comment) {
		try {
			final Person author = dbContext.getPersonDAO().getByUsername(username);

			if (author != null) {
				final Message commentedMessage = dbContext.getMessageDAO().getById(commentedMessageId);

				List<Community> communities = new ArrayList<Community>();
				for (Community c : commentedMessage.getCommunities()) {
					communities.add(dbContext.getCommunityDAO().get(c.getCommunityId()));
				}

				Message message = new Message(new Date(), headline, comment, new Date(), 
						dbContext.getDeliverySystemDAO().getPseService(), author);
				message.setCommunities(communities);

				return new MessageDTO(commentedMessage.addMessage(message));
			} else {
				logger.error("[ChatService] addComment commenting person not foud: " + username);
			}
			return null;
		} catch (Throwable ex) {
			logger.info("[ChatService] addComment failed for User " + username + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public CommunityMember getUnconfirmedCommunityMember(int communityId) {
		try {
			for (CommunityMember c : getAllUnconfirmedCommunityMembers()) {
				if (c.getCommunityMemberId() == communityId)
					return c;
			}
			return null;
		} catch (Throwable ex) {
			logger.info("[ChatService] getUnconfirmedCommunityMember failed for Community Id " + communityId + " : " +  ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}
}
