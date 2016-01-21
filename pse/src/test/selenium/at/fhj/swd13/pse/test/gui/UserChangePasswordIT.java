package at.fhj.swd13.pse.test.gui;

import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NotLoggedInPage;
import at.fhj.swd13.pse.test.util.SleepUtil;

public class UserChangePasswordIT extends SeleniumBaseTestCase {

	private  HomePage homepage = null;
	private  LoginPage loginPage = null;
	private  NotLoggedInPage notLoggedInPage = null;
	
	/**
	 * PSE2015-12: Als angemeldeter Benutzer des Systems möchte ich mein Passwort ändern können
	 */
	@Test
	public void testChangePasswordLoginWithNewPassword() {
		final String username = "krenn";
		final String oldPassword = "12345678";
		final String newPassword = "87654321";

		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login(username, oldPassword);

		homepage.changePassword(oldPassword, newPassword);
		homepage.logout();

		homepage = loginPage.login(username, newPassword);

		SleepUtil.sleep(1000);
		
		String loggedInUser = homepage.getLoggedInUser();
		
		verifyEquals(username, loggedInUser);
		
		homepage.logout();

	}
	@Test
	public void testChangePasswordLoginWithOldPassword() {
		
		final String username = "loeflerm13";
		final String oldPassword = "12345678";
		final String newPassword = "87654321";

		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login(username, oldPassword);

		homepage.changePassword(oldPassword, newPassword);
		homepage.logout();

		notLoggedInPage = loginPage.loginWithWrongCredentials(username, oldPassword);

		// forwarded to NotLoggedIn Page
		verifyTrue(notLoggedInPage.isDoorsOfDurinLabelPresent());
	}

}
