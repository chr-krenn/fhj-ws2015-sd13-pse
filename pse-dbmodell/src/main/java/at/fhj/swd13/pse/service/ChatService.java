package at.fhj.swd13.pse.service;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * Chat service that provides all functionality for chatting and administration
 * of chats
 *
 */
public class ChatService extends ServiceBase {

	/**
	 * Create an instance of the chat service
	 */
	public ChatService() {
		super();
	}

	/**
	 * Create a public chat community. If the creator is an administrator, the
	 * community is confirmed. Method creates a db-context and calls
	 * createChatCommunity
	 * 
	 * @param creator
	 * @param communityName
	 * @param dbContext
	 * 
	 * @return
	 * 
	 * @throws DuplicateEntityException
	 *             if the community already exists
	 * 
	 */
	public Community createChatCommunity(final String creatorUsername, final String communityName,
			final boolean invitationOnly) throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			Community community = createChatCommunity(creatorUsername, communityName, invitationOnly, dbContext);

			dbContext.commit();

			return community;
		}
	}

	/**
	 * 
	 * 
	 * @param creator
	 * @param communityName
	 * @param dbContext
	 * 
	 * @return
	 * 
	 * @throws DuplicateEntityException
	 *             if the community already exists
	 * @throws EntityNotFoundException
	 *             if the creator is no known user
	 * @throws IllegalStateException
	 * 				if the user is not active
	 */
	public Community createChatCommunity(final String creatorUsername, final String communityName,
			final boolean invitationOnly, final DbContext dbContext) {

		Person creator = dbContext.getPersonDAO().getByUsername(creatorUsername, true);

		if (creator.isActive()) {

			Community community = new Community(communityName);

			community.setCreatedBy(creator);
			community.setInvitationOnly(invitationOnly);

			return createCommunity(creator, community, dbContext);
			
		} else throw new IllegalStateException("User is not active and can therefore not create communities: " + creatorUsername );
	}

	/**
	 * Internal Helper to create a community and when the creator is admin set
	 * it to confirmed
	 * 
	 * @param creator
	 * @param community
	 * @param dbContext
	 * 
	 * @return the created, persisted but not commited community
	 *
	 * @throws DuplicateEntityException
	 *             if the community already exists
	 */
	protected Community createCommunity(final Person creator, final Community community, final DbContext dbContext) {

		CommunityDAO communityDao = dbContext.getCommunityDAO();

		if (communityDao.getByName(community.getName()) == null) {

			if (creator.isAdmin()) {
				community.setConfirmedBy(creator);
			}

			communityDao.insert(community);

			return community;
		} else {
			throw new DuplicateEntityException("Community already exists: " + community.getName());
		}

	}
}
