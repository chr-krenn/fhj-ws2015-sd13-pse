package at.fhj.swd13.pse.test.gui;

import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserSearchIT extends SeleniumBaseTestCase {
	
	@Test
	/*
	 * PSE2015-9: Als angemeldeter Benutzer des Systems möchte ich nach anderen Benutzern des Systems suchen können
	 */
	public void testSearchUser() {
		login("florian.genser", "12345678");
		HomePage homepage = new HomePage(driver);

		UserList userList = homepage.searchUser("ei");
		verifyEquals(2, userList.getUsers().size());
		
		UserPage userPage = userList.openUserPage(1);
		verifyEquals("Teiniker", userPage.getUserLastName());
	}
}
