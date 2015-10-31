package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.chat.ChatService;
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
	private ChatService chatService;

	@Inject
	private Logger logger;

	private UserDTO userDTO;

	private String editMode;
	private String userName;

	private String communityName = "";

	private List<UserDTO> usersWithDepartment = new ArrayList<UserDTO>();

	@PostConstruct
	public void setup() {
		userName = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("userName");
		editMode = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("mode");
		getPerson();
	}
	
	private void getPerson() {
		try {
			Person person = userService.getUser(userName);
			userDTO = userDTOBuilder.createFrom(person);
			setUsersWithDepartment(userService.getUsersWithDepartment(person
					.getDepartment()));
		} catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[USERPROFILE] user not found "
					+ userSession.getUsername() + " from " + context.toString());
		}
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	/**
	 * returns the name of the current login user
	 *
	 * @return usename
	 * 
	 */
	public String getUserName() {
		return userName;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;

		try {
			Document document = documentService.store(file.getFileName(),
					file.getInputstream());
			int documentid = document.getDocumentId();
			userService.setUserImage(getUserDTO().getUserName(),
					document.getDocumentId());
			getUserDTO().setImageRef(
					documentService.buildServiceUrl(documentid));
		} catch (IOException e) {
			logger.info("[USERPROFILE] handleFileUpload failed for "
					+ file.getFileName() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"File-Upload Fehler", "File Upload fehlgeschlagen");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (RuntimeException e) {
			logger.info("[USERPROFILE] handleFileUpload failed for "
					+ file.getFileName() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"File-Upload Fehler", "File Upload fehlgeschlagen");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (EntityNotFoundException e) {
			logger.info("[USERPROFILE] handleFileUpload failed for "
					+ file.getFileName() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"File-Upload Fehler",
					"File Upload fehlgeschlagen, Benutzer ungültig");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String getModeEditAdminDisplay() {
		boolean modeEdit = isModeEdit() || isAdmin();

		String display = modeEdit == false ? "display:none" : "display:all";

		return display;
	}

	public void updateProfile() {
		try {
			// add tag if not already existing
			if (getUserDTO().getTags() != null) {
				for (String token : getUserDTO().getTags()) {
					Tag tag = tagService.getTagByToken(token);
					if (tag == null) {
						tag = new Tag();
						tag.setToken(token);
						tag.setDescription(token);
						try {
							tagService.insert(tag);
						} catch (ConstraintViolationException x) {
							logger.error("[USERPROFILE] error creating new tag (duplicate...)");
						}
					}
				}
			}

			userService.update(userDTO);

		} catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[USERPROFILE] updateProfile failed for "
					+ userDTO.getFullname() + " from " + context.toString());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Aktualisiserung Fehler",
					"Aktualisiserung für Benutzer fehlgeschlagen, ungültiger Username");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		logger.info("[USERPROFILE] updateProfile successful for "
				+ userDTO.getFullname() + " from "
				+ RequestContext.getCurrentInstance().toString());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Aktualisiserung erfolgreich", "");
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	/**
	 * returns true if the current login user is administrator
	 *
	 * @return true / false
	 * 
	 */
	public boolean isAdmin() {
		return userSession.isAdmin();
	}

	/**
	 * returns true if the current page is opened in login mode
	 *
	 * @return true if the current page is opened in login mode
	 * 
	 */
	public boolean isModeEdit() {
		return editMode != null && editMode.equals("edit");
	}

	/**
	 * returns true if the current user is the login user
	 *
	 * @return true / false
	 * 
	 */
	private boolean isLoggedInUser() {
		return userName != null && userName.equals(userSession.getUsername());
	}

	public List<String> completeTag(String input) {
		List<String> result = new ArrayList<String>();
		logger.info("[USERPROFILE] completeTag - selcted tag count "
				+ userDTO.getTags().size());

		for (Tag tag : tagService.getMatchingTags(input)) {
			if (!isTagAlreadySelected(tag.getToken())) {
				result.add(tag.getToken());
			}
		}

		if (result.isEmpty()) {
			result.add(input);
		}

		return result;
	}

	private boolean isTagAlreadySelected(final String token) {
		final String needle = token.toLowerCase();
		for (String tag : userDTO.getTags()) {
			if (tag.toLowerCase().equals(needle)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * called when a tag is added to the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagSelect(SelectEvent event) {

		logger.info("[USERPROFILE] handleSelect");
	}

	/**
	 * called when a tag is removed from the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagUnselect(UnselectEvent event) {

		logger.info("[USERPROFILE] handleUnselect");
	}

	public String getTagEditStyle() {
		return (!isLoggedInUser() && !isAdmin()) || !isModeEdit() ? "display:none" : "display:all";
	}

	public String getTagDisplayStyle() {
		return getTagEditStyle().equals("display:none") ? "display:all" : "display:none";
	}
	

	/**
	 * returns the display representation for tags
	 *
	 * @return display representation for tags
	 * 
	 */
	public String getTagDisplayString() {
		StringBuffer result = new StringBuffer();
		for (String tag : getUserDTO().getTags()) {
			if (result.length() > 0)
				result.append(" ");
			result.append(tag);
		}
		return result.toString();
	}

	public boolean addToContactVisible() {
		return !isLoggedInUser();
	}

	public boolean canSendMessage() {
		if (userDTO.getContacts().contains(userService.getLoggedInUser())) {
			return true;
		}
		return false;
	}

	public String contactButtonText() {
		if (userDTO.getContacts().contains(userService.getLoggedInUser())) {
			return "Aus meinen Kontakten entfernen";
		} else {
			return "Zu meinen Kontakten hinzufügen";
		}
	}

	public String sendMessageButtonText() {
		return "Nachricht senden";
	}

	public String sendMessageButtonAction() {
		List<Community> communities = userDTO.getCommunities();
		Optional<Community> userCommunity = communities
				.stream()
				.filter(c -> c.getPrivateUser() != null
						&& c.getPrivateUser().getUserName().equals(userName)).findAny();

		if (userCommunity.isPresent()) {
			return "/protected/xperimental/AddMessage.jsf?community="
					+ userCommunity.get().getName() + "&faces-redirect=true";
		}
		return "/protected/xperimental/AddMessage.jsf?faces-redirect=true";
	}

	public void contactButtonAction() {

		try {
			if (userDTO.getContacts().contains(userService.getLoggedInUser())) {

				userService.removeRelation(userService.getLoggedInUser(),
						userService.getUser(userDTO.getUserName()));

				// Update userDTO
				userDTO.getContacts().remove(userService.getLoggedInUser());

			} else {
				userService.createRelation(userService.getLoggedInUser(),
						userService.getUser(userDTO.getUserName()));

				// Update userDTO
				userDTO.getContacts().add(userService.getLoggedInUser());
			}
		} catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[USERPROFILE] contactButtonAction failed for "
					+ userDTO.getFullname() + " from " + context.toString());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Kontakt hinzufügen Fehler",
					"Kontakt konnte nicht hinzugefügt werden, ungültiger Username");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (RuntimeException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[USERPROFILE] contactButtonAction failed for "
					+ userDTO.getFullname() + " from " + context.toString());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Kontakt hinzufügen Fehler", "Kontakt konnte nicht hinzugefügt werden");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public boolean isActiveFlagEnabled() {
		return (!isLoggedInUser() && isAdmin());
	}

	public boolean isLoginAllowedFlagEnabled() {
		return (!isLoggedInUser() && isAdmin());
	}

	public boolean isExternFlagEnabled() {
		return isModeEdit() || isAdmin();
	}

	public List<UserDTO> getUsersWithDepartment() {
		return usersWithDepartment;
	}

	public void setUsersWithDepartment(List<Person> usersWithDepartment) {
		for (Person p : usersWithDepartment)
			this.usersWithDepartment.add(userDTOBuilder.createFrom(p));
	}
	
	public String getContactListEntryClass(String username, String department) {
		if (!username.equals(userSession.getUsername()))
			return "";

		Person person = null;
		try {
			person = userService.getUser(userSession.getUsername());
		} catch (EntityNotFoundException e) {
			return "";
		}

		if (department.equals(person.getDepartment()))
			return "samedepartment";
		else
			return "";
	}
	
	/**
	 * creates  a new Community for the user logged in
	 */	
	public void createCommunity() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message;
		
		try {
			chatService.createChatCommunity(userSession.getUsername(), getCommunityName(), true);
			setCommunityName("");
			getPerson();
		} catch (EntityNotFoundException e) {
			logger.info("[USERPROFILE] createCommunity failed for "
					+ userDTO.getFullname() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Community anlegen Fehler",
					"Community anlegen fehlgeschlagen, ungültiger Username");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (RuntimeException e) {
			logger.info("[USERPROFILE] createCommunity failed for "
					+ userDTO.getFullname() + " from " + context.toString());
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Community anlegen Fehler", "Community anlegen fehlgeschlagen");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/**
	 * returns a List of community where the current user is member of
	 *
	 * @return the Community List
	 * 
	 */	
	public List<CommunityMember> getCommunityMemberships() {
		List<CommunityMember> communityMemberships = getUserDTO().getCommunityMemberships();
		Iterator<CommunityMember> membershipIterator = communityMemberships.iterator();
		
		while (membershipIterator.hasNext()) {
			CommunityMember membership = membershipIterator.next();
			if (membership.getCommunity().getPrivateUser() != null && 
				membership.getCommunity().getPrivateUser().getPersonId() == userDTO.getId()) {
				membershipIterator.remove();	
			}
		}
		
		return communityMemberships;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
}
