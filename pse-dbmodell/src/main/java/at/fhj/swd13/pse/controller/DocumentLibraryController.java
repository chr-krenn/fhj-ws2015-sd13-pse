package at.fhj.swd13.pse.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import at.fhj.swd13.pse.db.EntityNotFoundException;
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
	private Logger logger;

	@Inject
	private DocumentLibraryRightsProviderFactory documentLibraryRightsProviderFactory;

	private DocumentLibraryRightsProvider documentLibraryRightsProvider;

	public DocumentLibraryController() {

	}

	public int getCommunityId() {
		return communityId;
	}

	public void addNewDocument() {
		setIsInNewDocumentMode(true);
	}

	public void uploadFileDocument(FileUploadEvent fileUploadEvent) {
		setUploadedFileName(fileUploadEvent.getFile().getFileName());
		uploadedFileContent = fileUploadEvent.getFile().getContents();
	}

	public void saveNewDocument() {
		try {

			//Check manually whether an file has been uploaded earlier
			//Primefaces validation is not applicable in this scenario 
			if (uploadedFileContent == null) {
				addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Es wurde keine Datei ausgewählt."));
				return;
			}

			InputStream is = new ByteArrayInputStream(uploadedFileContent);
			documentLibraryService.addEntry(getUploadedFileName(), newDocumentDescription, is, communityId);

			uploadedFileContent = null;
			setUploadedFileName(null);
			newDocumentDescription = null;
			setIsInNewDocumentMode(false);

		} catch (Exception e) {
			logger.error("[DocumentLibrary] Failed to add new document.",e);
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", getStringResource("UnknownErrorMessage")));
		}
	}
	
	public void deleteDocument(int id)
	{
		try {
			documentLibraryService.deleteEntry(id);
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", getStringResource("DeletedDocumentEntrySuccessfully")));
		} catch (EntityNotFoundException e) {
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", String.format(getStringResource("DocumentLibraryEntryNotFound"), id)));
		} catch(Exception ex)
		{
			logger.error(String.format("[DocumentLibrary] Failed to delete document with id %s.", id),ex);
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", getStringResource("UnknownErrorMessage")));
		}
	}
	
	public List<DocumentLibraryEntry> getEntries() {
		return documentLibraryService.getEntriesForCommunity(communityId);
	}

	public StreamedContent downloadDocument(int id) {
		try {
			DocumentLibraryEntry entry = documentLibraryService.getEntryById(id);
			InputStream stream = new FileInputStream(entry.getServerPath());
			return new DefaultStreamedContent(stream, entry.getContentType(), entry.getName());
		} catch (FileNotFoundException e) {
			// TODO create error message
			return null;
		}
	}

	public boolean getCanViewLibrary() {
		return documentLibraryRightsProvider.canViewLibrary();
	}

	public boolean getCanEditLibrary() {
		return documentLibraryRightsProvider.canEditLibrary();
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public void init() {
		// If no community id has been provided, use the default community
		if (communityId == 0)
			communityId = 1;

		documentLibraryRightsProvider = documentLibraryRightsProviderFactory.create(communityId);
	}
	
	private void addFacesMessage(FacesMessage message)
	{
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	private String getStringResource(String key)
	{
		FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "Resources");
		
        return bundle.getString(key);
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
