package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.document.DocumentServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class UserServiceIT extends RemoteTestBase {

	private UserService userService;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
        userService = lookup(UserServiceFacade.class, UserService.class);
		userService.updateNullPasswords();
    }	
	
    /*
     * PSE2015-7 Als Benutzer des Systems möchte ich mich mit meinem Usernamen und meinem Passwort am System anmelden können
     */
    @Test
    public void login() {
		assertNotNull(userService.loginUser("angelofr13", "12345678", UserSession.createSessionId()));
    }
    
    /*
     * PSE2015-8 Als Benutzer des Systems möchte ich eine Möglichkeit haben mein Passwort zurücksetzen zu lassen wenn ich es vergessen habe
     */
    @Test
    public void resetPassword() {
    	String randomPasswort = userService.resetPassword("franz.angelo@edu.fh-joanneum.at", "localhost", 8080);
    	assertTrue(userService.isMatchingPassword("angelofr13", randomPasswort));
    }

    /*
     * PSE2015-9 Als angemeldeter Benutzer des Systems möchte ich nach anderen Benutzern des Systems suchen können
     */
	@Test
	public void findUsers() {
		List<Person> persons = userService.findUsers("ei");
		assertEquals(2, persons.size());
	}
	
	/*
	 * 	PSE2015-10 Als angemeldeter Benutzer des Systems möchte ich die Profile anderer Benutzer des Systems anschauen können
	 */
	@Test
	public void viewProfile() {
		List<Person> persons = userService.findUsers("integrationtestuser");
		assertEquals(1, persons.size());
		Person person = persons.get(0);
		
		assertEquals("Test", person.getLastName());
		assertEquals("User", person.getFirstName());
		assertEquals("test.user@edu.fh-joanneum.at", person.getEmailAddress());
		assertEquals("+43666123456789", person.getPhoneNumberMobile());
		assertEquals("Knecht", person.getJobPosition());
		assertEquals("Team 4", person.getDepartment());
		assertEquals("A", person.getLocationBuilding());
		assertEquals("666", person.getLocationRoomNumber());
		assertEquals(new Integer(-2), person.getLocationFloor());
		
		Calendar calBirth = Calendar.getInstance();
		calBirth.set(Calendar.YEAR, 1980);
		calBirth.set(Calendar.MONTH, Calendar.JANUARY);
		calBirth.set(Calendar.DAY_OF_MONTH, 1);
		calBirth.set(Calendar.HOUR_OF_DAY, 0);
		calBirth.set(Calendar.MINUTE, 0);
		calBirth.set(Calendar.SECOND, 0);
		calBirth.set(Calendar.MILLISECOND, 0);
		assertEquals(calBirth.getTime(), person.getDateOfBirth());

		Calendar calEntry = Calendar.getInstance();
		calEntry.set(Calendar.YEAR, 2015);
		calEntry.set(Calendar.MONTH, Calendar.JANUARY);
		calEntry.set(Calendar.DAY_OF_MONTH, 1);
		calEntry.set(Calendar.HOUR_OF_DAY, 0);
		calEntry.set(Calendar.MINUTE, 0);
		calEntry.set(Calendar.SECOND, 0);
		calEntry.set(Calendar.MILLISECOND, 0);
		assertEquals(calEntry.getTime(), person.getDateOfEntry());
		
		assertTrue(person.getIsActive());
		assertFalse(person.getIsExtern());
		assertTrue(person.getIsLoginAllowed());
		assertFalse(person.getIsOnline());
		assertFalse(person.isAdmin());
	}
	
	/*
	 * PSE2015-11	Als angemeldeter Benutzer des Systems möchte ich meine persönlichen Daten verändern können
	 */
	@Test
	public void changePersonalData() {
    	Person user = userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId());

    	// Interessen
		List<String> tags = new ArrayList<>();
		tags.add("Software");
		userService.update(user, tags);
		
    	// Communities -> ChatService Tests
		
		user = userService.findUsers("integrationtestuser").get(0);
		assertEquals("Software", user.getPersonTags().get(0).getTag().getToken());
	}
	
	/*
	 * PSE2015-12	Als angemeldeter Benutzer des Systems möchte ich mein Passwort ändern können
	 */
    @Test
    public void changePassword() {
    	assertTrue(userService.changePassword("integrationtestuser", "12345678", "ABCDEFGH"));
    	// login must be possible with new password
		assertNull(userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId()));
		assertNotNull(userService.loginUser("integrationtestuser", "ABCDEFGH", UserSession.createSessionId()));
    }

	/*
	 * PSE2015-13	Als angemeldeter Admin möchte ich einen User aktiv/inaktiv setzen können
	 */
    @Test
    public void setUserActive() {
		// login person is possible
		Person user = userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId());
		assertNotNull(user);

		// update active flag
		user.setIsActive(false);
		userService.update(user, null);

		// login is not possible anymore
		assertNull(userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId()));
    }
	
	/*
	 * PSE2015-14	Als angemeldeter Admin möchte ich allgemeine Informationen eines Users verändern können
	 */
	@Test
	public void changeUserData() {
		List<Person> persons = userService.findUsers("integrationtestuser");
		Person user = persons.get(0);

		user.setFirstName("User_Updated");
		user.setLastName("Test_Updated");
		user.setDepartment("Team 4 Updated");
		userService.update(user, null);

		persons = userService.findUsers("integrationtestuser");
		user = persons.get(0);
		assertEquals("User_Updated", user.getFirstName());
		assertEquals("Test_Updated", user.getLastName());
		assertEquals("Team 4 Updated", user.getDepartment());
	}

    
    /*
	 * PSE2015-29	Als angemeldeter User möchte ich per Klick auf das Startseitemenuitem des angemeldeten Users im Header auf die Userseite kommen
	 * reine GUI Userstory
	 */
	
	/*
	 * PSE2015-30	Als angemeldeter Benutzer des Systems möchte ich mich vom System abmelden können	
	 */
    @Test
    public void logout() {
    	Person user = userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId());
		assertNotNull(user);
		assertNotNull(user.getCurrentSessionId());
		assertTrue(user.getIsOnline());

		userService.logoutUser(user.getUserName());
		
		List<Person> persons = userService.findUsers("integrationtestuser");
		assertEquals(1, persons.size());
		assertNull(persons.get(0).getCurrentSessionId());
		assertFalse(persons.get(0).getIsOnline());
    }
	
	/*
	 * PSE2015-46	Als angemeldeter Benutzer des System möchte ich einen anderen Benutzer als Kontakt hinzufügen können
	 */
    @Test
    public void addContact() {
    	Person user = userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId());
		assertNotNull(user);

		List<Person> persons = userService.findUsers("integrationtestcontactuser");
		assertEquals(1, persons.size());
		Person contactPersonExisting = persons.get(0);

		persons = userService.findUsers("angelofr13");
		assertEquals(1, persons.size());
		Person contactPersonNew = persons.get(0);
		
		userService.createRelation(user, contactPersonNew);

		persons = userService.findUsers("integrationtestuser");
		assertEquals(1, persons.size());
		
		List<PersonRelation> relations = persons.get(0).getPersonSourceRelations();
		
		int relationsFound = 0;
		for (PersonRelation relation : relations) {
			if (relation.getTargetPerson().equals(contactPersonNew) || relation.getTargetPerson().equals(contactPersonExisting))
				relationsFound++;
		}
		assertEquals(2, relationsFound);
    }
		
	/*
	 * PSE2015-47	Als angemeldeter Benutzer des System möchte ich einen anderen Benutzer als Kontakt entfernen können
	 */
    @Test
    public void removeContact() {
    	Person user = userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId());
		assertNotNull(user);
		assertEquals(1, user.getPersonSourceRelations().size());

		List<Person> persons = userService.findUsers("integrationtestcontactuser");
		Person contactPersonExisting = persons.get(0);

		userService.removeRelation(user, contactPersonExisting);

		persons = userService.findUsers("integrationtestuser");
		assertEquals(0, persons.get(0).getPersonSourceRelations().size());
    }
	
	/*
	 * 	PSE2015-49	Als angemeldeter Benutzer möchte ich die Benutzer, die ich als Kontakt hinzugefügt habe, angezeigt bekommen
	 */
    @Test
    public void listContacts() {
    	Person user = userService.loginUser("integrationtestuser", "12345678", UserSession.createSessionId());
		assertNotNull(user);
		assertEquals(1, user.getPersonSourceRelations().size());

		List<Person> persons = userService.findUsers("integrationtestcontactuser");
		Person contactPersonExisting = persons.get(0);

		persons = userService.findUsers("integrationtestuser");
		assertEquals(contactPersonExisting, persons.get(0).getPersonSourceRelations().get(0).getTargetPerson());
    }
	
	/*
	 * PSE2015-51	Als angemeldeter Benutzer möchte ich die Benutzer die in der selben Abteilung sind, angezeigt bekommen
	 */
    @Test
    public void listDepartmentUser() {
		List<Person> users = userService.getUsersWithDepartment("Team 2");
		assertEquals(4, users.size());
		
		List<Person> persons = userService.findUsers("angelofr13");
		assertEquals(1, persons.size());
		assertTrue(users.contains(persons.get(0)));

		persons = userService.findUsers("haringst13");
		assertEquals(1, persons.size());
		assertTrue(users.contains(persons.get(0)));

		persons = userService.findUsers("oswaldge13");
		assertEquals(1, persons.size());
		assertTrue(users.contains(persons.get(0)));

		persons = userService.findUsers("schmidtr13");
		assertEquals(1, persons.size());
		assertTrue(users.contains(persons.get(0)));
    }
    
    @Test
    public void getFullNameTest() {
    	Person p = userService.getUser("pompenig13");
    	String name = userService.getFullName(p);
    	assertEquals("Christine Pompenig", name);
    }
    
    @Test
    public void getUsersTest() {
    	List<Person> users = userService.getUsers();
    	assertEquals(28,users.size());
    }
    
    //FIXME: only working with locally stored image...
    @Test
    public void setUserImageTest() throws NamingException {
    	//Prepare document
    	//FIXME: Not working with "src/test/resources/testDocs/no_img.png"
    	DocumentService documentService = lookup(DocumentServiceFacade.class, DocumentService.class);
    	Document icon = documentService.store("pic", "D:\\no_img.png");
    	assertTrue(icon != null);
    	
    	userService.setUserImage("pompenig13", icon.getDocumentId());
    	
    	Person p = userService.getUser("pompenig13");
    	assertEquals(icon, p.getDocument());
    	assertEquals("/store/media/"+icon.getDocumentId(), userService.getImageRef(p));
    }
    
    @Test
    public void getImageRefTest() {
    	Person p = userService.getUser("angelofr13");
    	assertEquals("/protected/img/default_user_image.jpg", userService.getImageRef(p));
    }
    
    @Test
    public void setPasswordTest() {
    	userService.setPassword("integrationtestuser", "87654321");
		assertNotNull(userService.loginUser("integrationtestuser", "87654321", UserSession.createSessionId()));
    }
}
