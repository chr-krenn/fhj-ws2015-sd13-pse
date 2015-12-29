package at.fhj.swd13.pse.test.gui;

import org.junit.Before;
import org.junit.Test;
import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NotLoggedInPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserPageAdminIT extends SeleniumBaseTestCase {
	
	private HomePage homepage = null;
	private LoginPage loginPage = null;
	private NotLoggedInPage notLoggedInPage = null;

	@Before
	public void init() {
		// login as admin
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("padmin", "12345678");
	}
	
	/**
	 * PSE2015-13: Als angemeldeter Admin möchte ich einen User aktiv/inaktiv setzen können
	 * set user active to false
	 */
	@Test
	public void testUserActive() {
		testLogin(false, true);
		
		// forwarded to NotLoggedIn Page
	}

	/**
	 * PSE2015-13: Als angemeldeter Admin möchte ich einen User aktiv/inaktiv setzen können
	 * set login allowed to false
	 */
	@Test
	public void testUserLoginAllowed() {
		testLogin(true, false);
		
		// forwarded to NotLoggedIn Page
	}
	
	/**
	 * PSE2015-13: Als angemeldeter Admin möchte ich einen User aktiv/inaktiv setzen können
	 * set login allowed to false and active to false
	 */
	@Test
	public void testUserActiveLoginAllowedFalse() {
		testLogin(false, false);
		
		// forwarded to NotLoggedIn Page
	}

	/**
	 * PSE2015-13: Als angemeldeter Admin möchte ich einen User aktiv/inaktiv setzen können
	 * set login allowed to false and active to false
	 */
	@Test
	public void testUserActiveLoginAllowedTrue() {
		testLogin(true, true);
		
		// forwarded to HomePage
		homepage.logout();
	}
	
	private void testLogin(boolean active, boolean loginAllowed) {
		// search for user
		UserList userList = homepage.searchUser("Angelo");
		verifyEquals(1, userList.getUsers().size());
		
		// open user page
		UserPage userPage = userList.openUserPage(0);
		verifyEquals("Angelo", userPage.getUserLastName());

		// set inactive and save
		userPage.setActive(active);
		userPage.setLoginAllowed(loginAllowed);
		userPage.save();
		
		// user must not be able to login
		homepage.logout();
		if((!active) || (!loginAllowed))
		{
			//forwarded to NotLoggedIn Page
			notLoggedInPage = loginPage.loginWithWrongCredentials("angelofr13", "12345678");
			
			verifyTrue(notLoggedInPage.isDoorsOfDurinLabelPresent());
		}
		else
		{
			homepage = loginPage.login("angelofr13", "12345678");
			verifyTrue(homepage.isActivitiesStreamPresent());
		}
	}
}
