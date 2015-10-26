package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbPersonPagingTest extends DbTestBase {

	private static final int NUMBER_OF_USERS = 100;
	private static final String USER_NAME_PREFIX = "PAGING_USER_";

	@BeforeClass
	public static void setup() throws Exception {

		DbTestBase.prepareDbOnly();

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDao = dbContext.getPersonDAO();

			for (int i = 1; i < NUMBER_OF_USERS; ++i) {

				String username = USER_NAME_PREFIX + String.format("%04d", i);

				Person p = new Person(username, username, "Gustav", "12345678");

				personDao.insert(p);
			}

			dbContext.commit();
		}
	}

	@AfterClass
	public static void teardown() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDao = dbContext.getPersonDAO();

			for (int i = 1; i < NUMBER_OF_USERS; ++i) {

				String username = USER_NAME_PREFIX + String.format("%04d", i);

				Person p = personDao.getByUsername(username);

				personDao.remove(p);
			}

			dbContext.commit();
		}
	}

	@Test
	public void testPagingGeneral() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			List<Person> persons = dbContext.getPersonDAO().getAllPersons(1, 25);

			assertEquals(25, persons.size());

			assertEquals(USER_NAME_PREFIX + "0001", persons.get(0).getUserName());
			assertEquals(USER_NAME_PREFIX + "0025", persons.get(24).getUserName());
		}
	}

	@Test
	public void testPagingFull() throws Exception {

		int pageSize = 30;

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDao = dbContext.getPersonDAO();

			for (int i = 0; i < NUMBER_OF_USERS / pageSize; ++i) {

				List<Person> persons = personDao.getAllPersons(pageSize * i + 1, pageSize);

				assertEquals(pageSize, persons.size());
			}

			int remainingRows = NUMBER_OF_USERS % pageSize;

			if (remainingRows > 0) {

				int startPos = NUMBER_OF_USERS - remainingRows;

				List<Person> persons = personDao.getAllPersons(startPos, pageSize);

				assertEquals(remainingRows, persons.size());
			}
		}
	}
}
