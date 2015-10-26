package at.fhj.swd13.pse.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.dto.MessageDTO;

public class MessageDAOImpl extends DAOBase implements MessageDAO {

	public MessageDAOImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public void insert(Message message) throws ConstraintViolationException{
		dbContext.persist(message);
	}

	@Override
	public Message getById(int messageId) {
		return null;
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
	public List<MessageDTO> loadForUser(Person user) {
		// TODO Update query (work in progress by Christine!)
		Query query = dbContext.createNamedQuery("Message.findForUser");
		query.setParameter("person", user);
		query.setParameter("communities", dbContext.getCommunityDAO().getCommunities(user));
		List<MessageDTO> result = new ArrayList<MessageDTO>();
		for(Message m: (List<Message>)query.getResultList()) {
			result.add(new MessageDTO(m));
		}
		return result;
	}

}
