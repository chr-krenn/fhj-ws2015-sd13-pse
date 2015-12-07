package at.fhj.swd13.pse.test.gui;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;


public class UserPageIT extends SeleniumBaseTestCase {
	
	private static HomePage homepage;

	@Before
	public void init() {
		LoginPage loginPage = new LoginPage(driver, baseUrl);
//		login("florian.genser", "12345678");
		homepage = loginPage.login("florian.genser", "12345678");
	}
	
	@Test
	public void testSetOutOfOffice() {
		UserPage userPage = homepage.getUserProfilePage();
		userPage.setOutOfOffice(true);
		verifyTrue(userPage.getOutOfOffice());
	}
}
