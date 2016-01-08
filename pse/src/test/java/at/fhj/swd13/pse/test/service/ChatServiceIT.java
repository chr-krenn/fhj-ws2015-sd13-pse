package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class ChatServiceIT extends RemoteTestBase {

	private ChatService chatService;
	private UserService userService;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
        chatService = lookup(ChatServiceFacade.class, ChatService.class);
        userService = lookup(UserServiceFacade.class, UserService.class);
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
     * PSE2015-58 Als angemeldeter Benutzer habe ich auf der Übersichtsseite der Communities eine Liste aller öffentlichen und privaten Communities  
     */
    @Test
    public void getAllAssessibelCommunities()
    {
    	List<Community> communities = new ArrayList<Community>();
    	
    	communities = chatService.getAllAccessibleCommunities();
    	assertNotNull(communities);
    	assertEquals(communities.size(), 3);
    	
    	Community communityPublicCommunity = chatService.getCommunity("Public community");
    	assertNotNull(communityPublicCommunity);
    	assertTrue(communities.contains(communityPublicCommunity));
    	
    	Community communitySWD = chatService.getCommunity("SWD");
    	assertNotNull(communitySWD);
    	assertTrue(communities.contains(communitySWD));
    	
    	Community communityPrivate = chatService.getCommunity("Private community");
    	assertNotNull(communityPrivate);
    	assertTrue(communities.contains(communityPrivate));
    }
    
    @Test
    public void GetUnconfirmedCommunities() {
    	assertEquals(1, chatService.getUnconfirmedCommunities().size());
    	Community unconfirmedCommunity = chatService.getUnconfirmedCommunities().get(0);
    	assertNull(unconfirmedCommunity.getConfirmedBy());
    	assertTrue(unconfirmedCommunity.getName().equals("Not confirmed community"));
    }
    
    @Test
    public void GetCommunity() {
    	Community community = chatService.getCommunity("Not confirmed community");
    	assertNotNull(community);
    	assertNull(community.getConfirmedBy());
    	assertTrue(community.getName().equals("Not confirmed community"));
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
 
}
 