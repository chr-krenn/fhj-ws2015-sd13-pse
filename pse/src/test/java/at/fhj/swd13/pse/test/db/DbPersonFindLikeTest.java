package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbPersonFindLikeTest extends DbTestBase {

	private static Person p1 = new Person("etester", "Ausprobierer", "Ehrenfried", "12345678");
	private static Person p2 = new Person("xtester", "Probierer", "Xaver", "12345678");
	private static Person p3 = new Person("xtesla", "Tesla", "Xaver", "12345678");
	private static Person p4 = new Person("maha", "Maier", "Hans", "12345678");

	@BeforeClass
	public static void setup() throws Exception {

		DbTestBase.prepareDbOnly();

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.insert(p1);
			personDAO.insert(p2);
			personDAO.insert(p3);
			personDAO.insert(p4);

			dbContext.commit();
		}
	}

	@AfterClass
	public static void teardown() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.remove(p1.getPersonId());
			personDAO.remove(p2.getPersonId());
			personDAO.remove(p3.getPersonId());
			personDAO.remove(p4.getPersonId());

			dbContext.commit();
		}
	}

	@Test
	public void testFetch0() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();

			List<Person> result = personDAO.getPersonLike("xxx");

			assertEquals(0, result.size());
		}
	}

	@Test
	public void testFetch1() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();

			List<Person> result = personDAO.getPersonLike("mai");

			assertEquals(1, result.size());
			assertEquals("Maier", result.get(0).getLastName());
		}
	}

	@Test
	public void testFetch2() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();

			List<Person> result = personDAO.getPersonLike("xtes");

			assertEquals(2, result.size());
		}
	}

	@Test
	public void testFetch2_2() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();

			List<Person> result = personDAO.getPersonLike("probier");

			assertEquals(2, result.size());

			for (Person person : result) {
				assertTrue(person.getLastName().toLowerCase().contains("probier"));
			}
		}
	}
}
