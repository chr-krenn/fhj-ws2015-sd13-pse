package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.InvalidPathException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.UserDTO;

@ManagedBean
@ViewScoped
public class UserView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UserService userService;
	
	@Inject
	private Logger logger;

	@Inject
	private DocumentService documentService;

	public UserDTO getUserDTO() {
		String userName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userName");
		return new UserDTO(userService.getUser(userName));
	}
	
	public void handleFileUpload(FileUploadEvent event) {

		logger.info("[UPLOAD] storing: " + event.getFile().getFileName());

		try {

			Document document = documentService.store(event.getFile().getFileName(), event.getFile().getInputstream());

			FacesMessage message = new FacesMessage("In Ordnung", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

			logger.info("[UPLOAD] file has been uploaded: " + event.getFile().getFileName());
			
			userService.setDocument(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userName"), document);

			
			
		} catch (IOException | InvalidPathException | EntityNotFoundException x) {

			logger.error("[UPLOAD] error uploading: " + x.getMessage());

			FacesMessage message = new FacesMessage("Fehler", "Fehler beim hochladen von " + event.getFile().getFileName());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
}
