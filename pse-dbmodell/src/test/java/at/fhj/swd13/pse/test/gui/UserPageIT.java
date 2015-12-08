package at.fhj.swd13.pse.test.gui;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserPageIT extends SeleniumBaseTestCase {
	
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
	
	@Test
	public void testSetOutOfOffice() {
		homepage = loginPage.login("florian.genser", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		userPage.setOutOfOffice(true);
		verifyTrue(userPage.getOutOfOffice());
	}
	
	/*
	 * Test only works with correct test data (see testdata.sql)
	 */
	@Test
	public void testNumberOfContacts() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		verifyEquals(2, userPage.getNumberOfContacts());
	}
	
	/*
	 * Test only works with correct test data (see testdata.sql)
	 */
	@Test
	public void testOpenContactProfile() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		UserPage contactPage = userPage.openContactProfile(2);
		verifyEquals("Löfler", contactPage.getUserLastName());
	}
	
	
}
