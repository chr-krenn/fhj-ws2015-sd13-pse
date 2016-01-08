package at.fhj.swd13.pse.test.gui;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NewMessagePage;

public class CommunitiesPageIT extends SeleniumBaseTestCase {
	
	
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
	
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	/*
	 * PSE2015-30 "Als angemeldeter Benutzer habe ich auf der Übersichtsseite der Communities eine Liste aller öffentlichen und privaten Communities"
	 */
		
	@Test
	public void listCommunities(){
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
		CommunitiesPage communityPage = homepage.getCommunitiesPage();	
		verifyEquals(3,communityPage.getListedCommunitiesNumber());
	}
	
	/*
	 * PSE2015-30 "Auf der Community-Seite gibt es ein Suchfeld, wo man nach Community-Namen suchen kann."
	 * PSE2015-30 "Wird ein Community-Name erfolgreich gefunden, wird nur dieser in der Liste angezeigt."
	 */
	@Test
	public void testSearchForCommunities(){
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
		CommunitiesPage communityPage = homepage.getCommunitiesPage();
		String communitysearch = "SWD";
		communityPage.searchCommunities(communitysearch);
		verifyEquals(0,communityPage.getFoundCommunitiesNumber());
		verifyEquals(0,communityPage.getCommunityName(communitysearch));
	}
	
	
	@Test
	public void testCommunitySection() {
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
		CommunitiesPage communityPage = homepage.getCommunitiesPage();
		verifyTrue(communityPage.isCommunityListPresent());
	}
	
	@Test
	public void testSearchFunctionality(){
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
		CommunitiesPage communitiesPage = homepage.getCommunitiesPage();
		verifyTrue(communitiesPage.isSearchButtonPresent());
	}
	
	


	

}

	

