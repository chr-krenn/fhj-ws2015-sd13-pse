package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;

import at.fhj.swd13.pse.db.entity.Document;

public interface DocumentService {

	/**
	 * Store the file into the archive on the server and persist the file information
	 * Multiple uploads of the same file are possible
	 * 
	 * @param filename name (absolute path) of the file to add to the storage
	 * 
	 * @param data file content
	 * 
	 * @return an instance of the newly created file or null when an error occured
	 */
	Document store(String filename, InputStream data);

}