package at.fhj.swd13.pse.test.gui;

import org.junit.Test;
import org.openqa.selenium.By;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class UserChangePasswordIT extends SeleniumBaseTestCase {

	private  static HomePage homepage = null;
	private  static LoginPage loginPage = null;
	
	/**
	 * PSE2015-12: Als angemeldeter Benutzer des Systems möchte ich mein Passwort ändern können
	 */
	@Test
	public void testChangePasswordLoginWithNewPassword() {
		final String username = "angelofr13";
		final String oldPassword = "12345678";
		final String newPassword = "87654321";

		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login(username, oldPassword);

		homepage.changePassword(oldPassword, newPassword);
		homepage.logout();

		homepage = loginPage.login(username, newPassword);

		verifyEquals(homepage.getLoggedInUser(), username);
		
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

		loginPage.login(username, oldPassword);

		// forwarded to NotLoggedIn Page
		verifyEquals(1, driver.findElements(By.linkText("Doors of Durin")).size());
	}

}
