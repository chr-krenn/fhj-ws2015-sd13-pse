package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.ParameterException;
import at.fhj.swd13.pse.db.entity.Person;

public class DbPersonTest {

	private static DbContextProvider contextProvider;

	@BeforeClass
	public static void setup() {
		contextProvider = new DbContextProviderImpl();
	}

	@Test
	public void getById() {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = personDAO.getById(1); // 1 is the system internal user
												// that should always exist

			assertEquals(p.getUserName(), "pse_system");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test(expected=EntityNotFoundException.class)
	public void getByIdNoneFound() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.getById(-1); 
		}
	}

	@Test
	public void getByUsername() {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = personDAO.getByUsername("pse_system");

			assertEquals(p.getPersonId(), 1);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void getByUsernameXaseMismatch() {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = personDAO.getByUsername("Pse_system");

			assertNull(p);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void getByUsernameNoneFound() {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = personDAO.getByUsername("gnurmifgjp�sh l hlfdyghlfd glkdfghv y");

			assertEquals(p, null);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void insert() throws Exception, ConstraintViolationException {

		Person p = new Person("etester", "Tester", "Ehrenfried", "1234567");

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.insert(p);
			dbContext.commit();

			assertNotEquals(0, p.getPersonId());

		} catch (Exception e) {

			e.printStackTrace();
			fail("Exception " + e.getMessage());

		} finally {

			if (p.getPersonId() != 0) {
				try (DbContext dbContext = contextProvider.getDbContext()) {

					PersonDAO personDAO = dbContext.getPersonDAO();

					personDAO.remove(p.getPersonId());

					dbContext.commit();
				}
			}
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void insertRemovCycle() throws Exception {

		Person p = new Person("etester", "Tester", "Ehrenfried", "12345678");

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.insert(p);
			dbContext.commit();

			assertNotEquals(0, p.getPersonId());
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person x = personDAO.getById(p.getPersonId());

			personDAO.remove(x);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person x = personDAO.getById(p.getPersonId());
			assertEquals(x, new Person());
		}
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertDuplicate() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p1 = new Person("etester", "Tester", "Ehrenfried", "12345678");

			Person p2 = new Person("etester", "Tester", "Ehrenfried", "12345678");

			personDAO.insert(p1);
			personDAO.insert(p2);
			dbContext.commit();

		} catch (Exception e) {

			throw e;
		}
	}

	@Test(expected = ParameterException.class)
	public void removeOnNull() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.remove(null);
		}
	}

	@Test(expected = ParameterException.class)
	public void removeOnIdZero() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = new Person();
			p.setPersonId(0);

			personDAO.remove(p);
		}
	}
}
