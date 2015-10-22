/**
 * 
 */
package at.fhj.swd13.pse.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.fhj.swd13.pse.db.CurrentDbContext;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * @author florian.genser
 *
 */
public class MessageRepositoryImpl implements MessageRepository {
	
	@Inject
	@CurrentDbContext
	protected DbContext dbContext;

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
				"WHERE (m.expiresOn is null or m.expiresOn > CURRENT_TIMESTAMP)" +
				" AND (m.community is null or m.community in (:communities))" +
				"ORDER BY m.createdOn DESC", Message.class);
		query.setParameter("communities", dbContext.getCommunityDAO().getCommunities(user));
		return query.getResultList();
	}
	
}
