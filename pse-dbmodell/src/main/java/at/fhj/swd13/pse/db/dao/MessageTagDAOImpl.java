package at.fhj.swd13.pse.db.dao;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.MessageTag;

public class MessageTagDAOImpl extends DAOBase implements MessageTagDAO {

	public MessageTagDAOImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public void insert(MessageTag messageTag) throws ConstraintViolationException{
		dbContext.persist(messageTag);
	}

	@Override
	public MessageTag getById(int messageTagId) {
		Query query = dbContext.createNamedQuery("MessageTag.findById");
		query.setParameter("id", messageTagId);
		return (MessageTag) query.getSingleResult();
	}

	@Override
	public void remove(int messageTagId) {
		final Query q = dbContext.createNamedQuery("MessageTag.deleteById");
		q.setParameter("id", messageTagId);
		q.executeUpdate();
	}

	

}
