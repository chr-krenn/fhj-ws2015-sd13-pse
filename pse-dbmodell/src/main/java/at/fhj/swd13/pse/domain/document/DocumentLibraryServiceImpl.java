package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.DocumentLibraryEntryDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;

@Stateless
public class DocumentLibraryServiceImpl implements DocumentLibraryService {

	private DocumentService documentService;
	private DocumentLibraryEntryDAO documentLibraryEntryDAO;
	private CommunityDAO communityDAO;

	@Inject
	public DocumentLibraryServiceImpl(DocumentService documentService, 
								      DocumentLibraryEntryDAO documentLibraryEntryDAO,
								      CommunityDAO communityDAO) {
		this.documentService = documentService;
		this.documentLibraryEntryDAO = documentLibraryEntryDAO;
		this.communityDAO = communityDAO;		
	}
	
	public List<DocumentLibraryEntry> getEntriesForCommunity(int communityId)
	{
		List<at.fhj.swd13.pse.db.entity.DocumentLibraryEntry> entries = documentLibraryEntryDAO.getAllForCommunity(communityId);
		
		
		return entries.stream().map(x -> new DocumentLibraryEntry(x.getDocumentLibraryEntryId(), x.getDocument().getName(), 
														   x.getDocument().getDescription(), x.getDocument().getCreatedAt().toString(), 
														   x.getDocument().getDocumentId()))
						.collect(Collectors.toList());
	}

	@Override
	public void addEntry(String fileName, String description, InputStream content, int communityId) {
		
		Community community = communityDAO.get(communityId);
		
		Document document = documentService.store(fileName, content, description);
		
		at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry = new at.fhj.swd13.pse.db.entity.DocumentLibraryEntry();
		
		entry.setCommunity(community);
		entry.setDocument(document);
		
		documentLibraryEntryDAO.insert(entry);
	}

	@Override
	public void deleteEntry(int documentLibraryEntryId) throws EntityNotFoundException {
		documentLibraryEntryDAO.remove(documentLibraryEntryId);
	}
}
