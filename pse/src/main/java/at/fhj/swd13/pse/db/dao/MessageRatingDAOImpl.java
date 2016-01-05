package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.Person;

public class MessageRatingDAOImpl extends DAOBase implements MessageRatingDAO {

	public MessageRatingDAOImpl(DbContext dbContext) {
		super(dbContext);
	}
	
	@Override
	public void insert(MessageRating rating) {
		dbContext.persist(rating);	
	}

	@Override
	public void remove(MessageRating rating) {
		dbContext.remove(rating);
		
	}

	@Override
	public void remove(int ratingId) {
		Query q = dbContext.createNamedQuery("MessageRating.deleteById");
		q.setParameter("id", ratingId);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> loadAllRatersByMessage(Message message) {
		Query q = dbContext.createNamedQuery("MessageRating.findAllRatersByMessage");
		q.setParameter("message", message);
		return (List<Person>) q.getResultList();
	}

	@Override
	public MessageRating findRatingByPersonAndMessage(Message message, Person person) {
		Query q = dbContext.createNamedQuery("MessageRating.findRatingByPersonAndMessage");
		q.setParameter("message", message);
		q.setParameter("person", person);
		return (MessageRating) q.getSingleResult();
	}
}
