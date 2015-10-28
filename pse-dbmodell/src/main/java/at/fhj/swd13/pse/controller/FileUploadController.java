package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.nio.file.InvalidPathException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.domain.document.DocumentService;

@ManagedBean
@ViewScoped
public class FileUploadController {

	@Inject
	private Logger logger;

	@Inject
	private DocumentService documentService;

	private int lastDocumentId = 0;

	/**
	 * handles uploading of the document
	 * 
	 * @param event
	 */
	public void handleUpload(FileUploadEvent event) {

		logger.info("[UPLOAD] storing: " + event.getFile().getFileName());

		try {

			Document document = documentService.store(event.getFile().getFileName(), event.getFile().getInputstream());

			if (document != null) {

				lastDocumentId = document.getDocumentId();

				FacesMessage message = new FacesMessage("In Ordnung", event.getFile().getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

				logger.info("[UPLOAD] file has been uploaded: " + event.getFile().getFileName());
			} else {
				logger.error("[UPLOAD] error uploading file");

				FacesMessage message = new FacesMessage("Fehler",
						"Fehler beim hochladen von " + event.getFile().getFileName());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
			}

		} catch (IOException | InvalidPathException x) {
			logger.error("[UPLOAD] error uploading file");

			FacesMessage message = new FacesMessage("Fehler",
					"Fehler beim hochladen von " + event.getFile().getFileName());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void ok() {

		logger.info("[UPLOAD] returning with new document id " + lastDocumentId);
		RequestContext.getCurrentInstance().closeDialog(lastDocumentId);
	}

	public void cancel() {
		RequestContext.getCurrentInstance().closeDialog(0);
	}
}
