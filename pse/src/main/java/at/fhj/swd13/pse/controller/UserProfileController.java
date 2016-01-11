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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonTag;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.domain.user.UserService;
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
	private ChatService chatService;

	private Person person;
	
	private String editMode;
	private String userName;

	private String communityName = "";
	
	private List<String> tags = new ArrayList<String>();

	@PostConstruct
	public void setup() {
		userName = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("userName");
		editMode = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("mode");
		try {
			getPersonData();
			
		} catch (Throwable e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Setup Fehler", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}
	
	private void getPersonData() {
		if (userName != null) {
			person = userService.getUser(userName);

			for (PersonTag p : person.getPersonTags())
				tags.add(p.getTag().getToken());
		}
	}
	
	private Person getLoggedInUser() {
		return userService.getUser(userSession.getUsername());
	}
	
	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}
	
	public List<String> getTags()
	{
		return this.tags;
	}
	
	public Person getPerson(){
		return person;
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
		FacesMessage message = null;

		try {
			Document document = documentService.store(file.getFileName(), file.getInputstream());
			userService.setUserImage(getPerson().getUserName(), document.getDocumentId());
		} catch (Throwable e) {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "File-Upload Fehler", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			FacesContext context = FacesContext.getCurrentInstance();
			String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/User.jsf"));
			extContext.redirect(url + "?userName=" + userName + "&mode=" + editMode);

		} catch (IOException e) {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "File-Upload Fehler", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
	}

	public String getModeEditAdminDisplay() {
		boolean modeEdit = isModeEdit() || isAdmin();
		return !modeEdit ? "display:none" : "display:all";
	}

	public void updateProfile() {
		try {
			// add tag if not already existing
			if (getTags() != null) {
				for (String token : getTags()) {
					Tag tag = tagService.getTagByToken(token);
					if (tag == null) {
						tag = new Tag(token, token);
						tagService.insert(tag);
					}
				}
			}
			
			
			userService.update(person, getTags());

		} catch (Throwable e) { 
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aktualisiserung Fehler", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aktualisiserung erfolgreich", "");
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
		for (String tag : getTags()) {
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
	public void handleTagSelect(SelectEvent event) {}

	/**
	 * called when a tag is removed from the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagUnselect(UnselectEvent event) {}

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
		for (String tag : getTags()) {
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
		if (person.getContacts().contains(getLoggedInUser())) {
			return true;
		}
		return false;
	}

	public String contactButtonText() {
		if (person.getContacts().contains(getLoggedInUser())) {
			return "Aus meinen Kontakten entfernen";
		} else {
			return "Zu meinen Kontakten hinzufügen";
		}
	}

	public String sendMessageButtonText() {
		return "Nachricht senden";
	}

	public String sendMessageButtonAction() {
		List<Community> communities = person.getConfirmedCommunities();
		Optional<Community> userCommunity = communities
				.stream()
				.filter(c -> c.getPrivateUser() != null
						&& c.getPrivateUser().getUserName().equals(userName)).findAny();

		if (userCommunity.isPresent()) {
			return "/protected/chat/AddMessage.jsf?community="
					+ userCommunity.get().getName() + "&lockCommunity=true&faces-redirect=true";
		}
		return "/protected/chat/AddMessage.jsf?faces-redirect=true";
	}

	public String contactButtonAction() {

		try {
			if (person.getContacts().contains(getLoggedInUser())) {

				userService.removeRelation(getLoggedInUser(), userService.getUser(person.getUserName()));

			} else {
				userService.createRelation(getLoggedInUser(), userService.getUser(person.getUserName()));
			}
			
		} catch (Throwable e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Kontakt hinzufügen Fehler", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return "/protected/User.jsf/?faces-redirect=true&userName=" + userName + "&mode=" + editMode;
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

	public List<Person> getUsersWithDepartment() {
		return userService.getUsersWithDepartment(person.getDepartment());
	}
	
	public String getContactListEntryClass(String username, String department) {
		if (!username.equals(userSession.getUsername()))
			return "";

		Person person = null;
		try {
			person = userService.getUser(userSession.getUsername());
			if (department.equals(person.getDepartment()))
				return "samedepartment";
			else
				return "";
		} catch (Throwable e) {
			return "";
		}
	}
	
	/**
	 * creates  a new Community for the user logged in
	 */	
	public void createCommunity() {
		try {
			chatService.createChatCommunity(userSession.getUsername(), getCommunityName(), true);
			setCommunityName("");
			getPerson();
		} catch (ServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Community anlegen Fehler", e.getMessage());
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
		List<CommunityMember> communityMemberships = person.getMemberships();
		Iterator<CommunityMember> membershipIterator = communityMemberships.iterator();
		
		while (membershipIterator.hasNext()) {
			CommunityMember membership = membershipIterator.next();
			if (membership.getCommunity().getPrivateUser() != null && 
				membership.getCommunity().getPrivateUser().getPersonId() == person.getPersonId()) {
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
	
	/**
	 * returns true if the current login user is allowed to change news
	 *
	 * @return true / false
	 * 
	 */
	public boolean getCanEditNews() {
		return userSession.canEditNews();
	}
	
	public String getImageRef () {
		return userService.getImageRef(person);
	}
}
