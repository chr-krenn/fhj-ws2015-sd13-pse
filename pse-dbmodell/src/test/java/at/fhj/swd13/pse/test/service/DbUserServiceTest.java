package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.PasswortStrengthValidatorImpl;
import at.fhj.swd13.pse.domain.user.WeakPasswordException;
import at.fhj.swd13.pse.service.UserService;

public class DbUserServiceTest {

	private DbContextProvider contextProvider;

	private UserService userService;

	private Person plainPerson = new Person("plainPerson", "Person", "Plain", "12345678");
	private List<Object> toDelete = new ArrayList<Object>();

	@Before
	public void setup() throws Exception {

		contextProvider = new DbContextProviderImpl();

		userService = new UserService();
		userService.setDbContext(contextProvider);
		userService.setPasswordStrengthValidator(new PasswortStrengthValidatorImpl());

		try (DbContext context = contextProvider.getDbContext()) {

			context.getPersonDAO().getById(1).setHashedPassword("--");

			context.getPersonDAO().insert(plainPerson);

			context.commit();
		}
	}

	@After
	public void teardown() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			for (Object o : toDelete) {
				context.remove(o);
			}

			context.getPersonDAO().remove(plainPerson.getPersonId());

			context.commit();
		}
	}

	@Test
	public void setDefaultPassword() throws Exception {

		assertEquals(1, userService.updateNullPasswords());

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Person p = context.getPersonDAO().getById(1);
			assertNotNull(p);
			assertNotNull(p.getHashedPassword());
		}
	}

	@Test
	public void loginUser() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			assertNotNull(userService.loginUser(plainPerson.getUserName(), "12345678", dbContext));
		}
	}

	@Test
	public void loginUserUnknown() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			assertNull(userService.loginUser("xxxPerson", "12345678", dbContext));
		}
	}

	@Test
	public void loginUserInvalidPassword() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			assertNull(userService.loginUser(plainPerson.getUserName(), "gustl", dbContext));
			
			dbContext.commit();
		}

	}

	@Test
	public void passwordOk() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			userService.setPassword( plainPerson.getUserName(), "gustavgusgustav", dbContext);			
			
			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			assertNotNull(userService.loginUser(plainPerson.getUserName(), "gustavgusgustav", dbContext));
		}
	}

	@Test(expected = WeakPasswordException.class)
	public void passwordWeakNull() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			userService.setPassword( plainPerson.getUserName(), null, dbContext);			
		}
	}

	@Test(expected = WeakPasswordException.class)
	public void passwordWeakEmpty() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			userService.setPassword( plainPerson.getUserName(), "", dbContext);			
		}
	}

	@Test(expected = WeakPasswordException.class)
	public void passwordWeakShort() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			userService.setPassword( plainPerson.getUserName(), "3344", dbContext);			
		}
	}
}
