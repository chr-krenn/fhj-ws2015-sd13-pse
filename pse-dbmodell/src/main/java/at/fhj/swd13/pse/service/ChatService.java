package at.fhj.swd13.pse.service;

import java.util.List;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;

public interface ChatService {

	/**
	 * Create a public chat community. If the creator is an administrator, the
	 * community is confirmed. Method creates a db-context and calls
	 * createChatCommunity
	 * 
	 * also creates an admin membership for the creator
	 * 
	 * @param creator username of the creator of the chat
	 * @param communityName name of the community to create
	 * @param invitationOnly when true the members can only join the community when community admin confirms request
	 * 
	 * @return instance of the created community
	 * 
	 * @throws DuplicateEntityException
	 *             if the community already exists
	 * 
	 */
	Community createChatCommunity(String creatorUsername, String communityName, boolean invitationOnly)
			throws Exception;

	/**
	 * Create a public chat community. If the creator is an administrator, the
	 * community is confirmed.
	 *
	 * also creates an admin membership for the creator
	 * 
	 * @param creator username of the creator of the chat
	 * @param communityName name of the community to create
	 * @param invitationOnly when true the members can only join the community when community admin confirms request
	 * @param dbContext session to the persistent storage 
	 * 
	 * @return instance of the created community	 * 
	 * @throws DuplicateEntityException
	 *             if the community already exists
	 * @throws EntityNotFoundException
	 *             if the creator is no known user
	 * @throws IllegalStateException
	 * 				if the user is not active
	 */
	Community createChatCommunity(String creatorUsername, String communityName, boolean invitationOnly,
			DbContext dbContext);

	/**
	 * Get a list of all currently unconfirmed communities
	 * 
	 * @param dbContext session to the persistent store
	 * 
	 * @return List of unconfirmed communities or an empty list
	 */
	List<Community> getUnconfirmedCommunities( DbContext dbContext);

	/**
	 * Confirm an unconfirmed community
	 *  	
	 * @param adminPerson administrator that confirms the community
	 * 
	 * @param unconfirmed the unconfirmed community
	 * 
	 * @throws IllegalStateException when the adminPerson is not active or not an admin
	 */
	void confirmCommunity( final Person adminPerson, Community unconfirmed);

}