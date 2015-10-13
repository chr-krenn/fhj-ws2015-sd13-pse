package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.nio.file.InvalidPathException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;

import at.fhj.swd13.pse.domain.document.DocumentService;

@ManagedBean
public class FileUploadView {

	@Inject
	private Logger logger;

	@Inject
	private DocumentService documentService;

	public void handleFileUpload(FileUploadEvent event) {

		logger.info("[UPLOAD] storing: " + event.getFile().getFileName());

		try {

			documentService.store(event.getFile().getFileName(), event.getFile().getInputstream());

			FacesMessage message = new FacesMessage("In Ordnung", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

			logger.info("[UPLOAD] file has been uploaded: " + event.getFile().getFileName());
		} catch (IOException | InvalidPathException x) {

			logger.error("[UPLOAD] error uploading: " + x.getMessage());

			FacesMessage message = new FacesMessage("Fehler", "Fehler beim hochladen von " + event.getFile().getFileName());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
}
