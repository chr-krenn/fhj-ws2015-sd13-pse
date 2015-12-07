package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
<<<<<<< HEAD
import at.fhj.swd13.pse.test.gui.pageobjects.MessageDetailView;
=======
>>>>>>> 71fe058301c11efedcedbe27087d72c9b3c4afa6
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;


public class ActivityStreamIT extends SeleniumBaseTestCase {
	
	private LoginPage loginPage;
	private static HomePage homepage;

	@Before
	public void init() {
		loginPage = new LoginPage(driver, BASE_URL);
<<<<<<< HEAD
		homepage = loginPage.login("pompenig13", "12345678");
=======
		homepage = loginPage.login("florian.genser", "12345678");
>>>>>>> 71fe058301c11efedcedbe27087d72c9b3c4afa6
	}
	
	@After
	public void logoutAfter() {
		loginPage.logout();
	}
	
	@Test
	public void testActivitySection() {
		verifyTrue(homepage.isActivitiesStreamPresent());
	}
	
	/*
	 * PSE2015-66: "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream 
	 * auf meiner Startseite die Details der Activity ansehen können"
	 */
	@Test
	public void testActivitySectionDetailsButton() {
		verifyTrue(homepage.isActivityDetailsButtonPresent());
	}
	
	/*
	 * PSE2015-66: "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream 
	 * auf meiner Startseite die Details der Activity ansehen können"
	 */
	@Test
	public void testActivityDetailView() {
		verifyTrue(homepage.isActivityStreamDetailViewCorrect());
	}
	
	/*
	 * Test only works with correct test data (see testdata.sql)
	 * 
	 * PSE2015-66: "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream 
	 * auf meiner Startseite die Details der Activity ansehen können"
	 * - Im Activity Stream auf der Startseite sehe ich die Anzahl der Kommentare
	 */
	@Test
	public void testNumberOfComments() {
		int messageWithComments = homepage.getMessageNumberByHeadline("Message Headline");
		int messageWithoutComments = homepage.getMessageNumberByHeadline("New software");
		verifyEquals(3, homepage.getNumberOfComments(messageWithComments));
		verifyEquals(0, homepage.getNumberOfComments(messageWithoutComments));
	}
	
	/*
	 * Test only works with correct test data (see testdata.sql)
	 * 
	 * PSE2015-66: "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream 
	 * auf meiner Startseite die Details der Activity ansehen können"
	 * - Im Activity Stream auf der Startseite sehe ich die Anzahl der Likes
	 */
	@Test
	public void testNumberOfLikes() {
		int messageWithLikes = homepage.getMessageNumberByHeadline("Message Headline");
		int messageWithoutLikes = homepage.getMessageNumberByHeadline("New software");
		verifyEquals(2, homepage.getNumberOfLikes(messageWithLikes));
		verifyEquals(0, homepage.getNumberOfLikes(messageWithoutLikes));
	}
	
	/*
	 * Test only works with correct test data (see testdata.sql)
	 */
	@Test
	public void testNumberOfLikesDetailView() {
		int messageWithLikes = homepage.getMessageNumberByHeadline("Message Headline");
		MessageDetailView detailView = homepage.openDetailView(messageWithLikes);
		verifyEquals(2, detailView.getNumberOfLikes());
	}
	
	@Test
	public void testLike() {
		Boolean revert = false;
		
		int expected = homepage.getNumberOfLikes(0);
		if(homepage.likeMessage(0)) {
			expected += 1;
			revert = true;
		}
		verifyEquals(expected, homepage.getNumberOfLikes(0));
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		if(revert) {
			homepage.clickLikeButton(0);
		}
	}
	
	@Test
	public void testLikeDetailView() {
		Boolean revert = false;
		
		MessageDetailView detailView = homepage.openDetailView();
		int expected = detailView.getNumberOfLikes();
		if(detailView.likeMessage()) {
			expected += 1;
			revert = true;
		}
		verifyEquals(expected, detailView.getNumberOfLikes());
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		if(revert) {
			detailView.clickLikeButton();
		}
	}
	
	@Test
	public void testRevertLike() {
		Boolean revert = false;
		
		int expected = homepage.getNumberOfLikes(0);
		if(homepage.revertLike(0)) {
			expected -= 1;
			revert = true;
		}
		verifyEquals(expected, homepage.getNumberOfLikes(0));
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		if(revert) {
			homepage.clickLikeButton(0);
		}
	}
	
	@Test
	public void testRevertLikeDetailView() {
		Boolean revert = false;
		
		MessageDetailView detailView = homepage.openDetailView();
		int expected = detailView.getNumberOfLikes();
		if(detailView.revertLike()) {
			expected -= 1;
			revert = true;
		}
		verifyEquals(expected, detailView.getNumberOfLikes());
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		if(revert) {
			detailView.clickLikeButton();
		}
	}
	
	@Test
	public void testLikeButton() {
		int expected = homepage.getNumberOfLikes(0);
		int change = homepage.clickLikeButton(0);
		verifyEquals(expected+change,homepage.getNumberOfLikes(0));
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		homepage.clickLikeButton(0);
	}
	
	@Test
	public void testLikeButtonDetailView() {
		MessageDetailView detailView = homepage.openDetailView();
		int expected = detailView.getNumberOfLikes();
		int change = detailView.clickLikeButton();
		verifyEquals(expected+change,detailView.getNumberOfLikes());
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		detailView.clickLikeButton();
	}
	
	/*
	 * PSE2015-66: "Als angemeldeter Benutzer möchte ich ausgehend vom Activity Stream 
	 * auf meiner Startseite die Details der Activity ansehen können"
	 */
	@Test
	public void testGetUsersLikingMessage() {
		homepage.likeMessage(0);
		verifyTrue(homepage.getUsersLikingMessage(0).contains("Christine Pompenig"));
	}

	@Test
	public void testGetUsersLikingMessageDetailView() {
		MessageDetailView detailView = homepage.openDetailView();
		detailView.likeMessage();
		verifyTrue(detailView.getUsersLikingMessage().contains("Christine Pompenig"));
	}
}
