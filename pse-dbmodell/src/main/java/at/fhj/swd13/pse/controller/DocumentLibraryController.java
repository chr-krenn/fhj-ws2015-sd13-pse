package at.fhj.swd13.pse.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
	
	public void uploadFileDocument(FileUploadEvent fileUploadEvent)
	{
		uploadedFileName =  fileUploadEvent.getFile().getFileName();
		uploadedFileContent = fileUploadEvent.getFile().getContents();
	}
	
	public void saveNewDocument()
	{
		InputStream is = new ByteArrayInputStream(uploadedFileContent);
		
		documentLibraryService.addEntry(uploadedFileName, newDocumentDescription, is, communityId);
		
		uploadedFileContent = null;
		uploadedFileName = null;
		newDocumentDescription = null;
	}
	
	
	public List<DocumentLibraryEntry> getEntries()
	{
		return documentLibraryService.getEntriesForCommunity(communityId);
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
}
