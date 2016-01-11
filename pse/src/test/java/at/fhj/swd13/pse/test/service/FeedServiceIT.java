package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.BeforeClass;
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

public class FeedServiceIT extends RemoteTestBase {

	private static FeedService feedService;
	private static ChatService chatService;
	private static TagService tagService;
	private static DocumentService documentService;
	private static UserService userService;
	private static Person user;

	@BeforeClass
	public static void setupServices() throws NamingException {
		feedService = lookup(FeedServiceFacade.class, FeedService.class);
		chatService = lookup(ChatServiceFacade.class, ChatService.class);
		tagService = lookup(TagServiceFacade.class, TagService.class);
		documentService = lookup(DocumentServiceFacade.class, DocumentService.class);
		userService = lookup(UserServiceFacade.class, UserService.class);
		user = userService.getUser("pompenig13");
	}

	@Before
	public void setup() throws NamingException {
		prepareDatabase();
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
		assertEquals(likes.size() + 1, feedService.getMessageById(messageID).getMessageRatings().size());
		feedService.removeRating(messageID, user);
		assertEquals(likes.size(), feedService.getMessageById(messageID).getMessageRatings().size());
	}

	/*
	 * PSE2015-26 Als angemeldeter Benutzer möchte ich sehen können, wer eine Activity geliked hat.
	 */
	@Test
	public void getPersonsWhoLikedActivity() throws EntityNotFoundException {
		List<MessageRating> likes = feedService.getMessageById(1).getMessageRatings();
		List<Person> persons = new ArrayList<>();
		for (MessageRating like : likes) {
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
		assertEquals(3, feedService.loadComments(1).size());
	}

	/*
	 * PSE2015-48 Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können
	 */
	@Test
	public void sendMessageToContact() throws EntityNotFoundException {
		Person contact = user.getContacts().iterator().next();
		int numberOfMessages = feedService.loadFeedForUser(contact).size();
		List<Community> communities = new ArrayList<>();
		communities.add(contact.getPrivateCommunity());
		feedService.saveMessage("IT Test headline", "IT Test Text", user.getUserName(), null, null, communities, new ArrayList<MessageTag>(), new Date(), null);
		List<MessageDTO> messages = feedService.loadFeedForUser(contact);
		assertEquals(numberOfMessages + 1, messages.size());
		// Newest message is first in list -> index 0
		assertEquals("IT Test headline", messages.get(0).getHeadline());
		feedService.removeMessage(messages.get(0).getId());
	}
	
	/*
	 * PSE2015-57 Als angemeldeter Benutzer kann ich einem anderen Benutzer eine private Nachricht senden.
	 */
	@Test
	public void sendMessageToUser() throws EntityNotFoundException {
		Person contact = userService.getUser("poschdan13");
		int numberOfMessages = feedService.loadFeedForUser(contact).size();
		List<Community> communities = new ArrayList<>();
		communities.add(contact.getPrivateCommunity());
		feedService.saveMessage("IT Test headline", "IT Test Text", user.getUserName(), null, null, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);
		List<MessageDTO> messages = feedService.loadFeedForUser(contact);
		assertEquals(numberOfMessages + 1, messages.size());
		// Newest message is first in list -> index 0
		assertEquals("IT Test headline", messages.get(0).getHeadline());
		feedService.removeMessage(messages.get(0).getId());
	}
	
	/*
	 * PSE2015-64 Als Benutzer kann ich eine Nachricht schreiben und diese in einer oder mehreren Communities veröffentlichen.
	 */
	@Test
	public void sendMessageToCommunity() throws EntityNotFoundException {
		Person contact = userService.getUser("haringst13");
		int numberOfMessages = feedService.loadFeedForUser(contact).size();
		
		List<Community> communities = new ArrayList<>();
		communities.add(userService.getUser("haringst13").getConfirmedCommunities().get(0));
		feedService.saveMessage("IT Test headline", "IT Test Text", user.getUserName(), null, null, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);
		List<MessageDTO> messages = feedService.loadFeedForUser(contact);
		assertEquals(numberOfMessages + 1, messages.size());
		// Newest message is first in list -> index 0
		assertEquals("IT Test headline", messages.get(0).getHeadline());
		feedService.removeMessage(messages.get(0).getId());
	}

	/*
	 * PSE2015-60 Beim Erfassen einer Nachricht kann ich Tags auswählen, um meine Nachricht zu klassifizieren.
	 */
	@Test
	public void sendMessageWithTags() throws Exception {
		// Prepare Community list
		List<Community> communities = new ArrayList<>();
		communities.add(chatService.getCommunity(100));

		// Prepare MessageTag list (Add MessageTag with existing Tag)
		List<MessageTag> tags = new ArrayList<MessageTag>();
		tags.add(new MessageTag(tagService.getTagByToken("Software")));

		// Create new message
		feedService.saveMessage("IT Test with Tags headline", "IT Test with Tags Text", user.getUserName(), null, null, communities, tags, new Date(), null);
		SleepUtil.sleep(1500);

		// Get first (= newest) message of Message list for community
		MessageDTO m = feedService.loadNews(100).get(0);

		assertTrue(!m.getTags().isEmpty());
		assertEquals("Software", m.getTags().get(0));
		feedService.removeMessage(m.getId());
	}

	/*
	 * PSE2015-66 "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream auf meiner Startseite die Details der Activity ansehen können."
	 * 
	 */
	@Test
	public void getMessageDetailsWithIcon() throws Throwable {
		// Prepare Community list
		List<Community> communities = new ArrayList<>();
		communities.add(chatService.getCommunity(100));

		// Prepare document
		prepareFile("testDocs/no_img.png", "/tmp/no_img.png");
		Document icon = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(icon != null);

		String headline = "IT Test with Icon headline";
		String text = "IT Test with Icon Text";

		// Create new message
		feedService.saveMessage(headline, text, user.getUserName(), null, icon, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);

		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(100).get(0).getId();

		// Check data
		Message m = feedService.getMessageById(messageId);
		assertEquals(icon, m.getIcon());
		assertEquals(headline, m.getHeadline());
		assertEquals(text, m.getMessage());
		assertEquals(user, m.getPerson());
		assertEquals(communities, m.getCommunities());
	}

	/*
     * PSE2015-24: Als Mitglied einer Community möchte ich auf der Seite der Community alle für die Community erfassten Activities sehen
     * 
     */
    @Test
    public void loadCommunityActivities() {
    	List<MessageDTO> activities = feedService.loadNews(100);
		assertEquals(2, activities.size());
    }
    
	@Test
	public void getMessageDTOByIdTest() {
		MessageDTO m = feedService.getMessageDTOById(1);
		assertTrue(m != null);
		assertEquals(1, m.getId());
	}

	@Test
	public void loadFeedTest() {
		List<MessageDTO> messages = feedService.loadFeed();
		assertTrue(messages != null);
		assertEquals(15, messages.size());
	}

	/*
     * PSE2015-65 Als angemeldeter Benutzer kann ich bestehende Nachrichten die mir angezeigt werden kommentieren.
     * 
     */
	@Test
	public void setCommentsTest() {
		MessageDTO m = feedService.getMessageDTOById(1);
		assertTrue(m.getComments() == null); // Comments not loaded, should be empty
		m = feedService.setComments(m); // Load comments
		assertFalse(m.getComments() == null);
		assertEquals("Comment 1", m.getComments().get(0).getText());
	}

	@Test
	public void setImageRefAndUpdateTest() throws Throwable {
		// Prepare document
		prepareFile("testDocs/no_img.png", "/tmp/no_img.png");

		Document icon = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(icon != null);

		// Get message & add icon
		Message m = feedService.getMessageById(1);
		feedService.updateMessage(m.getMessageId(), m.getHeadline(), m.getMessage(), null, icon, m.getMessageTags(), m.getValidFrom(), m.getExpiresOn());

		// Get messsageDTO
		MessageDTO mDTO = feedService.getMessageDTOById(1);
		assertTrue(mDTO.getImage() != null);

		// Check whether image ref is there
		assertTrue(mDTO.getImageRef() == null);

		mDTO = feedService.setImageRef(mDTO);
		assertFalse(mDTO.getImageRef() == null);
	}

	@Test
	public void setMessageLikesTest() {
		MessageDTO m = feedService.getMessageDTOById(1);
		assertEquals(0, m.getQuantityRatings());
		assertTrue(m.getRatingPersonsList() == null);
		assertFalse(m.isLike());

		m = feedService.setMessageLikes(m, "pompenig13");
		assertEquals(2, m.getQuantityRatings());
		assertFalse(m.getRatingPersonsList() == null);
		assertTrue(m.isLike());
	}

	@Test
	public void updateDTOafterRatingTest() {
		MessageDTO mDTO = feedService.getMessageDTOById(1);
		assertEquals(null, mDTO.getRatingPersonsList()); // user & one other person like this message, but ratingPersonsList hasn't been loaded
		assertFalse(mDTO.isLike());
		assertEquals(0, mDTO.getQuantityRatings());
		mDTO = feedService.setMessageLikes(mDTO, user.getUserName()); // Data is loaded
		mDTO = feedService.updateDTOafterRating(mDTO, user); // Data is set
		assertEquals(2, mDTO.getRatingPersonsList().size());
		assertTrue(mDTO.isLike());
		assertEquals(2, mDTO.getQuantityRatings());
	}

	@Test
	public void updateDTOafterRatingTest2() {
		MessageDTO mDTO = feedService.getMessageDTOById(5); // No one likes this message yet
		assertEquals(null, mDTO.getRatingPersonsList());
		assertFalse(mDTO.isLike());
		assertEquals(0, mDTO.getQuantityRatings());
		mDTO = feedService.setMessageLikes(mDTO, user.getUserName());
		mDTO = feedService.updateDTOafterRating(mDTO, user);
		assertEquals(1, mDTO.getRatingPersonsList().size());
		assertTrue(mDTO.isLike());
		assertEquals(1, mDTO.getQuantityRatings());
	}

	@Test
	public void updateDTOAfterRemoveTest() {
		MessageDTO mDTO = feedService.getMessageDTOById(1); // user & one other person like this message
		assertEquals(null, mDTO.getRatingPersonsList());
		assertFalse(mDTO.isLike());
		assertEquals(0, mDTO.getQuantityRatings());
		mDTO = feedService.setMessageLikes(mDTO, user.getUserName());
		mDTO = feedService.updateDTOAfterRemove(mDTO, user);
		assertEquals(1, mDTO.getRatingPersonsList().size());
		assertFalse(mDTO.isLike());
		assertEquals(1, mDTO.getQuantityRatings());
	}
	

	/*
	 * PSE2015-31 "Als angemeldeter Benutzer möchte ich auf meiner Startseite die vom Admin erfassten Unternehmens-News sehen können."
	 * PSE2015-33 "Als Benutzer sehe ich alle News-Einträge im Portal auf meiner Startseite."
	 * PSE2015-34 "Als angemeldeter Benutzer kann ich auf der Startseite die News-Beiträge sehen"
	 * 
	 */
	@Test
	public void loadNews() throws Throwable{
		SleepUtil.sleep(1000);
		List<MessageDTO> newsOld = feedService.loadNews(1);
		
		createTestNews("News1", "Text 1");
		createTestNews("News2", "Text 2");
		SleepUtil.sleep(1000);
		List<MessageDTO> news = feedService.loadNews(1);
		
		assertTrue(news.size() >= 2);
		assertEquals(newsOld.size() + 2, news.size());
	}
	
	
	
	/*
	 * PSE2015-35 "Als Portaladministrator kann ich die News-Beiträge editieren"
	 * 
	 */
	@Test
	public void editNews() throws Throwable{
		createTestNews("News 1", "Text 1");
		
		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(1).get(0).getId();

		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);
		
		prepareFile("testDocs/no_img.png","/tmp/no_img.png");
		
		Document icon = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(icon != null);

		Document doc = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(doc != null);
		
		String headline = "News-Test new";
		String text = "News Text new";
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH,  c.get(Calendar.DAY_OF_MONTH)-10);
		Date validFrom = c.getTime();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH,  c.get(Calendar.DAY_OF_MONTH)+10);
		Date validTo = c.getTime();
		
		feedService.updateMessage(messageId, headline, text, doc, icon, new ArrayList<MessageTag>(), validFrom, validTo);
		
		Message m = feedService.getMessageById(messageId);
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		// Check data
		assertEquals(icon, m.getIcon());
		assertEquals(headline, m.getHeadline());
		assertEquals(text, m.getMessage());
		assertEquals(communities, m.getCommunities());
		assertEquals(df.format(validFrom), df.format(m.getValidFrom()));
		assertEquals(df.format(validTo), df.format(m.getExpiresOn()));
		
		feedService.removeMessage(m.getMessageId());
	}
	
	/*
	 * PSE2015-36 Als Portaladministrator kann ich dem News-Beitrag einen Titel geben.
	 * 
	 */
	@Test
	public void createNewsTitle() throws Throwable{
		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);

		String headline = "Header Test";

		// Create new message
		feedService.saveMessage(headline, "Test", user.getUserName(), null, null, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);

		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(newsCommunity.getCommunityId()).get(0).getId();

		Message m = feedService.getMessageById(messageId);
		
		assertEquals(headline, m.getHeadline());
		
		feedService.removeMessage(m.getMessageId());
	}
	
	/*
	 * PSE2015-37 Als Portaladministrator kann ich dem News-Beitrag ein Symbol-Bild hinzufügen.
	 * 
	 */
	@Test
	public void createNewsSymbol() throws Throwable{
		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);

		prepareFile( "testDocs/no_img.png", "/tmp/no_img.pmg" );
		Document icon = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(icon != null);

		// Create new message
		feedService.saveMessage("Header", "Test", user.getUserName(), null, icon, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);

		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(newsCommunity.getCommunityId()).get(0).getId();

		Message m = feedService.getMessageById(messageId);
		
		assertEquals(icon, m.getIcon());
		
		feedService.removeMessage(m.getMessageId());
	}
	
	/*
	 * PSE2015-38 Als Portaladministrator kann ich dem News-Beitrag ein Symbol-Bild hinzufügen.
	 * 
	 */
	@Test
	public void createNewsValidDates() throws Throwable{
		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH,  c.get(Calendar.DAY_OF_MONTH)-10);
		Date validFrom = c.getTime();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH,  c.get(Calendar.DAY_OF_MONTH)+10);
		Date validTo = c.getTime();

		// Create new message
		feedService.saveMessage("Header", "Test", user.getUserName(), null, null, communities, new ArrayList<MessageTag>(), validFrom, validTo);
		SleepUtil.sleep(1000);

		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(newsCommunity.getCommunityId()).get(0).getId();

		Message m = feedService.getMessageById(messageId);
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		// Check data
		assertEquals(df.format(validFrom), df.format(m.getValidFrom()));
		assertEquals(df.format(validTo), df.format(m.getExpiresOn()));
		
		feedService.removeMessage(m.getMessageId());
	}
	
	/*
	 * PSE2015-39 Als Portaladministrator kann ich den News-Beiträgen eine Datei anhängen.
	 * 
	 */
	@Test
	public void createNewsDoc() throws Throwable{
		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);

		prepareFile( "testDocs/no_img.png", "/tmp/no_img.pmg" );
		Document doc = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(doc != null);

		// Create new message
		feedService.saveMessage("Header", "Test", user.getUserName(), doc, null, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);

		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(newsCommunity.getCommunityId()).get(0).getId();

		Message m = feedService.getMessageById(messageId);
		
		assertEquals(doc, m.getAttachment());
		
		feedService.removeMessage(m.getMessageId());
	}
	
	/*
	 * PSE2015-40	Als Portaladministrator kann ich den News-Beiträgen einen Link anhängen.
	 * 
	 */
	@Test
	public void createNewsLink() throws Throwable{
		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);
		
		String text = "http:\\www.google.at";
	
		// Create new message
		feedService.saveMessage("Header", text, user.getUserName(), null, null, communities, new ArrayList<MessageTag>(), new Date(), null);
		SleepUtil.sleep(1000);

		// Get Id of first (= newest) message of Message list for community
		int messageId = feedService.loadNews(newsCommunity.getCommunityId()).get(0).getId();

		Message m = feedService.getMessageById(messageId);
		assertEquals(text, m.getMessage());
		
		feedService.removeMessage(m.getMessageId());
	}
	
	private void createTestNews(String headline, String text) throws Throwable{
		Community newsCommunity = chatService.getCommunity(1);
		List<Community> communities = new ArrayList<Community>();
		communities.add(newsCommunity);

		prepareFile( "testDocs/no_img.png", "/tmp/no_img.pmg" );
		Document icon = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(icon != null);

		Document doc = documentService.store("pic", "/tmp/no_img.png");
		assertTrue(doc != null);
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH,  c.get(Calendar.DAY_OF_MONTH)-10);
		Date validFrom = c.getTime();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH,  c.get(Calendar.DAY_OF_MONTH)+10);
		Date validTo = c.getTime();
		
		// Create new message
		feedService.saveMessage(headline, text, user.getUserName(), doc, icon, communities, new ArrayList<MessageTag>(), validFrom, validTo);
		SleepUtil.sleep(1000);
	}
}
