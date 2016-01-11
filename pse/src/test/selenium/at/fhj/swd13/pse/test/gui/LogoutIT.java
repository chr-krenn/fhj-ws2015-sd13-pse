package at.fhj.swd13.pse.test.gui;

import org.junit.Before;
import org.junit.Test;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class LogoutIT extends SeleniumBaseTestCase {

	private LoginPage loginPage;
	private HomePage homepage;

	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("haringst13", "12345678");
	}

	/*
	 * PSE2015-30 "Als angemeldeter Benutzer des Systems möchte ich mich vom System abmelden können"
	 */
	@Test
	public void testLogout() {
		loginPage = homepage.logout();
		
		verifyTrue(loginPage.isEinloggenLinkPresent());

	}
}
