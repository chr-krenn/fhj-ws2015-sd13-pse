package at.fhj.swd13.pse.test.gui;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.HomePage;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NewMessagePage;
import at.fhj.swd13.pse.test.gui.pageobjects.UserList;
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
	 * 
	 * PSE2015-46 "Als angemeldeter Benutzer des System möchte ich einen anderen Benutzer als Kontakt hinzufügen können"
	 */
	@Test
	public void testAddToContact() {
		homepage = loginPage.login("haringst13", "12345678");
				
		UserList userList = homepage.searchUser("Angelo");
		UserPage userPage = userList.openUserPage(0);
		userPage.openContactsTab();
		verifyEquals(userPage.getNumberOfContacts(), 0);
		userPage.addToContact();
		userPage.openContactsTab();
		verifyEquals(userPage.getNumberOfContacts(), 1);
	}
	
	/*
	 * Test only works with correct test data 
	 * 
	 * PSE2015-51 "Als angemeldeter Benutzer möchte ich die Benutzer die in der selben Abteilung sind, angezeigt bekommen"
	 */
	@Test
	public void testUsersWithDepartment() {
		homepage = loginPage.login("haringst13", "12345678");
		
		UserPage userPage = homepage.getUserProfilePage();
		verifyEquals(4, userPage.getNumberOfUsersWithDepartment());
		
		List<String> names = userPage.getNamesOfUsersWithDepartment();
		verifyTrue(names.contains("Haring Stefan"));
		verifyTrue(names.contains("Angelo Franz"));
		verifyTrue(names.contains("Oswald Gerald"));
		verifyTrue(names.contains("Schmidt Roman"));
	}
	
	/*
	 * Test only works with correct test data 
	 * 
	 * PSE2015-49 "Als angemeldeter Benutzer möchte ich die Benutzer, die ich als Kontakt hinzugefügt habe, angezeigt bekommen"
	 */
	@Test
	public void testContacts() {
		homepage = loginPage.login("pompenig13", "12345678");
				
		UserPage userPage = homepage.getUserProfilePage();
		userPage.openContactsTab();
		verifyEquals(userPage.getNumberOfContacts(), 2);
		
		List<String> names = userPage.getNamesOfContacts();
		verifyTrue(names.contains("Löfler Mario"));
		verifyTrue(names.contains("Genser Florian"));		
	}


	/*
	 * Test only works with correct test data (see testdata.sql)
	 */
	@Test
	public void testOpenContactProfile() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		UserPage contactPage = userPage.openContactProfile(2);
		verifyEquals("mario.loefler@edu.fh-joanneum.at", contactPage.getUserEmailAddress());
	}

	/*
	 * Test only works with correct test data (see testdata.sql)
	 * 
	 * PSE2015-48 "Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können"
	 */
	@Test(expected = NoSuchElementException.class)
	public void testSendMessageButtonNotPresent() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		userPage.getSendMessageButton();
	}

	/*
	 * Test only works with correct test data (see testdata.sql)
	 * 
	 * PSE2015-48 "Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können"
	 */
	@Test
	public void testSendMessageButtonPresent() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		UserPage contactPage = userPage.openContactProfile(2);
		verifyTrue(contactPage.getSendMessageButton() != null);
	}

	/*
	 * Test only works with correct test data (see testdata.sql)
	 * 
	 * PSE2015-48 "Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können"
	 */
	@Test
	public void testSendMessageButton() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		UserPage contactPage = userPage.openContactProfile(1);
		NewMessagePage messagePage = contactPage.clickSendMessageButton();
		verifyTrue(messagePage.getHeader().startsWith("Neue Nachricht eingeben für"));
	}

	/*
	 * PSE2015-48 "Als angemeldeter Benutzer des System möchte ich einem meiner Kontakte eine private Nachricht schicken können"
	 */
	@Ignore // FIXME: fix sendBasicMessage
	@Test
	public void testSendMessageToContact() {
		homepage = loginPage.login("pompenig13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		UserPage contactPage = userPage.openContactProfile(1);
		NewMessagePage messagePage = contactPage.clickSendMessageButton();
		messagePage.sendBasicMessage("Title 123", "Text");
		verifyEquals("Nachricht gesendet", contactPage.getConfirmationForSendingMessage());
	}

	/*
	 * PSE2015-29 "Als angemeldeter User möchte ich per Klick auf das Startseitemenuitem des angemeldeten Users im Header auf die Userseite kommen"
	 */
	@Test
	public void testLinkToReadOnlyUserPage() {
		homepage = loginPage.login("haringst13", "12345678");
		UserPage userPage = homepage.getReadOnlyUserProfilePage();
		verifyEquals("Haring", userPage.getUserLastName());
		verifyEquals("Stefan", userPage.getUserFirstName());
		verifyEquals("stefan.haring@edu.fh-joanneum.at", userPage.getUserEmailAddress());
		verifyEquals("Team 2", userPage.getUserDepartment());
		verifyEquals("A", userPage.getUserBuilding());
		verifyEquals("-2", userPage.getUserFloor());
		verifyEquals("666", userPage.getUserRoomNumber());
		verifyEquals("+436644711815", userPage.getUserPhoneNumberMobile());

	}
	/*
	 * PSE2015-29 "Als angemeldeter User möchte ich per Klick auf das Startseitemenuitem des angemeldeten Users im Header auf die Userseite kommen"
	 */
	@Test
	public void testLinkToUserPage() {
		homepage = loginPage.login("haringst13", "12345678");
		UserPage userPage = homepage.getUserProfilePage();
		verifyEquals("Haring", userPage.getUserLastName());
		verifyEquals("Stefan", userPage.getUserFirstName());
		verifyEquals("stefan.haring@edu.fh-joanneum.at", userPage.getUserEmailAddress());
		verifyEquals("Team 2", userPage.getUserDepartment());
		verifyEquals("A", userPage.getUserBuilding());
		verifyEquals("-2", userPage.getUserFloor());
		verifyEquals("666", userPage.getUserRoomNumber());
		verifyEquals("+436644711815", userPage.getUserPhoneNumberMobile());

	}
}
