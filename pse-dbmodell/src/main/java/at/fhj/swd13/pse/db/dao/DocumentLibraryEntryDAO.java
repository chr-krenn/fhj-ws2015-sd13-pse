package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.DocumentLibraryEntry;

public interface DocumentLibraryEntryDAO {

	void insert(DocumentLibraryEntry entry);
	
	void remove(int documentLibraryEntryId) throws EntityNotFoundException;
	
	List<DocumentLibraryEntry> getAllForCommunity(int communityId); 
}
