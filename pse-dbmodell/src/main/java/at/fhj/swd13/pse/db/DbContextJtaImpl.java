package at.fhj.swd13.pse.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.CommunityDAOImpl;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.dao.PersonDAOImpl;
import at.fhj.swd13.pse.db.dao.TagDAO;
import at.fhj.swd13.pse.db.dao.TagDAOImpl;

public class DbContextJtaImpl implements DbContext {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void persist(Object target) {

		entityManager.persist(target);

	}

	@Override
	public void remove(Object target) {

		entityManager.remove(target);
	}

	@Override
	public void commit() throws ConstraintViolationException {
		// nothing, handled by jta
	}

	@Override
	public void rollback() {
		// nothing, handled by jta
	}

	@Override
	public void clearCache() {
		// nothing, handled by jta
	}

	@Override
	public Query createQuery(String queryString) {

		return entityManager.createQuery(queryString);
	}

	@Override
	public Query createNamedQuery(String queryName) {

		return entityManager.createNamedQuery(queryName);
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public PersonDAO getPersonDAO() {

		return new PersonDAOImpl(this);
	}

	@Override
	public TagDAO getTagDAO() {

		return new TagDAOImpl(this);
	}

	@Override
	public CommunityDAO getCommunityDAO() {

		return new CommunityDAOImpl(this);
	}

	@Override
	public void close() throws Exception {
	}
}
