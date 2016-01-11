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
	public void deleteEntry(int documentLibraryEntryId){
		documentLibraryService.deleteEntry(documentLibraryEntryId);
	}

	@Override
	public DocumentLibraryEntry getEntryById(int documentLibraryEntryId) {
		return documentLibraryService.getEntryById(documentLibraryEntryId);
	}

	@Override
	public void addEntry(String fileName, String description, byte[] content, int communityId) {
		documentLibraryService.addEntry(fileName, description, content, communityId);
	}

}
