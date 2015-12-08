package at.fhj.swd13.pse.test.gui;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserPageAdminIT extends SeleniumBaseTestCase {
	
	private static HomePage homepage;

	@Before
	public void init() {
		LoginPage loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("padmin", "12345678");
	}
	
	/**
	 * PSE2015-13: Als angemeldeter Admin möchte ich einen User aktiv/inaktiv setzen können
	 */
	@Test
	public void testSetUserInactive() {
		// search for user
		UserList userList = homepage.searchUser("Angelo");
		verifyEquals(1, userList.getUsers().size());
		
		// open user page
		UserPage userPage = userList.openUserPage(0);
		verifyEquals("Angelo", userPage.getUserLastName());

		// set inactive and save
		userPage.setActive(false);
		verifyFalse(userPage.getActive());
	}
}
