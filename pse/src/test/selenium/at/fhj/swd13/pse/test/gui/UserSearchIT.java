package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserSearchIT extends SeleniumBaseTestCase {
	private HomePage homepage;
	private LoginPage loginPage;

	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("florian.genser", "12345678");
	}
	
	@After
	public void tearDown() throws Exception {
		homepage.logout();
		super.tearDown();
	}
	
	/**
	 * PSE2015-9: Als angemeldeter Benutzer des Systems möchte ich nach anderen Benutzern des Systems suchen können
	 */
	@Test
	public void testSearchUser() {
		UserList userList = homepage.searchUser("ei");
		verifyEquals(2, userList.getUsers().size());
		
		UserPage userPage = userList.openUserPage(1);
		verifyEquals("Teiniker", userPage.getUserLastName());
	}


	/**
	 * PSE2015-10: Als angemeldeter Benutzer des Systems möchte ich die Profile anderer Benutzer des Systems anschauen können
	 */
	@Test
	public void testViewUser() {
		UserList userList = homepage.searchUser("Angelo");
		verifyEquals(1, userList.getUsers().size());
		
		UserPage userPage = userList.openUserPage(0);
		verifyEquals("Angelo", userPage.getUserLastName());
		verifyEquals("Franz", userPage.getUserFirstName());
		verifyEquals("franz.angelo@edu.fh-joanneum.at", userPage.getUserEmailAddress());
		verifyEquals("Team 2", userPage.getUserDepartment());
		verifyEquals("A", userPage.getUserBuilding());
		verifyEquals("-2", userPage.getUserFloor());
		verifyEquals("666", userPage.getUserRoomNumber());
		verifyEquals("+436767067828", userPage.getUserPhoneNumberMobile());
	}
}
