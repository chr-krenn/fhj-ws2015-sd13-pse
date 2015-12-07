package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;

public class NavigationIT  extends SeleniumBaseTestCase {
	
	private LoginPage loginPage;
	private HomePage homepage;
	
	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("florian.genser", "12345678");
	}
	
	@After
	public void logoutAfter() {
		loginPage.logout();
	}
	
	/*
	 * PSE2015-23 "Als angemeldeter Benutzer möchte ich über den Header-Bereich direkt auf meine Userseite zugreifen können."
	 */
	@Test
	public void testUsernameInHeader() throws Exception {
		verifyTextById("florian.genser", "j_idt8:j_idt15_button");
	}
	
	/*
	 * PSE2015-23 "Als angemeldeter Benutzer möchte ich über den Header-Bereich direkt auf meine Userseite zugreifen können."
	 */
	@Test
	public void testLinkToUserPage() throws Exception {
		UserPage userpage = homepage.getReadOnlyUserProfilePage();
		verifyEquals("Genser",userpage.getUserLastName());
	}

}
