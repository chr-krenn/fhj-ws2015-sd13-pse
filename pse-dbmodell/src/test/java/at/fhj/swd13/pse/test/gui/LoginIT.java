package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class LoginIT extends SeleniumBaseTestCase {

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
	 * PSE2015-7 "Als Benutzer des Systems möchte ich mich mit meinem Usernamen und meinem Passwort am System anmelden können"
	 */
	@Test
	public void testLogin() {
		homepage = loginPage.login("haringst13", "12345678");
		
		verifyTrue(homepage.isActivitiesStreamPresent());

	}
}
