package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;

public class AdminPageIT  extends SeleniumBaseTestCase {
	private LoginPage loginPage;
	private HomePage homepage;
	
	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}

	/*
	 * PSE2015-22 "Als Admin User steht mir der Menüpunkt Administration zur Verfügung, um die Admin Seite aufzurufen."
	 */
	@Test(expected=NoSuchElementException.class)
	public void testAdminPageWithNormalUser() {
		homepage = loginPage.login("florian.genser", "12345678");
		homepage.getAdministrationPage();
	}
	
	/*
	 * PSE2015-22 "Als Admin User steht mir der Menüpunkt Administration zur Verfügung, um die Admin Seite aufzurufen."
	 */
	@Test
	public void testAdminPageWithAdminUser() {
		homepage = loginPage.login("padmin", "12345678");
		verifyTrue(homepage.getAdministrationPage() != null);
	}
}
