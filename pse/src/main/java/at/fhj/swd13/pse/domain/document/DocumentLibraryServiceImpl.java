package at.fhj.swd13.pse.domain.document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.DocumentLibraryEntryDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.plumbing.ArgumentChecker;

@Stateless
public class DocumentLibraryServiceImpl implements DocumentLibraryService {

	private DocumentService documentService;
	private DocumentLibraryEntryDAO documentLibraryEntryDAO;
	private CommunityDAO communityDAO;

	@Inject
	public DocumentLibraryServiceImpl(DocumentService documentService, DocumentLibraryEntryDAO documentLibraryEntryDAO, CommunityDAO communityDAO) {
		this.documentService = documentService;
		this.documentLibraryEntryDAO = documentLibraryEntryDAO;
		this.communityDAO = communityDAO;
	}

	private DocumentLibraryEntry convert(at.fhj.swd13.pse.db.entity.DocumentLibraryEntry other) {
		try {
			ArgumentChecker.assertNotNull(other, "other");

			String serverPath = documentService.getServerPath(other.getDocument());

			return new DocumentLibraryEntry(other.getDocumentLibraryEntryId(), other.getDocument().getName(), other.getDocument().getDescription(),
					other.getDocument().getCreatedAt().toString(), other.getDocument().getDocumentId(), serverPath, other.getDocument().getMimeType());
		} catch (Throwable e) {
			throw new ServiceException("[DOCS] internal error converting: " + other.getDocumentLibraryEntryId(), e);
		}
	}

	public List<DocumentLibraryEntry> getEntriesForCommunity(int communityId) {
		try {
			List<at.fhj.swd13.pse.db.entity.DocumentLibraryEntry> entries = documentLibraryEntryDAO.getAllForCommunity(communityId);
			return entries.stream().map(x -> convert(x)).collect(Collectors.toList());
		} catch (Throwable e) {
			throw new ServiceException("[DOCS] internal error getting entries for community: " + communityId, e);
		}
	}

	@Override
	public void addEntry(String fileName, String description, InputStream content, int communityId) {

		try {
			Community community = communityDAO.get(communityId);

			Document document = documentService.store(fileName, content, description);

			at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry = new at.fhj.swd13.pse.db.entity.DocumentLibraryEntry(community, document);

			documentLibraryEntryDAO.insert(entry);
		} catch (Throwable e) {
			throw new ServiceException("[DOCS] internal error adding entry: " + fileName, e);
		}
	}

	@Override
	public void deleteEntry(int documentLibraryEntryId) throws ServiceException {

		try {
			at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry;
			entry = documentLibraryEntryDAO.getById(documentLibraryEntryId);
			documentService.removeDocument(entry.getDocument().getDocumentId());
		} catch (EntityNotFoundException e) {
			throw new ServiceException("[DOCS] document not found: " + documentLibraryEntryId, e);
		} catch (Throwable e) {
			throw new ServiceException("[DOCS] internal error", e);
		}
	}

	@Override
	public DocumentLibraryEntry getEntryById(int documentLibraryEntryId) {
		try {
			at.fhj.swd13.pse.db.entity.DocumentLibraryEntry entry;
			entry = documentLibraryEntryDAO.getById(documentLibraryEntryId);
			return convert(entry);
		} catch (EntityNotFoundException e) {
			throw new ServiceException("[DOCS] error getting entry" + documentLibraryEntryId, e);
		} catch (Throwable e) {
			throw new ServiceException("[DOCS] internal error", e);
		}
	}

	@Override
	public void addEntry(String fileName, String description, byte[] content, int communityId) {
		InputStream inputStream = new ByteArrayInputStream(content);
		addEntry(fileName, description, inputStream, communityId);
	}
}
