package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.DocumentLibraryEntry;

public interface DocumentLibraryEntryDAO {

	void insert(DocumentLibraryEntry entry) throws ConstraintViolationException;
	
	void remove(int documentLibraryEntryId) throws EntityNotFoundException;
	
	List<DocumentLibraryEntry> getAllForCommunity(int communityId); 
	
	DocumentLibraryEntry getEntryById(int documentEntryId);
}
