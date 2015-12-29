package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.*;

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
		List<Person> persons = userService.findUsers("Tein");
		assertEquals("Teiniker", persons.get(0).getLastName());
	}
	
	
	/*
	 * 	PSE2015-10 Als angemeldeter Benutzer des Systems möchte ich die Profile anderer Benutzer des Systems anschauen können
	 */
	@Test
	public void viewProfile() {
		List<Person> persons = userService.findUsers("Tein");
		assertEquals("Teiniker", persons.get(0).getLastName());
	}
}
