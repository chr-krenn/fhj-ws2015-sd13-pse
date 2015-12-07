package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;


public class ActivityStreamIT extends SeleniumBaseTestCase {
	
	private LoginPage loginPage;
	private static HomePage homepage;

	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("florian.genser", "12345678");
	}
	
	@After
	public void logoutAfter() {
		loginPage.logout();
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
	
	@Test
	public void testLike() {
		int expected = homepage.getNumberOfLikes(0);
		if(homepage.likeMessage(0)) {
			expected += 1;
		}
		verifyEquals(expected, homepage.getNumberOfLikes(0));
	}
	
	@Test
	public void testRevertLike() {
		int expected = homepage.getNumberOfLikes(0);
		if(homepage.revertLike(0)) {
			expected -= 1;
		}
		verifyEquals(expected, homepage.getNumberOfLikes(0));
	}
	
	@Test
	public void testLikeButton() {
		int expected = homepage.getNumberOfLikes(0);
		int change = homepage.clickLikeButton(0);
		verifyEquals(expected+change,homepage.getNumberOfLikes(0));
	}
	
	@Test
	public void testGetUsersLikingMessage() {
		homepage.likeMessage(0);
		verifyTrue(homepage.getUsersLikingMessage(0).contains("Florian Genser"));
	}
}
