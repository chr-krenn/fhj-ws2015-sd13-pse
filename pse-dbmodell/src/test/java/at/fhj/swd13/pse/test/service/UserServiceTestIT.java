package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class UserServiceTestIT extends DbTestBase {

	private UserService userService;
	
	@BeforeClass
	public static void init() throws Exception {
		
		DbTestBase.prepare();
	}
	
    @Before
    public void setUp() throws NamingException  
    {
    	final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        
        final String jndiName = "ejb:" + "" 
        		+ "/" + "pse-dbmodell" 
        		+ "/" 
        		+ "/" + "UserServiceFacade" 
        		+ "!" + UserService.class.getName();
   
        userService =  (UserService) context.lookup(jndiName);
    }	
	
	@Test
	public void findUsers() {
		List<Person> persons = userService.findUsers("Tein");
		assertEquals("Teiniker", persons.get(0).getLastName());
	}
}
