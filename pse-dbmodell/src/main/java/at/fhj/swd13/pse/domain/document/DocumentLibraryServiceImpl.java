package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.ConstraintViolationException;
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
	
	private DocumentLibraryEntry convert(at.fhj.swd13.pse.db.entity.DocumentLibraryEntry other)
	{
		String serverPath = documentService.getServerPath(other.getDocument());
		
		return new DocumentLibraryEntry(other.getDocumentLibraryEntryId(), other.getDocument().getName(), 
				other.getDocument().getDescription(), other.getDocument().getCreatedAt().toString(), 
				other.getDocument().getDocumentId(),serverPath, other.getDocument().getMimeType());	
	}
	
	
	public List<DocumentLibraryEntry> getEntriesForCommunity(int communityId)
	{
		List<at.fhj.swd13.pse.db.entity.DocumentLibraryEntry> entries = documentLibraryEntryDAO.getAllForCommunity(communityId);
		return entries.stream().map(x -> convert(x)).collect(Collectors.toList());
	}

	@Override
	public void addEntry(String fileName, String description, InputStream content, int communityId) throws ConstraintViolationException {
		
		Community community = communityDAO.get(communityId);
		
		Document document = documentService.store(fileName, content, description);
		
		at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry = new at.fhj.swd13.pse.db.entity.DocumentLibraryEntry();
		
		entry.setCommunity(community);
		entry.setDocument(document);
		
		documentLibraryEntryDAO.insert(entry);
	}

	@Override
	public void deleteEntry(int documentLibraryEntryId) throws DocumentNotFoundException {
		
		at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry;
		try {
			entry = documentLibraryEntryDAO.getById(documentLibraryEntryId);
		} catch (EntityNotFoundException e) {
			throw new DocumentNotFoundException("",e);
		}
		documentService.removeDocument(entry.getDocument().getDocumentId());
	}

	@Override
	public DocumentLibraryEntry getEntryById(int documentLibraryEntryId) throws DocumentNotFoundException {
		at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry;
		try {
			entry = documentLibraryEntryDAO.getById(documentLibraryEntryId);
			return convert(entry);
		} catch (EntityNotFoundException e) {
			throw new DocumentNotFoundException("",e);
		}
	}
}
