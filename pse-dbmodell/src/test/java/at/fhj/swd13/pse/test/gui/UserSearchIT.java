package at.fhj.swd13.pse.test.gui;

import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserSearchIT extends SeleniumBaseTestCase {
	
	/**
	 * PSE2015-9: Als angemeldeter Benutzer des Systems möchte ich nach anderen Benutzern des Systems suchen können
	 */
	@Test
	public void testSearchUser() {

		LoginPage loginPage = new LoginPage(driver, BASE_URL);
		HomePage homepage = loginPage.login("florian.genser", "12345678");
		
		UserList userList = homepage.searchUser("ei");
		verifyEquals(2, userList.getUsers().size());
		
		UserPage userPage = userList.openUserPage(1);
		verifyEquals("Teiniker", userPage.getUserLastName());
	}
}
