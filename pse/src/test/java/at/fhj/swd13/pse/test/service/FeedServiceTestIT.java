package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.feed.FeedServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class FeedServiceTestIT extends RemoteTestBase {

	private FeedService feedService;
	private UserService userService;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
        feedService = lookup(FeedServiceFacade.class, FeedService.class);
        userService = lookup(UserServiceFacade.class, UserService.class);
        userService.updateNullPasswords();
        
        executeSqlScript("SQL/testdata_DBMessageTest.sql");
    }	
	
    /*
     * PSE2015-19 Als angemeldeter Benutzer möchte ich auf meiner Startseite alle Activities, für die ich berechtigt bin, sehen.
     * 
     * Similar to DbMessageTest.testActivityStream
     */
    @Test
    public void loadActivityStream() {
    	Person user = userService.getUser("pompenig13");
    	List<MessageDTO> activities = feedService.loadFeedForUser(user);
    	
		assertEquals(6, activities.size());
    }
}
