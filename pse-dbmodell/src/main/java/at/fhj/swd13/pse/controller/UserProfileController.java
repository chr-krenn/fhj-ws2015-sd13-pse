package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonTag;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.UserDTO;
import at.fhj.swd13.pse.dto.UserDTOBuilder;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
@ViewScoped
public class UserProfileController implements Serializable {

	private static final long serialVersionUID = -984282742840189477L;

	@Inject
	private UserService userService;

	@Inject
	private TagService tagService;
	
	@Inject
	private UserSession userSession;

	@Inject
	private DocumentService documentService;

	@Inject
	private UserDTOBuilder userDTOBuilder;

	@Inject
	private Logger logger;

	private UserDTO userDTO;
	
	private List<UserDTO> usersWithDepartment = new ArrayList<UserDTO>();

	@PostConstruct
	public void setup() {
		try {
			String userName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userName");
			Person person = userService.getUser(userName);
			userDTO = userDTOBuilder.createFrom(person);
			setUsersWithDepartment(userService.getUsersWithDepartment(person.getDepartment()));
		} catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[USERPROFILE] user not found " + userSession.getUsername() + " from " + context.toString());
		}
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;

		try {
			Document document = documentService.store(file.getFileName(), file.getInputstream());
			int documentid = document.getDocumentId();
			userService.setUserImage(getUserDTO().getUserName(), document.getDocumentId());
			getUserDTO().setImageRef(documentService.buildServiceUrl(documentid));
		} catch (IOException e) {
			logger.info("[USERPROFILE] handleFileUpload failed for " + file.getFileName() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "File-Upload Fehler", "File Upload fehlgeschlagen");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (RuntimeException e) {
			logger.info("[USERPROFILE] handleFileUpload failed for " + file.getFileName() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "File-Upload Fehler", "File Upload fehlgeschlagen");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (EntityNotFoundException e) {
			logger.info("[USERPROFILE] handleFileUpload failed for " + file.getFileName() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "File-Upload Fehler", "File Upload fehlgeschlagen, Benutzer ungültig");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String getModeEditAdminDisplay() {
		boolean modeEdit = isModeEdit() || isAdmin();

		String display = modeEdit == false ? "display:none" : "display:all";

		return display;
	}

	public void updateProfile() {
		System.out.println("updatee asdfsed");
		try {
			userService.update(userDTO);
		} catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[USERPROFILE] updateProfile failed for " + userDTO.getFullname() + " from " + context.toString());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aktualisiserung Fehler", "Aktualisiserung für Benutzer fehlgeschlagen, ungültiger Username");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		
		logger.info("[USERPROFILE] updateProfile successful for " + userDTO.getFullname() + " from " + RequestContext.getCurrentInstance().toString());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aktualisiserung erfolgreich", "");
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
	public boolean isAdmin() {
		return userSession.isAdmin();
	}
	
	private boolean isModeEdit() {
		String mode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mode");
		return ((mode != null) && (mode.equals("edit")));		
	}
	
	private boolean isLoggedInUser() {
		String userName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userName");
		return ((userName != null) && (userName.equals(userSession.getUsername())));		
	}
	
	public void addNewTag() {
		if (getTags().size() > 0) {
			Person person;
			try {
				person = userService.getUser(userDTO.getUserName());
	
				PersonTag tag = new PersonTag();
				tag.setPerson(person);
				tag.setTag(getTags().get(0));
				getUserDTO().getTags().add(tag);
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	
	public List<Tag> getTags() {
		return tagService.getMatchingTags("");
    }
	
	public boolean addToContactVisible() {
		return !isLoggedInUser();
	}

	public String contactButtonText() {
		if (userDTO.getContacts().contains(userService.getLoggedInUser())) {
			return "Aus meinen Kontakten entfernen";
		} else {
			return "Zu meinen Kontakten hinzufügen";
		}
	}

	public void contactButtonAction() {
		
		try {
			if (userDTO.getContacts().contains(userService.getLoggedInUser())) {
				
				userService.removeRelation(userService.getLoggedInUser(), userService.getUser(userDTO.getUserName()));
				
				//Update userDTO
				userDTO.getContacts().remove(userService.getLoggedInUser());
				
			} else {
				System.out.println(userService.getLoggedInUser().getContacts().size());
				userService.createRelation(userService.getLoggedInUser(), userService.getUser(userDTO.getUserName()));
				
				//Update userDTO
				userDTO.getContacts().add(userService.getLoggedInUser());
			}
			
			
			
		} catch (EntityNotFoundException e) {

		}
	}
	
	public boolean activeVisible() {
		return (!isLoggedInUser() && isAdmin());
	}
	
	public boolean loginAllowedVisible() {
		return (!isLoggedInUser() && isAdmin());
	}
	
	public boolean externEnabled() {
		return isModeEdit();
	}

	public List<UserDTO> getUsersWithDepartment() {
		return usersWithDepartment;
	}

	public void setUsersWithDepartment(List<Person> usersWithDepartment) {
		for (Person p : usersWithDepartment)
			this.usersWithDepartment.add(userDTOBuilder.createFrom(p));
	}
}
