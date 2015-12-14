package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.PasswordStrengthValidatorImpl;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.domain.user.UserServiceImpl;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class UserServiceTestIT extends DbTestBase {

	private UserService userService;

	
	@BeforeClass
	public static void init() throws Exception {
		
		DbTestBase.prepare();
	}
	
	@Before
	public void setup() throws NamingException {
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
	public void setDefaultPassword() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());
			
			//-2: the two users i created above since they have passwords
			assertEquals(dbContext.getPersonDAO().getAllPersons().size(), userService.updateNullPasswords());
		}
		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Person p = context.getPersonDAO().getById(1);
			assertNotNull(p);
			assertNotNull(p.getHashedPassword());
		}
	}
}
