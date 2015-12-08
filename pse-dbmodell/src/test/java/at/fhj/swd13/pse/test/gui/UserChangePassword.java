package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class UserChangePassword extends SeleniumBaseTestCase {

	private static HomePage homepage = null;

	/**
	 * PSE2015-12: Als angemeldeter Benutzer des Systems möchte ich mein Passwort ändern können
	 */
	@Test
	public void testChangePasswordLoginWithNewPassword() {
		LoginPage loginPage;
		final String username = "angelofr13";
		final String oldPassword = "12345678";
		final String newPassword = "87654321";

		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login(username, oldPassword);

		homepage.changePassword(oldPassword, newPassword);
		homepage.logout();

		homepage = loginPage.login(username, newPassword);

		verifyEquals(homepage.getLoggedInUser(), username);

	}
/* not working so far
	@Test
	public void testChangePasswordLoginWithOldPassword() {
		HomePage homepage;
		NotLoggedInPage notLoggedInPage;
		final String username = "loeflerm13";
		final String oldPassword = "12345678";
		final String newPassword = "87654321";

		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login(username, oldPassword);

		homepage.changePassword(oldPassword, newPassword);
		loginPage = loginPage.logout();

		notLoggedInPage = loginPage.loginInWrongPassword(username, oldPassword);

		verifyTrue(notLoggedInPage.pagePresent());

	}*/

	@After
	public void logoutAfter() {
		if (homepage != null)
			homepage.logout();
	}

}
