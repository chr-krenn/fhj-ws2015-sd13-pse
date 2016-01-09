package at.fhj.swd13.pse.test.gui;


import org.junit.BeforeClass;
import org.junit.Test;


import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.util.SleepUtil;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;


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
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
	}
	
	/*
	 * PSE2015-30 "Als angemeldeter Benutzer habe ich auf der Übersichtsseite der Communities eine Liste aller öffentlichen und privaten Communities"
	 */
	@Test
	public void listCommunities(){
		CommunitiesPage communitiesPage = homepage.getCommunitiesPage();	
		verifyEquals(3,communitiesPage.getListedCommunitiesNumber());
	}
	
	/*
	 * PSE2015-30 "Auf der Community-Seite gibt es ein Suchfeld, wo man nach Community-Namen suchen kann."
	 * PSE2015-30 "Wird ein Community-Name erfolgreich gefunden, wird nur dieser in der Liste angezeigt."
	 */
	@Test
	public void testSearchForCommunities(){
		CommunitiesPage communitiesPage = homepage.getCommunitiesPage();
		String communitySearch = "SWD";
		communitiesPage.searchCommunities(communitySearch);
		SleepUtil.sleep(3000);
		verifyEquals(1,communitiesPage.getFoundCommunitiesNumber());
		verifyEquals(1,communitiesPage.communityNameEquals(communitySearch));
	}
	
	@Test
	public void testCommunitySection() {
		CommunitiesPage communitiesPage = homepage.getCommunitiesPage();
		verifyTrue(communitiesPage.isCommunityListPresent());
	}
	
	@Test
	public void testSearchFunctionality(){
		CommunitiesPage communitiesPage = homepage.getCommunitiesPage();
		verifyTrue(communitiesPage.isSearchButtonPresent());
	}
	
}

	

