package at.fhj.swd13.pse.domain.chat;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.dto.MessageDTO;

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
	
	List<CommunityMember> getAllUnconfirmedCommunityMembers();
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
	 */
	Community createChatCommunity(String creatorUsername, String communityName,
			boolean invitationOnly);

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
	 */
	void confirmCommunity(final Person adminPerson, Community unconfirmed);

	void declineCommunity(final Person adminPerson, Community unconfirmed);

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

	List<Community> getAllAccessibleCommunities();
	
	List <Community> getAllAccessibleCommunities(String searchfieldText);

	String resolveReceipientsMail( final Message message );
	
	CommunityMember createCommunityMember(final Person creator, final Community community);
	Boolean isPersonMemberOfCommunity( final Person person, final Community community );
	
	List<CommunityMember> getCommunityMembersList( final Community community );
	
	CommunityMember getCommunityMember( final Community community, final Person person );
	/**
	 * Get the privat community for a person
	 * 
	 * @param person
	 *          
	 * @return the number of created communities
	 */
	Community getPrivateCommunity(Person person);
	
	/**
	 * Add a comment to a message
	 * 
	 * @param username user who comments
	 * 
	 * @param commentedMessageId message id that is commented
	 * 
	 * @param comment text of the comment itself
	 */
	MessageDTO addComment( final String username, final int commentedMessageId, final String headline, final String comment );
	
	void confirmCommunityMember(Person adminPerson, CommunityMember unconfirmed);
	
	void declineCommunityMember(Person adminPerson, CommunityMember unconfirmed);
	
	CommunityMember getUnconfirmedCommunityMember(int communityId);
}
