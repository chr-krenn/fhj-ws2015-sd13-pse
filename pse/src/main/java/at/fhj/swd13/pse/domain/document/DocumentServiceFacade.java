package at.fhj.swd13.pse.domain.document;

import java.io.InputStream;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import at.fhj.swd13.pse.db.entity.Document;

@Alternative
@Stateless
@Remote(DocumentService.class)
public class DocumentServiceFacade implements DocumentService {

	@EJB(beanName="DocumentServiceImpl")
	private DocumentService documentService;
	
	@Override
	public Document store(String filename, String filepath) {
		return documentService.store(filename, filepath);
	}
	
	@Override
	public Document store(String filename, InputStream data) {
		throw new UnsupportedOperationException("InputStream is not serializable. Use the store(String, String) method instead");
	}

	@Override
	public Document store(String filename, InputStream data, String description) {
		throw new UnsupportedOperationException("InputStream is not serializable. Use the store(String, String) method instead");
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
	public void assertDocumentFolders(){
		documentService.assertDocumentFolders();
	}

	@Override
	public void removeDocument(int documentId) {
		documentService.removeDocument(documentId);
	}

	@Override
	public InputStream getStreamForDocument(int documentId){
		return documentService.getStreamForDocument(documentId);
	}

}
