package at.fhj.swd13.pse.service;

import java.util.List;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * Chat service that provides all functionality for chatting and administration
 * of chats
 *
 */
public class ChatServiceImpl extends ServiceBase implements ChatService {

	/**
	 * Create an instance of the chat service
	 */
	public ChatServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.service.ChatService#createChatCommunity(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public Community createChatCommunity(final String creatorUsername, final String communityName,
			final boolean invitationOnly) throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			Community community = createChatCommunity(creatorUsername, communityName, invitationOnly, dbContext);

			dbContext.commit();

			return community;
		}
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.service.ChatService#createChatCommunity(java.lang.String, java.lang.String, boolean, at.fhj.swd13.pse.db.DbContext)
	 */
	@Override
	public Community createChatCommunity(final String creatorUsername, final String communityName,
			final boolean invitationOnly, final DbContext dbContext)  {

		try
		{
			Person creator = dbContext.getPersonDAO().getByUsername(creatorUsername, true);

			if (creator.isActive()) {

				Community community = new Community(communityName);

				community.setCreatedBy(creator);
				community.setInvitationOnly(invitationOnly);

				return createCommunity(creator, community, dbContext);
				
			} else throw new IllegalStateException("User is not active and can therefore not create communities: " + creatorUsername );	
		}
		catch(EntityNotFoundException ex)
		{
			//TODO Clarify which exception should be thrown
			throw new IllegalStateException("Unkown person provided");
		}
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.service.ChatService#getUnconfirmedCommunities(at.fhj.swd13.pse.db.entity.Person, at.fhj.swd13.pse.db.DbContext)
	 */
	@Override
	public List<Community> getUnconfirmedCommunities(  DbContext dbContext ) {
		
		
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
	protected Community createCommunity(final Person creator, final Community community, final DbContext dbContext) {

		CommunityDAO communityDao = dbContext.getCommunityDAO();

		if (communityDao.getByName(community.getName()) == null) {

			if (creator.isAdmin()) {
				community.setConfirmedBy(creator);
			}

			communityDao.insert(community);
			CommunityMember memberShip = community.addMember( creator, true );
			
			if ( memberShip != null ) {
				dbContext.persist( memberShip );
			}
			
			return community;
		} else {
			throw new DuplicateEntityException("Community already exists: " + community.getName());
		}

	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.service.ChatService#confirmCommunity(at.fhj.swd13.pse.db.entity.Person, at.fhj.swd13.pse.db.entity.Community)
	 */
	public void confirmCommunity( final Person adminPerson, Community unconfirmed) {
		
		if ( adminPerson.isActive() && adminPerson.isAdmin() ) {
		
			adminPerson.addConfirmedCommunities( unconfirmed );
			
		} else {
			throw new IllegalStateException( "Person confirming the community is either not active or not an admin: " + adminPerson.getUserName() );
		}		
	}
}
