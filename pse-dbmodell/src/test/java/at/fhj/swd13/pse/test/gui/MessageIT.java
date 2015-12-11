package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NewMessagePage;

public class MessageIT extends SeleniumBaseTestCase {

	private static LoginPage loginPage;
	private static HomePage homepage;

	@Before
	public void init() {
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
}
