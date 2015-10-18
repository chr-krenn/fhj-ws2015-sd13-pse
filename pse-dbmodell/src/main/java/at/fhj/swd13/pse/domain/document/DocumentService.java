package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;

import at.fhj.swd13.pse.db.entity.Document;

public interface DocumentService {
	// TODO move to configuration
	public static final String imageFolder = "/tmp/pse/documents";

	
	/**
	 * Store the file into the archive on the server and persist the file information
	 * Multiple uploads of the same file are possible
	 * 
	 * @param filename
	 *            name (absolute path) of the file to add to the storage
	 * 
	 * @param data
	 *            file content
	 * 
	 * @return an instance of the newly created file or null when an error occured
	 */
	Document store(String filename, InputStream data);

	/**
	 * Get the document with the given id
	 * 
	 * @param documentId
	 * 
	 * @return instance of the (detached) document or null if none found
	 */
	Document get(final int documentId);
	
	/**
	 * return the (current) absolut path to the stored document
	 * 
	 * @param d the document
	 * 
	 * @return full path to the document
	 */
	String getServerPath( final Document d );
}