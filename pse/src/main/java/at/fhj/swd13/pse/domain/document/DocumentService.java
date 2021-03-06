package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;

import at.fhj.swd13.pse.db.entity.Document;

public interface DocumentService {

	enum DocumentCategory {
		MESSAGE_ICON, USER_IMAGE
	};

	/**
	 * Store the file into the archive on the server and persist the file
	 * information Multiple uploads of the same file are possible
	 * 
	 * @param filename
	 *            name (absolute path) of the file to add to the storage
	 * 
	 * @param data
	 *            file content
	 * 
	 * @return an instance of the newly created file or null when an error
	 *         occured
	 */
	Document store(String filename, InputStream data);
	
	
	
	Document store(String filename, InputStream data, String description);
	
	/**
	 * For remote access, InputStream must be opened in store Method
	 * 
	 * @param filename
	 * @param filepath
	 * @return
	 */
	Document store(String filename, String filepath);
	
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
	 * @param d
	 *            the document
	 * 
	 * @return full path to the document
	 */
	String getServerPath(final Document d);

	/**
	 * Get the full app-relative URL to the stored image
	 * 
	 * @param filename
	 *            filename part of the url to build
	 * 
	 * @return app-relative path to the given image file
	 */
	String buildImageUrl(final String filename);

	/**
	 * Get the full ap-relative URL to the stored document (stored in dynamic
	 * storage)
	 * 
	 * @param documentId
	 *            the documentId which to refer
	 * 
	 * @return full app-relative path of to the given documentId
	 */
	String buildServiceUrl(final int documentId);

	/**
	 * Get the default image for a given usage
	 * 
	 * @param documentCategory
	 *            the intended usage
	 * 
	 * @return full app-relative path to the image
	 */
	String getDefaultDocumentRef(DocumentCategory documentCategory);
	
	/**
	 * check if all needed folders exist in the file system
	 * if not, try to create them
	 */
	void assertDocumentFolders();
	
	/**
	 * Removes a document from the database
	 * 
	 * @param documentId
	 * 
	 */
	void removeDocument(final int documentId);
	
	/**
	 * Provides a file stream of the file which represented by a document reference through documentId
	 * 
	 * @param documentId
	 * 
	 */
	InputStream getStreamForDocument(final int documentId);
	
	
}