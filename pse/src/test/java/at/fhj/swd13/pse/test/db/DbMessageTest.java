package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.Query;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.MessageDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.test.util.DbTestBase;
import at.fhj.swd13.pse.test.util.JdbcTestHelper;

public class DbMessageTest extends DbTestBase {

	private static final JdbcTestHelper JDBC_HELPER = new JdbcTestHelper();

	@BeforeClass
	public static void init() throws Exception {
		DbTestBase.prepare();
		// Setting up private communities per user
		try (DbContext dbContext = contextProvider.getDbContext()) {
			ChatService chatService = new ChatServiceImpl(dbContext);
			chatService.createAllPrivateCommunities();
			dbContext.commit();
		}
		// Adding private message
		JDBC_HELPER.executeSqlScript("SQL/testdata_DBMessageTest.sql");
	}

	@Test
	public void testLoadAllMessages() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<Message> activities = messageDAO.loadAll();

			assertNotNull(activities);
			assertEquals(17, activities.size());
		}
	}

	/**
	 * See testdata.sql & testdata_DBMessageTest.sql Should NOT select expired
	 * messages not yet valid messages news messages created by the user
	 * comments messages in private communities the user is not a member of
	 * messages in public communities the user is not a member of UNLESS the
	 * message is tagged with a user interest the message is written by a
	 * contact of the user
	 * 
	 * @throws Exception
	 */
	@Test
	public void testActivityStream() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<Message> activities = messageDAO.loadForUser(getPerson(108));

			assertNotNull(activities);
			assertEquals(6, activities.size());
		}
	}

	/**
	 * See testdata.sql No specific message exists for this user, but there are
	 * three messages without any restrictions (no community)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testActivityStream2() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<Message> activities = messageDAO.loadForUser(getPerson(110));

			assertNotNull(activities);
			assertEquals(2, activities.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testfindForUserQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findForUser");
			query.setParameter("person", getPerson(108));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(4, message.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByOneTagQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findByTags");
			query.setParameter("person", getPerson(108));
			query.setParameter("tags",
					dbContext.getTagDAO().getByToken("Software"));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(1, message.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByTagsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findByTags");
			query.setParameter("person", getPerson(108));
			query.setParameter("tags",
					dbContext.getTagDAO().getByPerson(getPerson(108)));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(1, message.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByContactsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findByContacts");
			query.setParameter("person", getPerson(108));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(2, message.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindForUserAndTagsAndContactsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext
					.createNamedQuery("Message.findForUserAndTagsAndContacts");
			query.setParameter("person", getPerson(108));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(6, message.size());
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindNewsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findNews");
			query.setParameter("id", 1);
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(2, message.size());
		}
	}

	@Test
	public void testLoadMessagesForCommunity() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<Message> activities = messageDAO.loadNews(100);

			assertNotNull(activities);
			assertEquals(2, activities.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindCommentsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findComments");
			query.setParameter("message", dbContext.getMessageDAO().getById(1));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(3, message.size());
		}
	}

	@Test
	public void testFindComments() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			MessageDAO messageDAO = dbContext.getMessageDAO();

			List<Message> activities = messageDAO.loadComments(dbContext
					.getMessageDAO().getById(1));

			assertNotNull(activities);
			assertEquals(3, activities.size());
		}
	}

	@Test
	public void testUpdateMessage() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageDAO messageDAO = dbContext.getMessageDAO();

			Message m = messageDAO.getById(1);
			assertTrue(!m.getMessage().equals("Updated message text"));
			m.setMessage("Updated message text");

			messageDAO.update(m);

			assertEquals("Updated message text", dbContext.getMessageDAO()
					.getById(1).getMessage());
		}
	}

	@Test
	public void testRemoveAndReinsertMessage() throws Exception {
		Message message = null;
		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageDAO messageDAO = dbContext.getMessageDAO();
			Message m = messageDAO.getById(8);
			messageDAO.remove(8);
			message = new Message(m);
			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {
			dbContext.getMessageDAO().getById(8);
			fail();
		} catch (EntityNotFoundException e) {
			assertNotNull(e);
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {
			// Attention: Transaction will be set to read only when you try to persist a detached entity 
			dbContext.getMessageDAO().insert(message);
			dbContext.commit();
		}
		
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Message m = dbContext.getMessageDAO().getById(message.getMessageId());
			assertEquals(m, message);
		}
	}

	private Person getPerson(int id) throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			return personDAO.getById(id);
		}
	}

}
