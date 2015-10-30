package at.fhj.swd13.pse.domain.chat;

import java.util.List;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.service.DuplicateEntityException;

public interface ChatService {

	/**
	 * Get the community with the given id
	 * 
	 * @param communityId
	 *            the id of the community to retrieve
	 * 
	 * @return community or null if not found
	 */
	Community getCommunity(final int communityId);

	/**
	 * Get the community with the given name
	 * 
	 * @param communityName
	 *            the id of the community to retrieve
	 * 
	 * @return community or null if not found
	 */
	Community getCommunity(final String communityName);

	/**
	 * Create a public chat community. If the creator is an administrator, the
	 * community is confirmed.
	 *
	 * also creates an admin membership for the creator
	 * 
	 * @param creator
	 *            username of the creator of the chat
	 * @param communityName
	 *            name of the community to create
	 * @param invitationOnly
	 *            when true the members can only join the community when
	 *            community admin confirms request
	 * @param dbContext
	 *            session to the persistent storage
	 * 
	 * @return instance of the created community *
	 * @throws DuplicateEntityException
	 *             if the community already exists
	 * @throws EntityNotFoundException
	 *             if the creator is no known user
	 * @throws IllegalStateException
	 *             if the user is not active
	 */
	Community createChatCommunity(String creatorUsername, String communityName,
			boolean invitationOnly) throws EntityNotFoundException;

	/**
	 * Get a list of all currently unconfirmed communities
	 * 
	 * @param dbContext
	 *            session to the persistent store
	 * 
	 * @return List of unconfirmed communities or an empty list
	 */
	List<Community> getUnconfirmedCommunities();

	/**
	 * Confirm an unconfirmed community
	 * 
	 * @param adminPerson
	 *            administrator that confirms the community
	 * 
	 * @param unconfirmed
	 *            the unconfirmed community
	 * 
	 * @throws IllegalStateException
	 *             when the adminPerson is not active or not an admin
	 */
	void confirmCommunity(final Person adminPerson, Community unconfirmed);

	/**
	 * Get a list of communities that his user can post to and that match the
	 * given needle (name starts with)
	 * 
	 * @param username
	 *            username to get the communities for
	 * 
	 * @param needle
	 *            needle for the comparison
	 * 
	 * @return
	 */
	List<Community> getPossibleTargetCommunities(final String username,
			final String needle);

	/**
	 * Creates the user private communities (every user has a community onto
	 * which his private messages are pushed)
	 * 
	 * @return the number of created communities
	 */
	int createAllPrivateCommunities();
	
	List<Community> getAllCommunities();
	
	List <Community> getAllCommunities(String searchfieldText);

	String resolveReceipientsMail( final Message message );
	
	CommunityMember createCommunityMember(final Person creator, final Community community);
	Boolean isPersonMemberOfCommunity( final Person person, final Community community );
	
}