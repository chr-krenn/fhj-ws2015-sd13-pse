package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbPersonTestRead extends DbTestBase {

	@BeforeClass
	public static void setup() {

		DbTestBase.prepare();
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

	@Test(expected = EntityNotFoundException.class)
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
	public void getByUsernameAssertRequired() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p = personDAO.getByUsername("pse_system", true);
			assertEquals(p.getPersonId(), 1);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void getByUsernameAssertNotRequired() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p = personDAO.getByUsername("pse_system", false);
			assertEquals(p.getPersonId(), 1);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void getByUsernameUnknownAssertRequired() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			personDAO.getByUsername("unkown", true);
			fail("No Excpetion thrown");
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(EntityNotFoundException.class.getName(), e.getClass().getName());
		}
	}

	@Test
	public void getByUsernameUnknownAssertNotRequired() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p = personDAO.getByUsername("unkown", false);
			assertNull(p);
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
	public void getAllPersonsWithCount() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			List<Person> persons = personDAO.getAllPersons(0, 5);
			assertEquals(5, persons.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
	
	@Test
	public void getAllPersons() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			List<Person> persons = personDAO.getAllPersons();
			assertEquals(28, persons.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void getAllPersonsWithDepartment() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			List<Person> persons = personDAO.getAllPersonsWithDepartment("Team 4");
			assertEquals(7, persons.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
	
	@Test
	public void getAllWithNullPasswords() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			List<Person> persons = personDAO.getAllWithNullPasswords();
			assertEquals(0, persons.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
	
	@Test
	public void getByUsernameNoneFound() {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = personDAO
					.getByUsername("gnurmifgjp�sh l hlfdyghlfd glkdfghv y");

			assertEquals(p, null);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void getByEmailAddress() {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = personDAO.getByEmailAddress("pse@edu.fh-joanneum.at");

			assertEquals(p.getPersonId(), 2);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void getUserByInvalidEMailAddress() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p = personDAO.getByEmailAddress("xx@yy.at");
			assertEquals(p, new Person("", "", "", ""));
		}
	}
	
	@Test
	public void getPersonLike() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			List<Person> personLikes = personDAO.getPersonLike("gelofr");
			assertEquals("Franz",  personLikes.get(0).getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
