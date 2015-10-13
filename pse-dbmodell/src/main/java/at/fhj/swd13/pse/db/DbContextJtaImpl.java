package at.fhj.swd13.pse.db;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.CommunityDAOImpl;
import at.fhj.swd13.pse.db.dao.DocumentDAO;
import at.fhj.swd13.pse.db.dao.DocumentDAOImpl;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.dao.PersonDAOImpl;
import at.fhj.swd13.pse.db.dao.TagDAO;
import at.fhj.swd13.pse.db.dao.TagDAOImpl;

@RequestScoped
@CurrentDbContext
public class DbContextJtaImpl implements DbContext {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private Logger logger;
	
	public DbContextJtaImpl() {}

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
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.DbContext#getDocumentDAO()
	 */
	@Override
	public DocumentDAO getDocumentDAO() {

		return new DocumentDAOImpl(this);
	}
	
	
	@PostConstruct
	protected void postConstruct() {
		logger.info("[CONTEXT] ++ constructed");
	}

	@PreDestroy
	protected void preDestroy() {
		logger.info("[CONTEXT] -- destroy");		
	}
	
	@Override
	public void close() throws Exception {}
}
