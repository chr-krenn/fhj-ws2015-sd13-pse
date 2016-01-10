package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.document.DocumentServiceFacade;
import at.fhj.swd13.pse.domain.document.DocumentService.DocumentCategory;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class DocumentServiceIT extends RemoteTestBase {
	
	private static String serviceUrl = "/store/media/";
	private static String imageFolderUrl = "/protected/img/";
	private static String imageFolder = "/tmp/pse/documents";
	
	private static DocumentService documentService;
	
	@Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
    	documentService = lookup(DocumentServiceFacade.class, DocumentService.class);
    }	
	
	@Test
	public void store(){
		String documentName = "testdoc";
		
		Document doc = documentService.store(documentName, getClass().getResource("/testDocs/no_img.png").getFile());
		assertEquals(documentName, doc.getName());
		
		Document doc2 = documentService.get(doc.getDocumentId());
		assertNotNull(doc2);
		
		documentService.removeDocument(doc.getDocumentId());
	}
	
	@Test
	public void get(){
		String documentName = "testdoc";
		Document doc = documentService.store(documentName, getClass().getResource("/testDocs/no_img.png").getFile());
		
		Document doc2 = documentService.get(doc.getDocumentId());
		assertEquals(doc, doc2);
		
		documentService.removeDocument(doc.getDocumentId());
	}
	
	@Test
	public void removeDocument(){
		String documentName = "testdoc";
		Document doc = documentService.store(documentName, getClass().getResource("/testDocs/no_img.png").getFile());
		
		Document doc2 = documentService.get(doc.getDocumentId());
		assertNotNull(doc2);
		
		documentService.removeDocument(doc.getDocumentId());
		
		Document doc3 = documentService.get(doc.getDocumentId());
		assertNull(doc3);
	}
	
	@Test
	public void getServerPath(){
		Document doc = documentService.store("test", getClass().getResource("/testDocs/no_img.png").getFile());
		String expected = Paths.get(imageFolder, doc.getStorageLocation()).toString();
		
		assertEquals(expected, documentService.getServerPath(doc));
		
		documentService.removeDocument(doc.getDocumentId());
	}
	
	@Test
	public void buildImageUrl(){
		String fileName = "testFileName";
		String expected = imageFolderUrl + fileName;
		assertEquals(expected, documentService.buildImageUrl(fileName));
	}
	
	@Test
	public void buildServiceUrl(){
		int documentId = 17;
		String expected = serviceUrl + Integer.toString(documentId);
		assertEquals(expected, documentService.buildServiceUrl(documentId));
	}
	
	@Test
	public void getDefaultDocumentRef(){
		assertEquals(imageFolderUrl + "default_message_icon.jpg", documentService.getDefaultDocumentRef(DocumentCategory.MESSAGE_ICON));
		assertEquals(imageFolderUrl + "default_user_image.jpg", documentService.getDefaultDocumentRef(DocumentCategory.USER_IMAGE));
	}
	
	/*@Test
	public void getStreamForDocument() throws FileNotFoundException{
		String documentName = "testdoc";
		Document doc = documentService.store(documentName, getClass().getResource("/testDocs/no_img.png").getFile());
		InputStream expected = (InputStream)new FileInputStream(Paths.get(imageFolder, doc.getStorageLocation()).toString());
		InputStream actual = documentService.getStreamForDocument(doc.getDocumentId()); 
		assertEquals(expected, actual);
		documentService.removeDocument(doc.getDocumentId());
	}*/
}
