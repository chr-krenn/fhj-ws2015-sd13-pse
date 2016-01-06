package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceFacade;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.document.DocumentServiceFacade;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.feed.FeedServiceFacade;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.domain.tag.TagServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.test.util.RemoteTestBase;
import at.fhj.swd13.pse.test.util.SleepUtil;

public class FeedServiceTestIT extends RemoteTestBase {

	private FeedService feedService;
	private ChatService chatService;
	private TagService tagService;
	private DocumentService documentService;
	private UserService userService;
	private Person user;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
        feedService = lookup(FeedServiceFacade.class, FeedService.class);
        chatService = lookup(ChatServiceFacade.class, ChatService.class);
        tagService = lookup(TagServiceFacade.class, TagService.class);
        documentService = lookup(DocumentServiceFacade.class, DocumentService.class);
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
    	feedService.removeRating(messageID, user);
    	assertEquals(likes.size(),feedService.getMessageById(messageID).getMessageRatings().size());
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
    	feedService.removeMessage(messages.get(0).getId());
    }
    
    /*
     * PSE2015-60 Beim Erfassen einer Nachricht kann ich Tags auswählen, um meine Nachricht zu klassifizieren.
     */
    @Test
    public void sendMessageWithTags() throws Exception {
    	//Prepare Community list
    	List<Community> communities = new ArrayList<>();
    	communities.add(chatService.getCommunity(100));
    	
    	//Prepare MessageTag list (Add MessageTag with existing Tag)
    	List<MessageTag> tags = new ArrayList<MessageTag>();
    	tags.add(new MessageTag(tagService.getTagByToken("Software")));
    	
    	//Create new message
    	feedService.saveMessage("IT Test with Tags headline", "IT Test with Tags Text", user.getUserName(), 
    			null, null, communities, tags, new Date(), null);
    	SleepUtil.sleep(1000);
    	
    	//Get first (= newest) message of Message list for community
    	MessageDTO m = feedService.loadNews(100).get(0);
    	
    	assertTrue(!m.getTags().isEmpty());
    	assertEquals("Software",m.getTags().get(0));
    	feedService.removeMessage(m.getId());
    }
    
    /*
     * PSE2015-66 "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream auf meiner Startseite die Details der Activity ansehen können."
     * 
     * FIXME: Only working with absolute path to file system...
     */
    @Test
    public void getMessageDetailsWithIcon() throws Exception {
    	//Prepare Community list
    	List<Community> communities = new ArrayList<>();
    	communities.add(chatService.getCommunity(100));
    	
    	//Prepare document
    	//Not working with "src/test/resources/testDocs/no_img.png"
    	Document icon = documentService.store("pic", "D:\\no_img.png");
    	assertTrue(icon != null);
    	
    	String headline = "IT Test with Icon headline";
    	String text = "IT Test with Icon Text";

    	//Create new message
		feedService.saveMessage(headline, text, user.getUserName(), 
    			null, icon, communities, new ArrayList<MessageTag>(), new Date(), null);
    	SleepUtil.sleep(1000);
    	
    	//Get Id of first (= newest) message of Message list for community
    	int messageId = feedService.loadNews(100).get(0).getId();
    	
    	//Check data
    	Message m = feedService.getMessageById(messageId);
    	assertEquals(icon, m.getIcon());
    	assertEquals(headline, m.getHeadline());
    	assertEquals(text, m.getMessage());
    	assertEquals(user, m.getPerson());
    	assertEquals(communities,m.getCommunities());
    }
    
    @Test
    public void getMessageDTOByIdTest() {
    	try {
			MessageDTO m = feedService.getMessageDTOById(1);
			assertTrue(m != null);
			assertEquals(1, m.getId());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			fail();
		}
    }
    
    @Test
    public void loadFeedTest() {
		List<MessageDTO> messages = feedService.loadFeed();
		assertTrue(messages != null);
		assertEquals(15, messages.size());
    }
    
    @Test
    public void setCommentsTest() {
		try {
			MessageDTO m = feedService.getMessageDTOById(1);
			assertTrue(m.getComments() == null); //Comments not loaded, should be empty
			m = feedService.setComments(m); //Load comments
			assertFalse(m.getComments() == null); //FIXME: why still null??
			assertEquals("Comment 1", m.getComments().get(0).getText());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			fail();
		}
    	
    }
    
    @Test
    public void setImageRefTest() {
    	//Prepare document
    	//Not working with "src/test/resources/testDocs/no_img.png"
    	Document icon = documentService.store("pic", "D:\\no_img.png");
    	assertTrue(icon != null);
    	
		try {
			//Get message & add icon
	    	Message m = feedService.getMessageById(1);
			feedService.updateMessage(m.getMessageId(), m.getHeadline(), m.getMessage(), null, icon, m.getMessageTags(), m.getValidFrom(), m.getExpiresOn());
		
			//Get messsageDTO
			MessageDTO mDTO = feedService.getMessageDTOById(1);
			assertTrue(mDTO.getImage() != null);
			
			//Check whether image ref is there
			assertTrue(mDTO.getImageRef() == null);
			
			mDTO = feedService.setImageRef(mDTO); 
			assertFalse(mDTO.getImageRef() == null);
			
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			fail();
		}
    }
}
