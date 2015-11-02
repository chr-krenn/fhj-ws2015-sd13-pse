package at.fhj.swd13.pse.domain.chat;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.chat.ChatService#getCommunity(int)
	 */
	@Override
	public Community getCommunity(final int communityId) {

		return dbContext.getCommunityDAO().get(communityId);
	}

	@Override
	public Community getCommunity(String communityName) {
		return dbContext.getCommunityDAO().getByName(communityName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.service.ChatService#createChatCommunity(java.lang.
	 * String, java.lang.String, boolean, at.fhj.swd13.pse.db.DbContext)
	 */
	@Override
	public Community createChatCommunity(final String creatorUsername, final String communityName, final boolean invitationOnly)
			throws EntityNotFoundException {

		Person creator = dbContext.getPersonDAO().getByUsername(creatorUsername, true);

		if (creator.isActive()) {

			Community community = new Community(communityName);

			community.setCreatedBy(creator);
			community.setInvitationOnly(invitationOnly);

			return createCommunity(creator, community);

		} else {
			throw new IllegalStateException("User is not active and can therefore not create communities: " + creatorUsername);
		}
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

		return dbContext.getCommunityDAO().getUnconfirmedCommunites();
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
	 * @throws DuplicateEntityException
	 *             if the community already exists
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
				throw new DuplicateEntityException("Community already exists: " + community.getName());

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

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Boolean isPersonMemberOfCommunity(final Person person, final Community community) {

		Boolean isMemberOfCommunity = false;
		try {
			Person p = dbContext.getPersonDAO().getById(person.getPersonId());
			Community c = dbContext.getCommunityDAO().get(community.getCommunityId());

			isMemberOfCommunity = c.isMember(p);

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}

		return isMemberOfCommunity;
	}

	public List<CommunityMember> getCommunityMembersList(final Community community) {

		List<CommunityMember> memberList = dbContext.getCommunityDAO().getCommunityMembers(community);

		return memberList;
	}

	public CommunityMember getCommunityMember(final Community community, final Person person) {

		return dbContext.getCommunityDAO().getCommunityMemberByCommunityAndPerson(community, person);

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

		if (adminPerson.isActive() && adminPerson.isAdmin()) {
			try {

				adminPerson.addConfirmedCommunities(unconfirmed);

				Community c = dbContext.getCommunityDAO().get(unconfirmed.getCommunityId());

				c.setConfirmedBy(adminPerson);

				dbContext.persist(c);

			} catch (ConstraintViolationException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException("Person confirming the community is either not active or not an admin: " + adminPerson.getUserName());
		}
	}
	
	@Override
	public void declineCommunity(final Person adminPerson, Community unconfirmed) {

		if (adminPerson.isActive() && adminPerson.isAdmin()) {
				Community c = dbContext.getCommunityDAO().get(unconfirmed.getCommunityId());

				dbContext.remove(c);
		} else {
			throw new IllegalStateException("Person declining the community is either not active or not an admin: " + adminPerson.getUserName());
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

		return dbContext.getCommunityDAO().getMatchingCommunities(username, needle);
	}

	/**
	 * Creates the user private communities (every user has a community onto
	 * which his private messages are pushed)
	 * 
	 * @return the number of created communities
	 */
	public int createAllPrivateCommunities() {

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
				throw new DuplicateEntityException("Community already exists: " + community.getName());
			}

			return community;
		} else {
			throw new DuplicateEntityException("Community already exists: " + community.getName());
		}
	}

	@Override
	public List<Community> getAllAccessibleCommunities() {

		return dbContext.getCommunityDAO().getAllAccessibleCommunities();

	}

	@Override
	public List<Community> getAllAccessibleCommunities(String searchfieldText) {

		return dbContext.getCommunityDAO().getAllAccessibleCommunities(searchfieldText);
	}

	public String resolveReceipientsMail(final Message message) {

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
		return dbContext.getCommunityDAO().getPrivateCommunity(person);
	}

	@Override
	public boolean addComment(final String username, final int commentedMessageId, final String headline, final String comment) {

		final Person author = dbContext.getPersonDAO().getByUsername(username);

		if (author != null) {

			try {
				final Message commentedMessage = dbContext.getMessageDAO().getById(commentedMessageId);

				Message message = new Message();

				message.setCreatedAt(new Date());
				message.setHeadline(headline);
				message.setMessage(comment);
				message.setPerson(author);
				message.setValidFrom(new Date());

				message.setDeliverySystem(dbContext.getDeliverySystemDAO().getPseService());

				commentedMessage.addMessage(message);

				return true;

			} catch (EntityNotFoundException e) {
				logger.error("[CHAT] message that is commented upon is not found");
			}

		} else {
			logger.error("[CHAT] commenting person not foud: " + username);
		}

		return false;
	}

}
