package at.fhj.swd13.pse.db;

import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.CommunityDAOImpl;
import at.fhj.swd13.pse.db.dao.DocumentDAO;
import at.fhj.swd13.pse.db.dao.DocumentDAOImpl;
import at.fhj.swd13.pse.db.dao.DocumentLibraryEntryDAO;
import at.fhj.swd13.pse.db.dao.DocumentLibraryEntryDAOImpl;
import at.fhj.swd13.pse.db.dao.MessageDAO;
import at.fhj.swd13.pse.db.dao.MessageDAOImpl;
import at.fhj.swd13.pse.db.dao.MessageTagDAO;
import at.fhj.swd13.pse.db.dao.MessageTagDAOImpl;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.dao.PersonDAOImpl;
import at.fhj.swd13.pse.db.dao.TagDAO;
import at.fhj.swd13.pse.db.dao.TagDAOImpl;

/**
 * Implementaition fo a db session using jpa/ mysql
 *
 */
@Alternative
public class DbContextImpl implements AutoCloseable, DbContext {

	private static final String PERSISTENCE_UNIT_NAME = "pseDbModell";

	private static EntityManagerFactory factory;

	private final EntityManager entityManager;

	private EntityTransaction transaction;

	/**
	 * 'Create' a new session with the database and begin the transaction
	 */
	public DbContextImpl() {
		assertStatics();

		entityManager = factory.createEntityManager();

		transaction = beginTransaction();
		transaction.begin();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#clearCache()
	 */
	public void clearCache() {

		entityManager.getEntityManagerFactory().getCache().evictAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.DbContext#persist(java.lang.Object)
	 */
	@Override
	public void persist(final Object target) throws ConstraintViolationException {

		try {
			entityManager.persist(target);
		} catch (PersistenceException x) {
			if (x.getCause() != null && x.getCause().getClass() == org.hibernate.exception.ConstraintViolationException.class) {

				throw new ConstraintViolationException("entity already exists: " + target.getClass().getName(), x);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#remove(java.lang.Object)
	 */
	public void remove(final Object target) {

		entityManager.remove(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.DbContext#commit()
	 */
	@Override
	public void commit() throws ConstraintViolationException {

		if (transaction == null || !transaction.isActive()) {

			throw new IllegalStateException("no transaction open");
		}

		transaction.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.DbContext#rollback()
	 */
	@Override
	public void rollback() {

		if (transaction == null || !transaction.isActive()) {

			throw new IllegalStateException("no transaction open");
		}

		transaction.rollback();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.DbContext#createQuery(java.lang.String)
	 */
	@Override
	public Query createQuery(final String queryString) {

		return entityManager.createQuery(queryString);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#createNamedQuery(java.lang.String)
	 */
	@Override
	public Query createNamedQuery(final String queryName) {

		return entityManager.createNamedQuery(queryName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.DbContext#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {

		return entityManager;
	}

	private EntityTransaction beginTransaction() {

		return entityManager.getTransaction();
	}

	private void assertStatics() {

		if (null == factory) {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#getPersonDAO()
	 */
	public PersonDAO getPersonDAO() {

		return new PersonDAOImpl(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#getTagDAO()
	 */
	public TagDAO getTagDAO() {

		return new TagDAOImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#getCommunityDAO()
	 */
	public CommunityDAO getCommunityDAO() {

		return new CommunityDAOImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContext#getDocumentDAO()
	 */
	@Override
	public DocumentDAO getDocumentDAO() {

		return new DocumentDAOImpl(this);
	}

	@Override
	public MessageDAO getMessageDAO() {

		return new MessageDAOImpl(this);
	}

	@Override
	public MessageTagDAO getMessageTagDAO() {

		return new MessageTagDAOImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.DbContext#close()
	 */
	@Override
	public void close() throws Exception {

		if (transaction != null) {

			if (transaction.isActive()) {
				transaction.rollback();
			}
			transaction = null;
		}

		entityManager.close();
	}

	@Override
	public DocumentLibraryEntryDAO getDocumentLibraryDAO() {
		return new DocumentLibraryEntryDAOImpl(this);
	}

}
