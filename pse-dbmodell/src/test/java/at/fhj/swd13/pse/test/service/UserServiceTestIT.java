package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class UserServiceTestIT extends RemoteTestBase {

	private UserService userService;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
    	final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        
        final String jndiName = "ejb:" + "" 
        		+ "/" + "pse-dbmodell" 
        		+ "/" 
        		+ "/" + "UserServiceFacade" 
        		+ "!" + UserService.class.getName();
   
        userService = (UserService) context.lookup(jndiName);
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
		List<Person> persons = userService.findUsers("Test");
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
}
