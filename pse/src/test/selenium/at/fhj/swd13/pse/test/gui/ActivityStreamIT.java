package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.MessageDetailView;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;


public class ActivityStreamIT extends SeleniumBaseTestCase {
	
	private LoginPage loginPage;
	private static HomePage homepage;

	@Before
	public void init() {	
		// resset Database for each testcase here
		prepare();
		loginPage = new LoginPage(driver, BASE_URL);
		homepage = loginPage.login("pompenig13", "12345678");
	}
	
	@After
	public void logoutAfter() {
		homepage.logout();
	}
	
	@Test
	public void testActivitySection() {
		verifyTrue(homepage.isActivitiesStreamPresent());
	}
	
	/*
	 * PSE2015-19: Funktionalität über andere Tests geprüft, zugehörige GUI mit Tests für PSE2015-66 getestet
	 */
	
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
	
	/*
	 * PSE2015-25 "Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde."
	 */
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
	
	/*
	 * PSE2015-25 "Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde."
	 */
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
	
	/*
	 * PSE2015-25 "Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde."
	 */
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
	
	/*
	 * PSE2015-25 "Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde."
	 */
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
	
	/*
	 * PSE2015-25 "Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde."
	 */
	@Test
	public void testLikeButton() {
		int expected = homepage.getNumberOfLikes(0);
		int change = homepage.clickLikeButton(0);
		verifyEquals(expected+change,homepage.getNumberOfLikes(0));
		
		//Clicking like button a second time to ensure the test data is the same as before the test
		homepage.clickLikeButton(0);
	}
	
	/*
	 * PSE2015-25 "Als angemeldeter Benutzer möchte ich eine Activity "liken" können, um zu zeigen, dass ich diese gut finde."
	 */
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
	 * PSE2015-26 "Als angemeldeter Benutzer möchte ich sehen können, wer eine Activity geliked hat."
	 */
	@Test
	public void testGetUsersLikingMessage() {
		homepage.likeMessage(0);
		verifyTrue(homepage.getUsersLikingMessage(0).contains("Christine Pompenig"));
	}

	/*
	 * PSE2015-26 "Als angemeldeter Benutzer möchte ich sehen können, wer eine Activity geliked hat."
	 */
	@Test
	public void testGetUsersLikingMessageDetailView() {
		MessageDetailView detailView = homepage.openDetailView();
		detailView.likeMessage();
		verifyTrue(detailView.getUsersLikingMessage().contains("Christine Pompenig"));
	}
	
	/* 
	 * PSE2015-27 "Als angemeldeter Benutzer möchte ich die Kommentare zu einer Activity lesen können."
	 */
	@Test
	public void testNumberOfCommentsDisplayedInDetailView() {
		int numberOfComments = homepage.getNumberOfComments(0);
		MessageDetailView detailView = homepage.openDetailView(0);
		verifyEquals(numberOfComments, detailView.getNumberOfCommentsInHeader());
		verifyEquals(detailView.getNumberOfCommentsInHeader(), detailView.getNumberOfDisplayedComments());
	}
	
	/*
	 * Test only works with correct test data (see testdata.sql)
	 * 
	 * PSE2015-27 "Als angemeldeter Benutzer möchte ich die Kommentare zu einer Activity lesen können."
	 */
	@Test
	public void testCommentsDisplay() {
		int messageWithComments = homepage.getMessageNumberByHeadline("Message Headline");
		MessageDetailView detailView = homepage.openDetailView(messageWithComments);
		verifyTrue(detailView.getCommentsText().contains("Comment 3"));
	}
}
