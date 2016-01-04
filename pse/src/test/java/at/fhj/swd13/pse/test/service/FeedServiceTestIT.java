package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.feed.FeedServiceFacade;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.domain.tag.TagServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class FeedServiceTestIT extends RemoteTestBase {

	private FeedService feedService;
	private TagService tagService;
	private UserService userService;
	private Person user;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
        feedService = lookup(FeedServiceFacade.class, FeedService.class);
        tagService = lookup(TagServiceFacade.class, TagService.class);
        userService = lookup(UserServiceFacade.class, UserService.class);
        user = userService.getUser("pompenig13");
    }	
	
    /*
     * PSE2015-19 Als angemeldeter Benutzer möchte ich auf meiner Startseite alle Activities, für die ich berechtigt bin, sehen.
     * 
     * Similar to DbMessageTest.testActivityStream (except for private message)
     */
    @Test
    public void loadActivityStream() {
    	List<MessageDTO> activities = feedService.loadFeedForUser(user);
		assertEquals(5, activities.size());
    }
    
    /*
     * PSE2015-25 Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde.
     */
    @Test
    public void likeActivity() throws Exception {
    	int messageID = 14;
    	List<MessageRating> likes = feedService.getMessageById(messageID).getMessageRatings();
    	feedService.rateMessage(messageID, user);
    	assertEquals(likes.size()+1,feedService.getMessageById(messageID).getMessageRatings().size());
    }
    
    /*
     * PSE2015-26 Als angemeldeter Benutzer möchte ich sehen können, wer eine Activity geliked hat.
     */
    @Test
    public void getPersonsWhoLikedActivity() throws EntityNotFoundException {
    	List<MessageRating> likes = feedService.getMessageById(1).getMessageRatings();
    	List<Person> persons = new ArrayList<>();
    	for(MessageRating like : likes) {
    		persons.add(like.getPerson());
    	}
    	assertEquals(2, persons.size());
    	assertTrue(persons.contains(user));
    }
    
    /*
     * PSE2015-27 Als angemeldeter Benutzer möchte ich die Kommentare zu einer Activity lesen können.
     */
    @Test
    public void getComments() throws EntityNotFoundException {
    	assertEquals(3,feedService.loadComments(1).size());
    }
    
    /*
     * PSE2015-48 	Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können
     */
    @Test
    public void sendMessageToContact() throws EntityNotFoundException {
    	Person contact = user.getContacts().iterator().next();
    	int numberOfMessages = feedService.loadFeedForUser(contact).size();
    	List<Community> communities = new ArrayList<>();
    	communities.add(contact.getPrivateCommunity());
    	feedService.saveMessage("IT Test headline", "IT Test Text", user.getUserName(), null, null, 
    			communities, new ArrayList<MessageTag>(), new Date(), null);
    	List<MessageDTO> messages = feedService.loadFeedForUser(contact); 
    	assertEquals(numberOfMessages+1,messages.size());
    	//Newest message is first in list -> index 0
    	assertEquals("IT Test headline",messages.get(0).getHeadline());
    }
    
    
}
