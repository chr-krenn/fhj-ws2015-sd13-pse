package at.fhj.swd13.pse.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.TagService;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.dto.CommunityDTO;

/*
 * Test data 
 *INSERT INTO `community` VALUES (4,'\0','\0','Dunkelgrau','2015-10-16 20:00:45',100,NULL,100),(5,'\0','\0','Dunkelblau','2015-10-16 20:00:56',100,NULL,100),(6,'\0','\0','Dunkelbunt','2015-10-16 20:01:02',100,NULL,100),(7,'\0','\0','Gelb','2015-10-16 20:01:10',100,NULL,100),(8,'\0','\0','Rot','2015-10-16 20:01:14',100,NULL,100); 
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
	DocumentService documentService;

	private String headline;
	private String richText;
	private String iconRef;

	private List<CommunityDTO> selectedCommunities = new ArrayList<CommunityDTO>();

	private List<String> selectedTags = new ArrayList<String>();

	/**
	 * 
	 */
	public void save() {
		logger.info("[MSG+] saving message...");
	}

	/**
	 * 
	 * @param event
	 */
	public void handleIconUpload(FileUploadEvent event) {

		FacesMessage message = new FacesMessage("Fehler", "Hochladen eines Icons noch nicht unterstützt");
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * 
	 * @param event
	 */
	public void handleDocumentUpload(FileUploadEvent event) {

		FacesMessage message = new FacesMessage("Fehler", "Hochladen eines Documentes noch nicht unterstützt");
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, message);
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

		for (Community community : chatService.getPossibleTargetCommunities("des wird no ignoriert", input)) {
			// TODO - cleanup add copyctor to CommunityDTO
			CommunityDTO communityDTO = new CommunityDTO(Integer.toString(community.getCommunityId()),
					community.getName());

			if (!isAlreadySelected(communityDTO.getToken())) {
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
	private boolean isAlreadySelected(final String token) {

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

		for (Tag tag : tagService.getMatchingTags(input)) {

			result.add(tag.getToken());
		}

		return result;
	}

	/**
	 * called when a community is added to the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleSelect(SelectEvent event) {

		logger.info("[MSG+] handleSelect");
	}

	/**
	 * called when a community is removed from the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleUnselect(UnselectEvent event) {

		logger.info("[MSG+] handleUnselect");
	}

	/**
	 * called when a tag is added to the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagSelect(SelectEvent event) {

		logger.info("[MSG+] handleSelect");
	}

	/**
	 * called when a tag is removed from the chosen list
	 * 
	 * @param event
	 *            event data
	 */
	public void handleTagUnselect(UnselectEvent event) {

		logger.info("[MSG+] handleUnselect");
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

		logger.info("[MSG+] set selected tags with an itemcount of " + selectedTags.size());

		this.selectedTags = selectedTags;
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
}
