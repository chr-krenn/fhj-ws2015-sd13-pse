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

	public UserDTO getUserDTO() {
		String userName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userName");
		return new UserDTO(userService.getUser(userName));
	}
}
