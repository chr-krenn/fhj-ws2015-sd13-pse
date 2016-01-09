package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunityPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NewCommunityMessagePage;

public class CommunityActivityStreamIT extends SeleniumBaseTestCase {
	
	private LoginPage loginPage;
	private static HomePage homepage;
	private static CommunitiesPage communitiespage;
	private static CommunityPage communitypage;
	private static NewCommunityMessagePage newcommunitymessagepage;

	@Before
	public void init() {	
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
		communitiespage = homepage.getCommunitiesPage();
		communitypage = communitiespage.getCommunityPage();
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	/*
	 * PSE2015-50: "Als angemeldeter User kann ich einen neuen Community-Beitrag in einer ausgewählten Community erstellen."
	 */
	@Test
	public void createNewActivity(){
		newcommunitymessagepage = communitypage.openNewMessage();
		String title = "Test Title";
		String text = "text ;-)";
		int firstActivity = 1;
		String messagesaved = "Ihre Nachricht wurde erfolgreich in SWD gepostet.";
		newcommunitymessagepage.sendBasicMessage(title, text);
		communitypage.getActivityText(firstActivity);
		verifyEquals(messagesaved,communitypage.messageWasSaved());
	}
	
	@Test
	public void verifyMessage(){
		String title = "Test Title";
		String text = "text ;-)";
		int firstActivity = 1;
		verifyEquals(title,communitypage.getActivityTitle(firstActivity));
		verifyEquals(text,communitypage.getActivityText(firstActivity));
	}

}
