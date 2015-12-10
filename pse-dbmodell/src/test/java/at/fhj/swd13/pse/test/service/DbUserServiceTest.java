package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.user.PasswordStrengthValidatorImpl;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceImpl;
import at.fhj.swd13.pse.domain.user.WeakPasswordException;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbUserServiceTest extends DbTestBase {

	private UserSession userSession;

	private Person plainPerson = new Person("plainPerson", "Person", "Plain", "12345678");
	private Person pwPerson = new Person("pwPerson", "Person", "PW", "12345678");

	private List<Object> toDelete = new ArrayList<Object>();

	@Before
	public void setup() throws Exception {

		DbTestBase.prepare();

		try (DbContext context = contextProvider.getDbContext()) {

			context.getPersonDAO().getById(1).setHashedPassword("--");

			context.getPersonDAO().insert(plainPerson);
			context.getPersonDAO().insert(pwPerson);

			context.commit();
		}

		userSession = new UserSession();
	}

	@After
	public void teardown() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			for (Object o : toDelete) {
				context.remove(o);
			}

			context.getPersonDAO().remove(plainPerson.getPersonId());
			context.getPersonDAO().remove(pwPerson.getPersonId());

			context.commit();
		}
	}

	@Test
	public void setDefaultPassword() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());
			
			//-2: the two users i created above since they have passwords
			assertEquals(dbContext.getPersonDAO().getAllPersons().size() - 2, userService.updateNullPasswords());
		}
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

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());
			
			assertNotNull(userService.loginUser(plainPerson.getUserName(), "12345678", userSession.getSessionId()));
		}
	}

	@Test
	public void loginUserUnknown() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			assertNull(userService.loginUser("xxxPerson", "12345678", userSession.getSessionId()));
		}
	}

	@Test
	public void loginUserInvalidPassword() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {
			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			assertNull(userService.loginUser(plainPerson.getUserName(), "gustl", userSession.getSessionId()));

			dbContext.commit();
		}

	}

	@Test
	public void passwordOk() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			userService.setPassword(plainPerson.getUserName(), "gustavgusgustav");

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			assertNotNull(userService.loginUser(plainPerson.getUserName(), "gustavgusgustav", userSession.getSessionId()));
		}
	}

	@Test
	public void passwordWeakNull() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			userService.setPassword(plainPerson.getUserName(), null);
		} catch (ServiceException e) {
			assertTrue(e.getCause() instanceof WeakPasswordException);
		}
	}

	@Test
	public void passwordWeakEmpty() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			userService.setPassword(plainPerson.getUserName(), "");
		} catch (ServiceException e) {
			assertTrue(e.getCause() instanceof WeakPasswordException);
		}
	}

	@Test
	public void passwordWeakShort() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());
			userService.setPassword(plainPerson.getUserName(), "3344");
		} catch (ServiceException e) {
			assertTrue(e.getCause() instanceof WeakPasswordException);
		}
	}

	@Test
	public void changePassword() throws Exception {

		final String newPassword1 = "gustl1234";

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			userService.setPassword(pwPerson.getUserName(), newPassword1);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			assertTrue(userService.isMatchingPassword(pwPerson.getUserName(), newPassword1));
		}
	}

	@Test
	public void updateUser() throws Exception {

		@SuppressWarnings("deprecation")
		final Date doe = new Date(1980, 1, 1);

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			Person p = userService.getUser(pwPerson.getUserName());	

			p.setFirstName("Hans-Otto");
			p.setLocationBuilding("Stall");
			p.setDateOfEntry(doe);

			// TODO - test more

			userService.update(p, null);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.clearCache();

			final UserService userService = new UserServiceImpl(dbContext);
			userService.setPasswordStrengthValidator(new PasswordStrengthValidatorImpl());

			Person p = userService.getUser(pwPerson.getUserName());

			assertEquals("Hans-Otto", p.getFirstName());
			assertEquals("Stall", p.getLocationBuilding());
			assertEquals(doe, p.getDateOfEntry());
		}
	}
}
