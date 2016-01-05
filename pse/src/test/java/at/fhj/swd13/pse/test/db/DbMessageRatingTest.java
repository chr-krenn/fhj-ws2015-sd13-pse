package at.fhj.swd13.pse.test.db;


import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.test.util.DbTestBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.MessageDAO;
import at.fhj.swd13.pse.db.dao.MessageRatingDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.Person;

public class DbMessageRatingTest extends DbTestBase {

	@BeforeClass
	public static void setup() throws Exception {
		DbTestBase.prepare();

		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();
			PersonDAO personDAO = dbContext.getPersonDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();

			Person person = personDAO.getById(118);
			Message message = messageDAO.getById(2);
			MessageRating rating = new MessageRating();
			rating.setMessage(message);
			rating.setPerson(person);
			rating.setCreatedAt(new Date());
			messageRatingDAO.insert(rating);

			person = personDAO.getById(114);
			message = messageDAO.getById(3);
			rating = new MessageRating();
			rating.setMessage(message);
			rating.setPerson(person);
			rating.setCreatedAt(new Date());
			messageRatingDAO.insert(rating);
			
			dbContext.commit();
		}
	}

	@Test
	public void insert() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();
			PersonDAO personDAO = dbContext.getPersonDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();

			Person person = personDAO.getById(104);
			Message message = messageDAO.getById(4);
			
			MessageRating rating = new MessageRating();
			rating.setMessage(message);
			rating.setPerson(person);
			rating.setCreatedAt(new Date());

			messageRatingDAO.insert(rating);
			
			List<Person> raters = messageRatingDAO.loadAllRatersByMessage(message);
			assertEquals(person, raters.get(0));
		}
	}
	
	@Test
	public void remove() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();
			PersonDAO personDAO = dbContext.getPersonDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();

			Person person = personDAO.getById(118);
			Message message = messageDAO.getById(2);
			MessageRating rating = dbContext.getMessageRatingDAO().findRatingByPersonAndMessage(message, person);
			message.removeMesasgeRating(rating);
			person.removeMesasgeRating(rating);
			messageRatingDAO.remove(rating);
			dbContext.commit();
		}
		
		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();
			Message message = messageDAO.getById(2);
			List<Person> raters = messageRatingDAO.loadAllRatersByMessage(message);
			assertEquals(0, raters.size());
		}
	}	

	@Test
	public void removeById() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();
			PersonDAO personDAO = dbContext.getPersonDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();

			Person person = personDAO.getById(114);
			Message message = messageDAO.getById(3);
			
			messageRatingDAO.remove(messageRatingDAO.findRatingByPersonAndMessage(message, person).getMessageRatingId());
			dbContext.commit();

			List<Person> raters = messageRatingDAO.loadAllRatersByMessage(message);
			assertEquals(0, raters.size());
		}
	}	

	@Test
	public void loadAllRatersByMessage() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();

			Person person = personDAO.getById(108);
			Message message = messageDAO.getById(1);
			
			List<Person> raters = messageRatingDAO.loadAllRatersByMessage(message);
			assertEquals(person, raters.get(0));
		}
	}	

	@Test
	public void findRatingByPersonAndMessage() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			MessageDAO messageDAO = dbContext.getMessageDAO();
			MessageRatingDAO messageRatingDAO = dbContext.getMessageRatingDAO();

			Person person = personDAO.getById(108);
			Message message = messageDAO.getById(1);
			
			MessageRating rating = messageRatingDAO.findRatingByPersonAndMessage(message, person);
			assertEquals(message, rating.getMessage());
			assertEquals(person, rating.getPerson());
		}
	}	
}
