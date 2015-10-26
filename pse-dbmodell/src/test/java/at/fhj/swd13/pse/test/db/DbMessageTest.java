package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.MessageDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.test.util.DbTestBase;
import at.fhj.swd13.pse.test.util.JdbcTestHelper;

public class DbMessageTest extends DbTestBase {
	
	private static final JdbcTestHelper JDBC_HELPER = new JdbcTestHelper();
	
	@BeforeClass
	public static void init() throws Exception {
		DbTestBase.prepare();
		//Setting up private communities per user
		try (DbContext dbContext = contextProvider.getDbContext()) {
			ChatService chatService = new ChatServiceImpl(dbContext);
			chatService.createAllPrivateCommunities();
			dbContext.commit();
		}
		//Adding private message
		JDBC_HELPER.executeSqlScript("SQL/testdata_DBMessageTest.sql");
	}

	@Test
	public void testActivityStream() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<MessageDTO> activities = messageDAO.loadForUser(getPerson(108));

			assertNotNull(activities);
			assertEquals(4, activities.size());
		}
	}

	private Person getPerson(int id) throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			return personDAO.getById(id);
		}
	}
	
}
