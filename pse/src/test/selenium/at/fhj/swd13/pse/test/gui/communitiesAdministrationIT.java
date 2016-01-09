package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.AdminPage;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class communitiesAdministrationIT extends SeleniumBaseTestCase{

	private static LoginPage loginPage;
	private static HomePage homePage;
	private static CommunitiesPage communitiesPage;
	private static AdminPage adminPage;
	private static String newCommunityName = "Not confirmed community";
	

	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
	}
	
	@After
	public void logoutAfter() {
		homePage.logout();
	}
	
	/*
	 * PSE2015-28: Als angemeldeter Admin möchte ich Communities freischalten können
	 */
	
	@Test
	public void activateRequestedCommunity(){
		homePage = loginPage.login("padmin", "12345678");
		adminPage = homePage.getAdministrationPage();
		String acceptButton = "Annehmen";
		String denyButton = "Ablehnen";
		verifyEquals(newCommunityName,adminPage.getFirstRequestedCommunityName());
		verifyEquals(acceptButton,adminPage.isAcceptCommunityButtonPresent());
		verifyEquals(denyButton,adminPage.isDenyCommunityButtonPresent());
		adminPage.clickCommunityAcceptButton();		
	}
	
	@Test
	public void verifyCommunityIsActivated(){
		int newCommunity = 4;
		homePage = loginPage.login("mitteregger13", "12345678");
		communitiesPage = homePage.getCommunitiesPage();
		verifyEquals(newCommunityName,communitiesPage.getCommunityName(newCommunity));
	}
	
	/*
	 * PSE2015-52: Als Community-Admin/Administrator kann ich Mitglieder für Communities freigeben.
	 */
	
	@Test
	public void acceptCommunitymembership(){
		int privateCommunity = 3;
		homePage = loginPage.login("mitteregger13", "12345678");
		communitiesPage = homePage.getCommunitiesPage();
		String expected = "Your request has been sent to the administrator";
		String isMember = "No";
		verifyTrue(expected.equals(communitiesPage.joinPrivateCommunity(2)));
		verifyTrue(isMember.equals(communitiesPage.isMemberOfCommunity(privateCommunity)));
		homePage.logout();
		homePage = loginPage.login("padmin", "12345678");
		adminPage = homePage.getAdministrationPage();
		String acceptButton = "Annehmen";
		String denyButton = "Ablehnen";
		verifyEquals("Private community",adminPage.getNameOfMembershipCommunityRequest());
		verifyEquals(acceptButton,adminPage.isAcceptMembershipButtonPresent());
		verifyEquals(denyButton,adminPage.isDenyMembershipButtonPresent());
		verifyEquals("mitteregger13",adminPage.getNameOfMembershipRequestor());
		adminPage.clickMembershipAcceptButton();		
	}
	
	@Test
	public void requestorIsNowMemberofCommunity(){
		int privateCommunity = 3;
		homePage = loginPage.login("mitteregger13", "12345678");
		communitiesPage = homePage.getCommunitiesPage();
		String isMember = "Yes";
		verifyTrue(isMember.equals(communitiesPage.isMemberOfCommunity(privateCommunity)));
	}
}
