package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.dto.CommunityDTO;
import at.fhj.swd13.pse.plumbing.UserSession;

/*
 * Test data
 * INSERT INTO `community` VALUES (4,'\0','\0','Dunkelgrau','2015-10-16 20:00:45',100,NULL,100),(5,'\0','\0','Dunkelblau','2015-10-16
 * 20:00:56',100,NULL,100),(6,'\0','\0','Dunkelbunt','2015-10-16 20:01:02',100,NULL,100),(7,'\0','\0','Gelb','2015-10-16
 * 20:01:10',100,NULL,100),(8,'\0','\0','Rot','2015-10-16 20:01:14',100,NULL,100);
 */

@ManagedBean
@ViewScoped
public class MessageEditorController {

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

	private Date dtFrom;
	private Date dtUntil;

	private int iconId;
	private String iconRef;

	private int documentId;
	private String documentRef;
	private String documentName;

	private boolean communityLocked;

	private Community targetCommunity = null;

	private List<CommunityDTO> selectedCommunities = new ArrayList<CommunityDTO>();

	private List<String> selectedTags = new ArrayList<String>();

	@PostConstruct
	public void init() {
		String receiverCommunity = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("community");
		if (receiverCommunity != null) {
			targetCommunity = chatService.getCommunity(receiverCommunity);
			if (targetCommunity != null) {
				selectedCommunities.add(new CommunityDTO(targetCommunity));
			}
		}

		String lockCommunityString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lockCommunity");

		communityLocked = false;
		if (lockCommunityString != null) {
			communityLocked = Boolean.parseBoolean(lockCommunityString);
		}
	}

	/**
	 * Returns the community name
	 */
	public String getCommunityName() {
		if (targetCommunity == null) {
			return "";
		}

		return targetCommunity.getName();
	}

	/**
	 * Save the entered message to the database
	 */
	public void save() {
		logger.info("[MSG+] saving message... ");

		Document document = documentService.get(documentId);
		Document icon = documentService.get(iconId);
		List<Community> communities = new ArrayList<Community>();
		List<MessageTag> messageTags = new ArrayList<MessageTag>();
		Tag tag;
		MessageTag messageTag;

		for (String tagString : selectedTags) {
			tag = tagService.getTagByToken(tagString);
			if (tag == null) {
				tag = new Tag();
				tag.setToken(tagString);
				tag.setDescription(tagString);
				try {
					tagService.insert(tag);
				} catch (ConstraintViolationException x) {
					logger.error("[MSG+] error creating new tag (duplicate...)");
				}
			}
			messageTag = new MessageTag();
			messageTag.setTag(tag);
			messageTag.setCreatedAt(new Date());
			messageTags.add(messageTag);
		}

		for (CommunityDTO communityDto : selectedCommunities) {
			communities.add(chatService.getCommunity(communityDto.getName()));
		}

		// FIXME valid from - valid until - also use MessageDTO
		feedService.saveMessage(headline, richText, userSession.getUsername(), document, icon, communities, messageTags, dtFrom, dtUntil);

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			if (targetCommunity == null) {
				String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/Main.jsf"));
				extContext.redirect(url);
			}// FIXME find better solution
			else if (targetCommunity.getCommunityId() == 1) {
				String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/protected/Main.jsf"));
				extContext.redirect(url);
			} else {
				String url = extContext.encodeActionURL(
						context.getApplication().getViewHandler().getActionURL(context, "/protected/Community.jsf?id= " + targetCommunity.getCommunityId()));
				extContext.redirect(url);
			}
		} catch (IOException e) {
			logger.error("[MSG+] error redirecting after logout: " + e.getMessage());
		}
	}

	/**
	 * Removes a message from the database
	 */
	public void removeMessage() {
		String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
		int id = Integer.parseInt(messageId);
		feedService.removeMessage(id);
	}

	/**
	 * Return a list of matching communities based on the input
	 * 
	 * @param input
	 *            needle for the search
	 * 
	 * @return a list of matching communities or an empty list
	 */
	public List<CommunityDTO> completeCommunity(String input) {
		logger.info("[MSG+] complete called with: " + input);

		List<CommunityDTO> result = new ArrayList<CommunityDTO>();

		for (Community community : chatService.getPossibleTargetCommunities( userSession.getUsername(), input)) {
			CommunityDTO communityDTO = new CommunityDTO(community);

			if (!isCommunityAlreadySelected(communityDTO.getToken())) {
				result.add(communityDTO);
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

		for (CommunityDTO communityDTO : selectedCommunities) {
			if (communityDTO.getToken().equals(token)) {
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
		CommunityDTO removedCommunity = (CommunityDTO) event.getObject();
		logger.info("[MSG+] Community handleUnselect: " + removedCommunity.getName());

		// TODO Prevent unselection of preselected items

		// if (targetCommunity != null &&
		// removedCommunity.getName().equals(targetCommunity.getName())) {
		// selectedCommunities.add(new CommunityDTO(targetCommunity));
		// }
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
	public List<CommunityDTO> getSelectedCommunities() {

		return selectedCommunities;
	}

	/**
	 * Set the list of the selected communities
	 * 
	 * @param selectedCommunities
	 *            the list of in the view selected communities
	 */
	public void setSelectedCommunities(List<CommunityDTO> selectedCommunities) {

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
}
