package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.plumbing.UserSession;


@ManagedBean
@ViewScoped
public class MessageEditorController extends ControllerBase{

	@Inject
	private Logger logger;

	@Inject
	private ChatService chatService;

	@Inject
	private TagService tagService;

	@Inject
	private DocumentService documentService;

	@Inject
	private FeedService feedService;

	@Inject
	private UserSession userSession;

	private String headline;
	private String richText;

	private Date dtFrom;
	private Date dtUntil;

	private int iconId;
	private String iconRef;

	private int documentId;
	private String documentRef;
	private String documentName;

	private boolean communityLocked;
	private int messageId;
	
	private Community targetCommunity = null;

	private List<Community> selectedCommunities = new ArrayList<Community>();

	private List<String> selectedTags = new ArrayList<String>();

	@PostConstruct
	public void init() {
		String receiverCommunity = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("community");
		if (receiverCommunity != null) {
			loadCommunity(receiverCommunity);
		}

		//community cannot be changed
		String lockCommunityString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lockCommunity");
		communityLocked = false;
		if (lockCommunityString != null) {
			communityLocked = Boolean.parseBoolean(lockCommunityString);
		}
		
		String messageIdString = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("messageId");		
		if(messageIdString != null){
			loadMessage(Integer.parseInt(messageIdString)); 
		}
	}

