package at.fhj.swd13.pse.domain.document;

import java.util.List;

public interface DocumentLibraryService {

	List<DocumentLibraryEntry> getEntriesForCommunity(int communityId);
}
