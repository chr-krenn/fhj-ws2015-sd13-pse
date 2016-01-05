package at.fhj.swd13.pse.domain.document;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import at.fhj.swd13.pse.db.entity.Document;

@Stateless
@Remote(DocumentService.class)
public class DocumentServiceFacade implements DocumentService {

	@EJB(beanName="DocumentServiceImpl")
	private DocumentService documentService;
	
	@Override
	public Document store(String filename, InputStream data) {
		return documentService.store(filename, data);
	}

	@Override
	public Document store(String filename, InputStream data, String description) {
		return documentService.store(filename, data, description);
	}

	@Override
	public Document get(int documentId) {
		return documentService.get(documentId);
	}

	@Override
	public String getServerPath(Document d) {
		return documentService.getServerPath(d);
	}

	@Override
	public String buildImageUrl(String filename) {
		return documentService.buildImageUrl(filename);
	}

	@Override
	public String buildServiceUrl(int documentId) {
		return documentService.buildServiceUrl(documentId);
	}

	@Override
	public String getDefaultDocumentRef(DocumentCategory documentCategory) {
		return documentService.getDefaultDocumentRef(documentCategory);
	}

	@Override
	public void assertDocumentFolders() throws IOException {
		documentService.assertDocumentFolders();
	}

	@Override
	public void removeDocument(int documentId) {
		documentService.removeDocument(documentId);
	}

	@Override
	public InputStream getStreamForDocument(int documentId) throws DocumentNotFoundException {
		return documentService.getStreamForDocument(documentId);
	}

}
