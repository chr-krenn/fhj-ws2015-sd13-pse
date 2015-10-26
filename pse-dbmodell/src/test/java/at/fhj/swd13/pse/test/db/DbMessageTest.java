package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.Query;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.MessageDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Message;
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
			assertEquals(6, activities.size());
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
			assertEquals(4,message.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByOneTagQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findByTags");
			query.setParameter("person", getPerson(108));
			query.setParameter("tags", dbContext.getTagDAO().getByToken("Software"));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(1,message.size());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindByTagsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findByTags");
			query.setParameter("person", getPerson(108));
			query.setParameter("tags", dbContext.getTagDAO().getByPerson(getPerson(108)));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(1,message.size());
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
			assertEquals(1,message.size());
		}
	}
	@SuppressWarnings("unchecked")
	@Test
	public void testFindForUserAndTagsAndContactsQuery() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			Query query = dbContext.createNamedQuery("Message.findForUserAndTagsAndContacts");
			query.setParameter("person", getPerson(108));
			List<Message> message = query.getResultList();
			assertNotNull(message);
			assertEquals(6,message.size());
		}
		
	}

	private Person getPerson(int id) throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			return personDAO.getById(id);
		}
	}
	
}
