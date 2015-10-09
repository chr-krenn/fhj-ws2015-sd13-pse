/**
 * 
 */
package at.fhj.swd13.pse.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.fhj.swd13.pse.db.entity.Message;

/**
 * @author florian.genser
 *
 */
public class MessageRepositoryImpl implements MessageRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Message> loadAll() {
		
		TypedQuery<Message> query = entityManager.createQuery("from Message", Message.class);
		
		return query.getResultList();
	}
}
