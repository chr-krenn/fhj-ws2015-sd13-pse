package at.fhj.swd13.pse.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.domain.document.DocumentLibraryEntry;
import at.fhj.swd13.pse.domain.document.DocumentLibraryRightsProvider;
import at.fhj.swd13.pse.domain.document.DocumentLibraryRightsProviderFactory;
import at.fhj.swd13.pse.domain.document.DocumentLibraryService;

@ManagedBean
@ViewScoped
public class DocumentLibraryController {

	private int communityId;
	
	private String newDocumentDescription; 
	
	private byte[] uploadedFileContent;
	private String uploadedFileName;
	
	private Boolean isInNewDocumentMode = false;
	
	private UploadedFile uploadedFile; 
	
	
	@Inject
	private DocumentLibraryService documentLibraryService;
	
	@Inject
	private DocumentLibraryRightsProviderFactory documentLibraryRightsProviderFactory;
	
	private DocumentLibraryRightsProvider documentLibraryRightsProvider;

	public DocumentLibraryController() {
		
	}
	
	public int getCommunityId() {
		return communityId;
	}
	
	public void addNewDocument()
	{
		setIsInNewDocumentMode(true);
	}
	
	public void uploadFileDocument(FileUploadEvent fileUploadEvent)
	{
		setUploadedFileName(fileUploadEvent.getFile().getFileName());
		uploadedFileContent = fileUploadEvent.getFile().getContents();
	}
	
	public void saveNewDocument()
	{
		InputStream is = new ByteArrayInputStream(uploadedFileContent);
		
		try {
			documentLibraryService.addEntry(getUploadedFileName(), newDocumentDescription, is, communityId);
		} catch (ConstraintViolationException e) {
//FIXME: need to handle failure on document generation			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		uploadedFileContent = null;
		setUploadedFileName(null);
		newDocumentDescription = null;
		setIsInNewDocumentMode(false);
	}
	
	
	public List<DocumentLibraryEntry> getEntries()
	{
		return documentLibraryService.getEntriesForCommunity(communityId);
	}
	
	public StreamedContent downloadDocument(int id)
	{
		try {
			DocumentLibraryEntry entry = documentLibraryService.getEntryById(id);	
	        InputStream stream = new FileInputStream(entry.getServerPath());
	        return new DefaultStreamedContent(stream, entry.getContentType(), entry.getName());	
		} catch (FileNotFoundException e) {
			//TODO create error message
			return null;
		}
	}
	
	public boolean getCanViewLibrary()
	{
		return documentLibraryRightsProvider.canViewLibrary();
	}
	
	public boolean getCanEditLibrary()
	{
		return documentLibraryRightsProvider.canEditLibrary();
	}
	
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}
	
	public void init() {
		//If no community id has been provided, use the default community
		if(communityId == 0)
			communityId = 1;
		
		documentLibraryRightsProvider = documentLibraryRightsProviderFactory.create(communityId);
	}

	public String getNewDocumentDescription() {
		return newDocumentDescription;
	}

	public void setNewDocumentDescription(String newDocumentDescription) {
		this.newDocumentDescription = newDocumentDescription;
	}

	public Boolean getIsInNewDocumentMode() {
		return isInNewDocumentMode;
	}

	public void setIsInNewDocumentMode(Boolean isInNewDocumentMode) {
		this.isInNewDocumentMode = isInNewDocumentMode;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}
}
