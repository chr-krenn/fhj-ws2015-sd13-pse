package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NewMessagePage;
import at.fhj.swd13.pse.test.gui.pageobjects.PrivateMessagesPage;

public class MessageIT extends SeleniumBaseTestCase {

	private static LoginPage loginPage;
	private static HomePage homepage;

	@BeforeClass
	public static void init() throws Exception {
		prepare();
		// Setting up private communities per user
		try (DbContext dbContext = contextProvider.getDbContext()) {
			ChatService chatService = new ChatServiceImpl(dbContext);
			chatService.createAllPrivateCommunities();
			dbContext.commit();
		}
		// Adding private message
		JDBC_HELPER.executeSqlScript("SQL/testdata_DBMessageTest.sql");
	}
	
	@Before
	public void loginBefore() {
		loginPage = new LoginPage(driver, BASE_URL);
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	/*
	 * PSE2015-60 "Beim Erfassen einer Nachricht kann ich Tags auswählen, um meine Nachricht zu klassifizieren."
	 */
	@Test
	public void testSetMessageTags() {
		homepage = loginPage.login("pompenig13", "12345678");
		NewMessagePage messagePage = homepage.openNewMessage();
		int result = messagePage.addKeyword("keyword");
		verifyEquals(1, result);
		verifyTrue(messagePage.getKeywords().contains("keyword"));
		verifyEquals(2, messagePage.addKeyword("soft"));
	}
	
	/*
	 * PSE2015-60 "Beim Erfassen einer Nachricht kann ich Tags auswählen, um meine Nachricht zu klassifizieren."
	 */
	@Test
	public void testSetExistingMessageTag() {
		homepage = loginPage.login("pompenig13", "12345678");
		NewMessagePage messagePage = homepage.openNewMessage();
		int result = messagePage.addKeyword("Soft");
		verifyEquals(1, result);
		verifyTrue(messagePage.getKeywords().contains("Software"));
	}
	
	/*
	 * PSE2015-48 "Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können."
	 */
	@Test
	public void testEnterBasicMessageData() {
		homepage = loginPage.login("zametter13", "12345678");
		NewMessagePage messagePage = homepage.openNewMessage();
		String title = "Title123456";
		String text = "Text";
		
		messagePage.enterBasicMessageData(title, text);
		
		String actualTitle = messagePage.getTitle();
		String actualText = messagePage.getText();
		
		verifyEquals(title, actualTitle);
		verifyEquals(text, actualText);
	}
	
	/*
	 * PSE2015-48 "Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können."
	 */
	@Test
	public void testSendMessage() {
		homepage = loginPage.login("zametter13", "12345678");
		NewMessagePage messagePage = homepage.openNewMessage();
		String title = "Title123456";
		String text = "Text";
		
		int numberOfCommunities = messagePage.addCommunity("@zametter");
		messagePage.sendBasicMessage(title, text);
		
		PrivateMessagesPage privateMessagesPage = homepage.getPrivateMessagePage();
		
		String actualText = privateMessagesPage.getLatestMessageText();
		String actualTitle = privateMessagesPage.getLatestMessageTitle();
		String actualAuthor = privateMessagesPage.getLastMessageAuthor();
		
		verifyEquals(title, actualTitle);
		verifyEquals(text, actualText);
		verifyEquals("Manuel Zametter", actualAuthor);
		verifyEquals(1, numberOfCommunities);
	}
}
