package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.CommunitiesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.ServicesPage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
import at.fhj.swd13.pse.test.gui.pageobjects.UserPage;

public class NavigationIT  extends SeleniumBaseTestCase {
	
	private LoginPage loginPage;
	private HomePage homepage;
	
	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("florian.genser", "12345678");
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	/*
	 * PSE2015-23 "Als angemeldeter Benutzer möchte ich über den Header-Bereich direkt auf meine Userseite zugreifen können."
	 */
	@Test
	public void testUsernameInHeader() throws Exception {
		verifyText("florian.genser", By.cssSelector("button[id='j_idt8:j_idt15_button'] span"));
	}
	
	/*
	 * PSE2015-23 "Als angemeldeter Benutzer möchte ich über den Header-Bereich direkt auf meine Userseite zugreifen können."
	 */
	@Test
	public void testLinkToUserPage() {
		UserPage userpage = homepage.getReadOnlyUserProfilePage();
		verifyEquals("Genser",userpage.getUserLastName());
	}
	
	/*
	 * PSE2015-21 "Als angemeldeter Benutzer möchte ich über eine Menüleiste zwischen den einzelnen Seiten navigieren können."
	 */
	@Test
	public void testOpenCommunitiesPage() {
		CommunitiesPage communitiesPage = homepage.getCommunitiesPage();
		verifyEquals("Communities", communitiesPage.getTitle());
	}
	
	/*
	 * PSE2015-21 "Als angemeldeter Benutzer möchte ich über eine Menüleiste zwischen den einzelnen Seiten navigieren können."
	 */
	@Test
	public void testOpenEmployeesPage() {
		UserList employeesPage = homepage.getEmployeesPage();
		verifyEquals("Liste aller Benutzer", employeesPage.getTitle());
	}
	
	/*
	 * PSE2015-21 "Als angemeldeter Benutzer möchte ich über eine Menüleiste zwischen den einzelnen Seiten navigieren können."
	 */
	@Test
	public void testOpenServicesPage() {
		ServicesPage servicesPage = homepage.getServicesPage();
		verifyEquals("Es sind noch keine Services verfügbar.", servicesPage.getContent());
	}
	
	/*
	 * PSE2015-21 "Als angemeldeter Benutzer möchte ich über eine Menüleiste zwischen den einzelnen Seiten navigieren können."
	 */
	@Test
	public void testOpenHomePage() {
		ServicesPage servicesPage = homepage.getServicesPage();
		HomePage homepage2 = servicesPage.openHomePage();
		verifyTrue(homepage2.isActivitiesStreamPresent());
	}

}
