package at.fhj.swd13.pse.db.dao;

import at.fhj.swd13.pse.db.entity.DocumentLibraryEntry;

public interface DocumentLibraryEntryDAO {

	void insert(DocumentLibraryEntry entry);
	
	void remove(DocumentLibraryEntry entry);
	
	void remove(int documentLibraryEntryId);
}
