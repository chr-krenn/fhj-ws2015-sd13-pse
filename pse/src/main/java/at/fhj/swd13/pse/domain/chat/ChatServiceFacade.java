package at.fhj.swd13.pse.domain.chat;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.dto.MessageDTO;

@Stateless
@Remote(ChatService.class)
public class ChatServiceFacade implements ChatService {

	@EJB(beanName="ChatServiceImpl")
	private ChatService chatService;
	
	@Override
	public Community getCommunity(int communityId) {
		return chatService.getCommunity(communityId);
	}

	@Override
	public List<CommunityMember> getAllUnconfirmedCommunityMembers() {
		return chatService.getAllUnconfirmedCommunityMembers();
	}

	@Override
	public Community getCommunity(String communityName) {
		return chatService.getCommunity(communityName);
	}

	@Override
	public Community createChatCommunity(String creatorUsername, String communityName, boolean invitationOnly) {
		return chatService.createChatCommunity(creatorUsername, communityName, invitationOnly);
	}

	@Override
	public List<Community> getUnconfirmedCommunities() {
		return chatService.getUnconfirmedCommunities();
	}

	@Override
	public void confirmCommunity(Person adminPerson, Community unconfirmed) {
		chatService.confirmCommunity(adminPerson, unconfirmed);
	}

	@Override
	public void declineCommunity(Person adminPerson, Community unconfirmed) {
		chatService.declineCommunity(adminPerson, unconfirmed);
	}

	@Override
	public List<Community> getPossibleTargetCommunities(String username, String needle) {
		return chatService.getPossibleTargetCommunities(username, needle);
	}

	@Override
	public int createAllPrivateCommunities() {
		return chatService.createAllPrivateCommunities();
	}

	@Override
	public List<Community> getAllCommunities() {
		return chatService.getAllCommunities();
	}

	@Override
	public List<Community> getAllAccessibleCommunities() {
		return chatService.getAllAccessibleCommunities();
	}

	@Override
	public List<Community> getAllAccessibleCommunities(String searchfieldText) {
		return chatService.getAllAccessibleCommunities(searchfieldText);
	}

	@Override
	public String resolveReceipientsMail(Message message) {
		return chatService.resolveReceipientsMail(message);
	}

	@Override
	public CommunityMember createCommunityMember(Person creator, Community community) {
		return chatService.createCommunityMember(creator, community);
	}

	@Override
	public Boolean isPersonMemberOfCommunity(Person person, Community community) {
		return chatService.isPersonMemberOfCommunity(person, community);
	}

	@Override
	public List<CommunityMember> getCommunityMembersList(Community community) {
		return chatService.getCommunityMembersList(community);
	}

	@Override
	public CommunityMember getCommunityMember(Community community, Person person) {
		return chatService.getCommunityMember(community, person);
	}

	@Override
	public Community getPrivateCommunity(Person person) {
		return chatService.getPrivateCommunity(person);
	}

	@Override
	public MessageDTO addComment(String username, int commentedMessageId, String headline, String comment) {
		return chatService.addComment(username, commentedMessageId, headline, comment);
	}

	@Override
	public void confirmCommunityMember(Person adminPerson, CommunityMember unconfirmed) {
		chatService.confirmCommunityMember(adminPerson, unconfirmed);
	}

	@Override
	public void declineCommunityMember(Person adminPerson, CommunityMember unconfirmed) {
		chatService.declineCommunityMember(adminPerson, unconfirmed);
	}

	@Override
	public CommunityMember getUnconfirmedCommunityMember(int communityId) {
		return chatService.getUnconfirmedCommunityMember(communityId);
	}

}
