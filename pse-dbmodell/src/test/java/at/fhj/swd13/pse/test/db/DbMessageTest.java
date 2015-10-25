package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.MessageDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.dto.MessageDTO;

public class DbMessageTest extends DbTestBase {

	@BeforeClass
	public static void init() {
		DbTestBase.prepare();
	}

	/*
	 * Test not working yet because the query used in loadForUser only works
	 * with Hibernate, but the tests are currently not using Hibernate
	 * Test will be improved once the basics work...
	 */
	@Test
	@Ignore
	public void testActivityStream() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<MessageDTO> activities = messageDAO.loadForUser(getPerson(108));

			assertNotNull(activities);
		}
	}

	private Person getPerson(int id) throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			return personDAO.getById(id);
		}
	}
}
