package at.fhj.swd13.pse.db.dao;

import at.fhj.swd13.pse.db.entity.Document;

public interface DocumentDAO {

	/**
	 * insert the document into the persistent storage
	 * (in detail: queue for persistent storage) 
	 * 
	 * @param document the document to delete
	 */
	void insert(Document document);

	/**
	 * Remove the given document
	 * 
	 * @param document the document to remove
	 * 
	 * @throws ParameterException when the document was obviously not persisted before
	 */
	void remove(Document document);
	
	/**
	 * Remove the document with the given id
	 * 
	 * @param documentId id of the document to delete
	 */
	void remove( int documentId );
	
	/**
	 * Retrieve a document by the given id
	 * 
	 * @param documentId the id of the document to get the id from
	 * 
	 * @return the document or null if none was found
	 */
	Document getById(int documentId);
}