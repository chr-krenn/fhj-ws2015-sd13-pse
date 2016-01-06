package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(DocumentLibraryService.class)
public class DocumentLibraryServiceFacade implements DocumentLibraryService {

	@EJB(beanName="DocumentLibraryServiceImpl")
	private DocumentLibraryService documentLibraryService;
	
	@Override
	public List<DocumentLibraryEntry> getEntriesForCommunity(int communityId) {
		return documentLibraryService.getEntriesForCommunity(communityId);
	}

	@Override
	public void addEntry(String fileName, String descrition, InputStream content, int communityId) {
		documentLibraryService.addEntry(fileName, descrition, content, communityId);
	}

	@Override
	public void deleteEntry(int documentLibraryEntryId) throws DocumentNotFoundException {
		documentLibraryService.deleteEntry(documentLibraryEntryId);
	}

	@Override
	public DocumentLibraryEntry getEntryById(int documentLibraryEntryId) throws DocumentNotFoundException {
		return documentLibraryService.getEntryById(documentLibraryEntryId);
	}

}
