package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;

public class MessageDAOImpl extends DAOBase implements MessageDAO {

	public MessageDAOImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public void insert(Message message) throws ConstraintViolationException{
		dbContext.persist(message);
	}

	@Override
	public Message getById(int messageId) throws EntityNotFoundException {
		final Query q = dbContext.createNamedQuery("Message.findById");
		q.setParameter("id", messageId);

		Message result = fetchSingle(q);

		if (result == null) {
			throw new EntityNotFoundException("Message not found");
		}

		return result;
	}

	@Override
	public void remove(int messageId) {
		final Query q = dbContext.createNamedQuery("Message.deleteById");
		q.setParameter("id", messageId);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> loadAll() {
		Query query = dbContext.createNamedQuery("Message.findAllOrderedByNewest");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> loadForUser(Person user) {
		Query query = dbContext.createNamedQuery("Message.findForUserAndTagsAndContacts");
		query.setParameter("person", user);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> loadNews(int communityId) {
		Query query = dbContext.createNamedQuery("Message.findNews");
		query.setParameter("id", communityId);
		return query.getResultList();
	}
}
