package at.fhj.swd13.pse.domain.chat;

import java.util.List;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.service.DuplicateEntityException;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * Chat service that provides all functionality for chatting and administration
 * of chats
 *
 */
public class ChatServiceImpl extends ServiceBase implements ChatService {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.service.ChatService#confirmCommunity(at.fhj.swd13.pse.db
	 * .entity.Person, at.fhj.swd13.pse.db.entity.Community)
	 */
	public void confirmCommunity(final Person adminPerson, Community unconfirmed) {

		if (adminPerson.isActive() && adminPerson.isAdmin()) {

			adminPerson.addConfirmedCommunities(unconfirmed);

		} else {
			throw new IllegalStateException("Person confirming the community is either not active or not an admin: " + adminPerson.getUserName());
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

		// TODO: filter those that the user is allowed to post to
		return dbContext.getCommunityDAO().getMatchingCommunities(needle);
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

}
