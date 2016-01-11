package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceFacade;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.feed.FeedServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.test.util.RemoteTestBase;
import at.fhj.swd13.pse.test.util.SleepUtil;

public class ChatServiceIT extends RemoteTestBase {

	private static ChatService chatService;
	private static FeedService feedService;
	private static UserService userService;
	
	@BeforeClass
    public static void setupServices() throws NamingException {
		chatService = lookup(ChatServiceFacade.class, ChatService.class);
		feedService = lookup(FeedServiceFacade.class, FeedService.class);
		userService = lookup(UserServiceFacade.class, UserService.class);
    }	
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
		userService.updateNullPasswords();
    }	
	
    /*
     * PSE2015-28 Als angemeldeter Admin möchte ich Communities freischalten können
     */
    @Test
    public void ConfirmCommunity() {
    	
    	assertEquals(1, chatService.getUnconfirmedCommunities().size());
    	Community unconfirmedCommunity = chatService.getUnconfirmedCommunities().get(0);
    	assertNull(unconfirmedCommunity.getConfirmedBy());
    	
    	Person admin = userService.getUser("padmin");
    	assertNotNull(admin);
    	
    	chatService.confirmCommunity(admin, unconfirmedCommunity);
	
    	Community community = chatService.getCommunity(unconfirmedCommunity.getCommunityId());
    	assertNotNull(community);
    	assertTrue(community.getConfirmedBy().equals(admin));
    }
    
    /*
     * PSE2015-28 Als angemeldeter Admin möchte ich Communities freischalten können
     */
    @Test
    public void DeclineCommunity() {
    	
    	assertEquals(1, chatService.getUnconfirmedCommunities().size());
    	Community unconfirmedCommunity = chatService.getUnconfirmedCommunities().get(0);
    	assertNull(unconfirmedCommunity.getConfirmedBy());
    	
    	Person admin = userService.getUser("padmin");
    	assertNotNull(admin);
    	
    	chatService.declineCommunity(admin, unconfirmedCommunity);
    	
    	assertNull(chatService.getCommunity(unconfirmedCommunity.getCommunityId()));
    }
    
    /*
     * PSE2015-50 Als angemeldeter User kann ich einen neuen Community-Beitrag in einer ausgewählten Community erstellen.
     */
    @Test
    public void CommunityAddMessage()
    {
    	//Prepare Community list
    	List<Community> communities = new ArrayList<>();
    	communities.add(chatService.getCommunity(1000));
    	
    	//Create new message
    	feedService.saveMessage("IT Test addMessage headline", "IT Test addMessage", "haringst13", 
    			null, null, communities, new ArrayList<MessageTag>(), new Date(), null);
    	SleepUtil.sleep(1000);

    	Community community = chatService.getCommunity(1000);
    	assertNotNull(community);
    	assertEquals(1, community.getMessages().size());
    	assertTrue(community.getMessages().get(0).getHeadline().equals("IT Test addMessage headline"));
    	assertTrue(community.getMessages().get(0).getMessage().equals("IT Test addMessage"));
    }
    
    /*
     * PSE2015-58 Als angemeldeter Benutzer habe ich auf der Übersichtsseite der Communities eine Liste aller öffentlichen und privaten Communities  
     */
    @Test
    public void GetAllAssessibelCommunities()
    {
    	List<Community> communities = new ArrayList<Community>();
    	
    	communities = chatService.getAllAccessibleCommunities();
    	assertNotNull(communities);
    	assertEquals(4, communities.size());
    	
    	Community communityPublicCommunity = chatService.getCommunity("Public community");
    	assertNotNull(communityPublicCommunity);
    	assertTrue(communities.contains(communityPublicCommunity));
    	
    	Community communitySWD = chatService.getCommunity("SWD");
    	assertNotNull(communitySWD);
    	assertTrue(communities.contains(communitySWD));
    	
    	Community communityAddMessage = chatService.getCommunity("AddMessage");
    	assertNotNull(communityAddMessage);
    	assertTrue(communities.contains(communityAddMessage));
    	
    	Community communityPrivate = chatService.getCommunity("Private community");
    	assertNotNull(communityPrivate);
    	assertTrue(communities.contains(communityPrivate));
    }
    
    @Test
    public void GetAllAssessibelCommunitiesWithSearchString()
    {
    	List<Community> communities = new ArrayList<Community>();
    	
    	communities = chatService.getAllAccessibleCommunities("SWD");
    	assertNotNull(communities);
    	assertEquals(communities.size(), 1);
    	
    	Community communitySWD = chatService.getCommunity("SWD");
    	assertNotNull(communitySWD);
    	assertTrue(communities.contains(communitySWD));
    }
    
    @Test
    public void GetAllAssessibelCommunitiesWithSearchStringNotFound()
    {
    	List<Community> communities = new ArrayList<Community>();
    	
    	communities = chatService.getAllAccessibleCommunities("SK8");
    	assertNotNull(communities);
    	assertEquals(communities.size(), 0);
    }
    
    /*
     *  PSE2015-54 Als angemeldeter Benutzer kann ich einer öffentlichen Community beitreten oder Mitgliedschaft bei einer privaten Community beantragen.
     */
    @Test
    public void CreateCommunityMember()
    {
    	Community communityPublicCommunity = chatService.getCommunity("Public community");
    	assertNotNull(communityPublicCommunity);
    	
    	Person user = userService.getUser("haringst13");
    	assertNotNull(user);

    	assertFalse(chatService.isPersonMemberOfCommunity(user, communityPublicCommunity));
    	
    	chatService.createCommunityMember(user, communityPublicCommunity);
    	
    	communityPublicCommunity = chatService.getCommunity("Public community");
    	assertNotNull(communityPublicCommunity);
    	  
    	assertTrue(chatService.isPersonMemberOfCommunity(user, communityPublicCommunity));
    }
    
    @Test
    public void GetUnconfirmedCommunities() {
    	assertEquals(1, chatService.getUnconfirmedCommunities().size());
    	Community unconfirmedCommunity = chatService.getUnconfirmedCommunities().get(0);
    	assertNull(unconfirmedCommunity.getConfirmedBy());
    	assertTrue(unconfirmedCommunity.getName().equals("Not confirmed community"));
    }
    
    @Test
    public void GetCommunityWithName() {
    	Community community = chatService.getCommunity("Not confirmed community");
    	assertNotNull(community);
    	assertNull(community.getConfirmedBy());
    	assertTrue(community.getName().equals("Not confirmed community"));
    	assertEquals(999, community.getCommunityId());
    }
    
    @Test
    public void GetCommunityWithId() {
    	Community community = chatService.getCommunity(999);
    	assertNotNull(community);
    	assertNull(community.getConfirmedBy());
    	assertTrue(community.getName().equals("Not confirmed community"));
    	assertEquals(999, community.getCommunityId());
    }
    
    @Test
    public void PersonNotMemberOfCommunity()
    {
    	Community community = chatService.getCommunity("Not confirmed community");
    	assertNotNull(community);
    	
    	Person admin = userService.getUser("padmin");
    	assertNotNull(admin);
    	
    	assertFalse(chatService.isPersonMemberOfCommunity(admin, community));
    }
    
    @Test
    public void PersonMemberOfCommunity()
    {
    	Community community = chatService.getCommunity("SWD");
    	assertNotNull(community);
    	
    	Person user = userService.getUser("pompenig13");
    	assertNotNull(user);
    	
    	assertTrue(chatService.isPersonMemberOfCommunity(user, community));
    }
    
    @Test
    public void GetCommunityMembersList()
    {
    	Community community = chatService.getCommunity(1000);
    	assertNotNull(community);
    	
    	Person user = userService.getUser("haringst13");
    	assertNotNull(user);
    	
    	List<CommunityMember> communityMembers = new ArrayList<CommunityMember>();
    	
    	communityMembers = chatService.getCommunityMembersList(community);
    	assertEquals(communityMembers.size(), 1);
    	assertTrue(communityMembers.get(0).getMember().equals(user));
    }
    
    @Test
    public void GetCommunityMemberNotFound()
    {    	
    	Person user = userService.getUser("pompenig13");
    	assertNotNull(user);
    	
    	assertNull(chatService.getCommunityMember(chatService.getCommunity(1000), user));
    }
    
    @Test
    public void GetCommunityMember()
    {    	
    	Community community = chatService.getCommunity(1000);
    	assertNotNull(community);
    	
    	Person user = userService.getUser("haringst13");
    	assertNotNull(user);
    	
    	CommunityMember communityMember = chatService.getCommunityMember(community, user);
    	assertNotNull(communityMember);
    	assertTrue(communityMember.getMember().equals(user));
    	assertTrue(communityMember.getCommunity().equals(community));
    }
}
 