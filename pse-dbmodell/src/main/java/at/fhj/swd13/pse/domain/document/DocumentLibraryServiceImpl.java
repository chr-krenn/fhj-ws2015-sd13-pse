package at.fhj.swd13.pse.domain.document;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;


@Stateless
public class DocumentLibraryServiceImpl implements DocumentLibraryService {

	public List<DocumentLibraryEntry> getEntriesForCommunity(int communityId)
	{
		ArrayList<DocumentLibraryEntry> tmp = new ArrayList<DocumentLibraryEntry>();
		
		tmp.add(new DocumentLibraryEntry(1, "Test", "Eine Datei", "10.10.2015 10:10:10", 1));
		
		return tmp;
	}
}
