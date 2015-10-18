/**
 * 
 */
package at.fhj.swd13.pse.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * @author florian.genser
 *
 */
public class MessageRepositoryImpl implements MessageRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Message> loadAll() {
		
		TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m ORDER BY m.createdOn DESC", Message.class);
		
		return query.getResultList();	
	}

	@Override
	public List<Message> loadForUser(Person user) {
		TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m " +
				"WHERE (m.expiresOn is null or m.expiresOn > CURRENT_TIMESTAMP) ORDER BY m.createdOn DESC", Message.class);
		return query.getResultList();
	}
	
}
