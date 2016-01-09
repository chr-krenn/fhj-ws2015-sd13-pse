package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunityPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class CommunityPageIT extends SeleniumBaseTestCase{

	private CommunitiesPage communitiesPage;
	private LoginPage loginPage;
	private HomePage homepage;
	private CommunityPage communityPage;

	
	@Before
	public void loginBefore() {
		loginPage = new LoginPage(driver, BASE_URL);
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	
	@Test
	public void testCommunityPageTitle() {
		homepage = loginPage.login("pompenig13", "12345678");
		communitiesPage = homepage.getCommunitiesPage();
		communityPage = communitiesPage.getCommunityPage();
		verifyTextById("Community SWD","communityProfile");
	}

	/*
	* PSE2015-24: "Als Mitglied einer Community möchte ich auf der Seite der Community alle für die Community erfassten Activities sehen."
	*/
	
	@Test 
	public void isActivityStreamPresent(){
		homepage = loginPage.login("pompenig13", "12345678");
		communitiesPage = homepage.getCommunitiesPage();
		communityPage = communitiesPage.getCommunityPage();

		verifyTrue(communityPage.isActivitiesStreamPresent());

	}
	
	@Test
	public void areActivitiesPresent(){
		homepage = loginPage.login("pompenig13", "12345678");
		communitiesPage = homepage.getCommunitiesPage();
		communityPage = communitiesPage.getCommunityPage();
		int firstActivity = 1;
		int secondActivity = 2;
		String firstActivityAutor = "Albert Salzinger";
		String firstActivityTitle = "Message Headline";
		String firstActivityText = "Test message";
		String secondActivityAutor = "Florian Genser";
		String secondActivityTitle = "Message in SWD Community";
		String secondActivityText = "Community message__";
		
		verifyEquals(firstActivityAutor,communityPage.getAuthor(firstActivity));
		verifyEquals(firstActivityTitle,communityPage.getActivityTitle(firstActivity));
		verifyEquals(firstActivityText,communityPage.getActivityText(firstActivity));
		verifyEquals(secondActivityAutor,communityPage.getAuthor(secondActivity));
		verifyEquals(secondActivityTitle,communityPage.getActivityTitle(secondActivity));
		verifyEquals(secondActivityText,communityPage.getActivityText(secondActivity));

	}
	
	
}