	private void loadMessage(int messageId){
		try {
			this.messageId = messageId;
			MessageDTO messageDto = feedService.getMessageDTOById(messageId);
			
			headline = messageDto.getHeadline();
			richText = messageDto.getText();
			
			dtFrom = messageDto.getValidFrom();
			dtUntil = messageDto.getValidUntil();
			
			for(Community c : messageDto.getCommunities()) {
				loadCommunity(c.getName());
			}
			
			Document icon = messageDto.getImage();
			if(icon != null){
				iconId = icon.getDocumentId();
				iconRef = documentService.buildServiceUrl(iconId);
			}
			
			Document document = messageDto.getAttachement();
			if(document != null){
				documentId = document.getDocumentId();
				documentRef = documentService.buildServiceUrl(documentId);
				documentName = document.getName();
			}
			
			selectedTags = messageDto.getTags();
			
		} catch (ServiceException e) {
			logger.error("[MSG+] Load message by id failed");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
	}
	
	private void loadCommunity(String communityName){
		try {
			targetCommunity = chatService.getCommunity(communityName);
		} catch (ServiceException e) {
			logger.info("[MSG+] failed to load community: " +e.getMessage());
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
		if (targetCommunity != null) {
			selectedCommunities.add(targetCommunity);
		}
	}
	
	/**
	 * Returns the community name
	 */
	public String getCommunityName() {
		String communities = "";
		for(Community community: this.selectedCommunities){
			if(communities != ""){
				communities += " und ";
			}
			
			communities += community.getName();
		}
		
		return communities;
	}

	/**
	 * Save the entered message to the database
	 */
	public void save() {
		logger.info("[MSG+] saving message... ");

		Document document = documentService.get(documentId);
		Document icon = documentService.get(iconId);
		List<MessageTag> messageTags = new ArrayList<MessageTag>();
		Tag tag;
		MessageTag messageTag;

		for (String tagString : selectedTags) {
			tag = tagService.getTagByToken(tagString);
			if (tag == null) {
				tag = new Tag(tagString, tagString);
				try {
					tagService.insert(tag);
				} catch (Throwable x) {
					logger.error("[MSG+] error creating new tag (duplicate...)");
				}
			}
			messageTag = new MessageTag(tag);
			messageTags.add(messageTag);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		extContext.getFlash().setKeepMessages(true);
		
		try {
			//if message id exists update the existing message
			if(this.messageId > 0){
				feedService.updateMessage(messageId, headline, richText, document, icon, messageTags, dtFrom, dtUntil);
			}else{
				feedService.saveMessage(headline, richText, userSession.getUsername(), document, icon, selectedCommunities, messageTags, dtFrom, dtUntil);
			}

			if (targetCommunity == null || targetCommunity.getSystemInternal()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nachricht gesendet", "Ihre Nachricht wurde erfolgreich in " + getCommunityName() + 
						" gepostet."));
				String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/Main.jsf"));
				extContext.redirect(url);
			} else if (targetCommunity.isPrivateChannel()) {
				Person receiver = targetCommunity.getPrivateUser();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nachricht gesendet", "Ihre Nachricht an " + receiver.getFullName() + 
						" wurde erfolgreich gesendet."));
				String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/User.jsf"));
				extContext.redirect(url+"?userName=" + receiver.getUserName() + "&mode=view");
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nachricht gesendet", "Ihre Nachricht wurde erfolgreich in " + targetCommunity.getName() + 
						" gepostet."));
				String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/Community.jsf"));
				extContext.redirect(url + "?id=" + targetCommunity.getCommunityId());
			}
		} catch (IOException e) {
			logger.error("[MSG+] error redirecting after logout: " + e.getMessage());
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nachricht konnte nicht gesendet werden", 
					"Fehler beim Senden der Nachricht! Bitte versuchen Sie es erneut."));
		} catch (ServiceException e) {
			logger.error("[MSG+] error redirecting after logout: " + e.getMessage());
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nachricht konnte nicht gesendet werden", 
					"Fehler beim Senden der Nachricht! Bitte versuchen Sie es erneut."));
		}
	}

	/**
	 * Removes a message from the database
	 * @throws IOException 
	 */
	public void removeMessage() {
		String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
		int id = Integer.parseInt(messageId);
		feedService.removeMessage(id);
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		extContext.getFlash().setKeepMessages(true);
		
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nachricht gelöscht", "Ihre Nachricht wurde erfolgreich gelöscht."));
		String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/Main.jsf"));
		
		try {
			extContext.redirect(url);
		} catch (IOException e) {
			logger.error("[MSG+] error redirecting after logout: " + e.getMessage());
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nachricht konnte nicht gelöscht werden", 
					"Fehler beim Löschen der Nachricht! Bitte versuchen Sie es erneut."));
		}
	}

	/**
	 * Return a list of matching communities based on the input
	 * 
	 * @param input
	 *            needle for the search
	 * 
	 * @return a list of matching communities or an empty list
	 */
	public List<Community> completeCommunity(String input) {
		logger.info("[MSG+] complete called with: " + input);

		List<Community> result = new ArrayList<Community>();

		List<Community> possibleTargetCommunities = new ArrayList<Community>();
		try {
			possibleTargetCommunities = chatService.getPossibleTargetCommunities(userSession.getUsername(), input);
		} catch (ServiceException e) {
			logger.info("[MSG+] cannot load possible target communities");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
		
		for (Community community : possibleTargetCommunities) {
			if (!isCommunityAlreadySelected(community.getToken())) {
				result.add(community);
			}
		}
		
		logger.info("[MSG+] matching and not already selected communities found: " + result.size());

		return result;
	}

	/**
	 * check if the community with the given token has already been selected
	 * 
	 * @param token
	 *            token of the communityDTO to check
	 * 
	 * @return true if the communityDTO has already been selected, false
	 *         otherwise
	 */
	private boolean isCommunityAlreadySelected(final String token) {

		logger.info("[MSG+] checking already selected for " + token);
		logger.info("[MSG+] selected count " + selectedCommunities.size());

		for (Community community : selectedCommunities) {
			if (community.getToken().equals(token)) {
				return true;
			}
		}

		return false;
	}

	public List<String> completeTag(String input) {

		List<String> result = new ArrayList<String>();

		logger.info("[MSG+] completeTag - selcted tag count " + selectedTags.size());

		for (Tag tag : tagService.getMatchingTags(input)) {

			if (!isTagAlreadySelected(tag.getToken())) {
				result.add(tag.getToken());
			}
		}

		if (result.size() == 0 && !selectedTags.contains(input)) {
			result.add(input);
		}

		return result;
	}

	private boolean isTagAlreadySelected(final String token) {

		final String needle = token.toLowerCase();

		for (String tag : selectedTags) {

			if (tag.toLowerCase().equals(needle)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * called when a community is added to the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleSelect(SelectEvent event) {

		logger.info("[MSG+] Community handleSelect");
	}

	/**
	 * called when a community is removed from the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleUnselect(UnselectEvent event) {
		Community removedCommunity = (Community) event.getObject();
		logger.info("[MSG+] Community handleUnselect: " + removedCommunity.getName());
	}

	/**
	 * called when a tag is added to the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagSelect(SelectEvent event) {

		logger.info("[MSG+] Tag handleSelect");
	}

	/**
	 * called when a tag is removed from the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagUnselect(UnselectEvent event) {

		logger.info("[MSG+] Tag handleUnselect");
	}

	/**
	 * Get the currently selected communities
	 * 
	 * @return list of the selected communities (may be empty)
	 */
	public List<Community> getSelectedCommunities() {

		return selectedCommunities;
	}

	/**
	 * Set the list of the selected communities
	 * 
	 * @param selectedCommunities
	 *            the list of in the view selected communities
	 */
	public void setSelectedCommunities(List<Community> selectedCommunities) {

		logger.info("[MSG+] set selected communities with an itemcount of " + selectedCommunities.size());

		this.selectedCommunities = selectedCommunities;
	}

	public List<String> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<String> selectedTags) {

		if (selectedTags != null) {
			logger.info("[MSG+] set selected tags with an itemcount of " + selectedTags.size());

			this.selectedTags = selectedTags;
		} else {
			this.selectedTags = new ArrayList<String>();
		}
	}

	/**
	 * open the file upload dialog and upload an image
	 */
	public void uploadIcon() {

		logger.info("[MSG+] uploading icon");

		RequestContext.getCurrentInstance().openDialog("/protected/ImageUpload");
	}

	public void onIconUploaded(SelectEvent element) {

		logger.info("[MSG+] icon uploaded " + element);

		if (element != null) {
			final int addedDocumentId = (Integer) element.getObject();

			logger.info("[MSG+] uploaded documentId was " + addedDocumentId);

			iconId = addedDocumentId;
			iconRef = documentService.buildServiceUrl(iconId);
		}
	}

	/**
	 * open the file upload dialog and upload an image
	 */
	public void uploadDocument() {

		logger.info("[MSG+] uploading document");

		RequestContext.getCurrentInstance().openDialog("/protected/DocumentUpload");
	}

	public void onDocumentUploaded(SelectEvent element) {

		logger.info("[MSG+] document uploaded " + element);

		if (element != null) {
			final int addedDocumentId = (Integer) element.getObject();

			logger.info("[MSG+] uploaded documentId was " + addedDocumentId);

			documentId = addedDocumentId;
			documentRef = documentService.buildServiceUrl(documentId);

			Document d = documentService.get(documentId);
			documentName = d.getName();
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * 
	 * @param headline
	 */
	public void setHeadline(String headline) {

		logger.info("[MSG+] new headline: " + headline);

		this.headline = headline;
	}

	/**
	 * 
	 * @return
	 */
	public String getRichText() {
		return richText;
	}

	/**
	 * 
	 * @param richText
	 */
	public void setRichText(String richText) {
		logger.info("[MSG+] new (rich)text: " + headline);

		this.richText = richText;
	}

	/**
	 * 
	 * @return
	 */
	public String getIconRef() {

		if (iconRef == null) {
			return documentService.getDefaultDocumentRef(DocumentService.DocumentCategory.MESSAGE_ICON);
		}

		return iconRef;
	}

	/**
	 * 
	 * @param iconRef
	 */
	public void setIconRef(String iconRef) {
		this.iconRef = iconRef;
	}

	public String getDocumentRef() {
		if (documentRef == null) {
			return documentService.getDefaultDocumentRef(DocumentService.DocumentCategory.USER_IMAGE);
		}

		logger.info("[MSG+] documentRef: " + documentRef);
		return documentRef;
	}

	public void setDocumentRef(String documentRef) {
		this.documentRef = documentRef;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	/**
	 * @return the dtUntil
	 */
	public Date getDtUntil() {
		return dtUntil;
	}

	/**
	 * @param dtUntil
	 *            the dtUntil to set
	 */
	public void setDtUntil(Date dtUntil) {
		this.dtUntil = dtUntil;
	}

	public boolean isCommunityLocked() {
		return communityLocked;
	}
	
	/**
	 * @return the dtFrom
	 */
	public Date getDtFrom() {
		return dtFrom;
	}

	/**
	 * @param dtFrom
	 *            the dtFrom to set
	 */
	public void setDtFrom(Date dtFrom) {
		this.dtFrom = dtFrom;
	}
	
	public boolean isNewMessage(){
		if(messageId > 0){
			return false;
		}
		return true;
	}
}
