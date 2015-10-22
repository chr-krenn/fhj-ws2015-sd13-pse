package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;
import java.util.List;

import at.fhj.swd13.pse.db.EntityNotFoundException;

public interface DocumentLibraryService {

	List<DocumentLibraryEntry> getEntriesForCommunity(int communityId);
	
	void addEntry(String fileName, String descrition, InputStream content, int communityId); 
	
	void deleteEntry(int documentLibraryEntryId) throws EntityNotFoundException;
}
