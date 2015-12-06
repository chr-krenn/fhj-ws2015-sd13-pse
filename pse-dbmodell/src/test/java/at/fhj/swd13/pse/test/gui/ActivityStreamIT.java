package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;


public class ActivityStreamIT extends SeleniumBaseTestCase {
	
	private static HomePage homepage;

	@Before
	public void init() {
		login("florian.genser", "12345678");
		homepage = new HomePage(driver);
	}
	
	@After
	public void tearDown() {
		logout();
	}
	
	@Test
	public void testActivitySection() {
		verifyTrue(homepage.isActivitiesStreamPresent());
	}
	
	@Test
	public void testActivitySectionDetailsButton() {
		verifyTrue(homepage.isActivityDetailsButtonPresent());
	}
	
	@Test
	public void testActivityDetailView() {
		verifyTrue(homepage.isActivityStreamDetailViewCorrect());
	}
}
