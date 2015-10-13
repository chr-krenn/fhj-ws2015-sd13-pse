package at.fhj.swd13.pse.db;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.DocumentDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.dao.TagDAO;

/**
 * Connection to a persistent storage service
 * (more or less a db-session)
 *
 */
public interface DbContext extends AutoCloseable {

	/**
	 * Add an object for storage to the persistent storage
	 * 
	 * @param target the object to add
	 */
	void persist(Object target);

	/**
	 * Remove an object from the persistent storage
	 * 
	 * @param target the object to remove
	 */
	void remove( Object target );
	
	
	/**
	 * Write all pending changes since instantiation to the storage
	 * 
	 * @throws ConstraintViolationException when an unique index is violated
	 * @throws IllegalStateException when commit is called 
	 * 			on an already closed DbContext
	 */
	void commit() throws ConstraintViolationException;

	/**
	 * Undo all pending changes since instantiation to the storage
	 *
	 * @throws IllegalStateException when commit is called
	 * 			 on an already closed DbContext
	 */
	void rollback();

	/**
	 * remove all cached entities
	 */
	void clearCache();
	
	/**
	 * Create a query on this transaction with the given query string
	 *  
	 * @param queryString the query to create
	 * 
	 * @return query object
 	*/
	Query createQuery(String queryString);
	
	/**
	 * Create a query on this transaction with the given name
	 *  
	 * @param queryName name of the query to instantiate
	 * 
	 * @return query object
 	*/
	Query createNamedQuery( String queryName );

	/**
	 * get the underlying entity manager - should not be used
	 * @return
	 */
	EntityManager getEntityManager();


	/**
	 * Get a dao for the person entity and closely related entities
	 * 
	 * @return an instance of a DAO
	 */
	PersonDAO getPersonDAO();
	
	/**
	 * Get a dao for the tag entity
	 * @return
	 */
	TagDAO getTagDAO();
	
	/**
	 * Get a dao for communities
	 * 
	 * @return an instance of a DAO
	 */
	CommunityDAO getCommunityDAO();
	
	/**
	 * Get a dao for documents
	 * 
	 * @return an instance of a DAO
	 */
	DocumentDAO getDocumentDAO();
	
	/**
	 * Close DbContext 
	 * if transaction has not been commited or rollback'ed, a rollback is performed
	 */
	@Override
	void close() throws Exception;
}