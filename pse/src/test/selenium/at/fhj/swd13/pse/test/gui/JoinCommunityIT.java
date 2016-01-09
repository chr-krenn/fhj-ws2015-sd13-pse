package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class JoinCommunityIT extends SeleniumBaseTestCase{
	
	private static LoginPage loginPage;
	private static HomePage homepage;
	private static CommunitiesPage communityPage;

	@Before
	public void init() throws Exception {
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
		communityPage = homepage.getCommunitiesPage();	
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	@Test
	public void isCommunityListedPrivate(){
		verifyEquals(0,communityPage.isCommunityPrivate(1));
		verifyEquals(0,communityPage.isCommunityPrivate(2));
		verifyEquals(1,communityPage.isCommunityPrivate(3));
	}
	
	/*
	 * PSE2015-55 "Als angemeldeter Benutzer kann ich einer öffentlichen Community beitreten oder Mitgliedschaft bei einer privaten Community beantragen."
	 */
	@Test
	public void testJoinPrivateCommunity(){
		String expected = "Your request has been sent to the administrator";
		String isMember = "No";
		verifyTrue(expected.equals(communityPage.joinPrivateCommunity(2)));
		verifyTrue(isMember.equals(communityPage.isMemberOfCommunity(2)));		
	}
	
	@Test
	public void testJoinPublicCommunity(){
		String expected = "You are now a member of the community 'Public community'";
		String isMember = "Yes";
		verifyTrue(expected.equals(communityPage.joinPublicCommunity(1)));
		verifyTrue(isMember.equals(communityPage.isMemberOfCommunity(1)));
	}

}
