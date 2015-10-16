package at.fhj.swd13.pse.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.dto.CommunityDTO;


/*
 * Test data 
 *INSERT INTO `community` VALUES (4,'\0','\0','Dunkelgrau','2015-10-16 20:00:45',100,NULL,100),(5,'\0','\0','Dunkelblau','2015-10-16 20:00:56',100,NULL,100),(6,'\0','\0','Dunkelbunt','2015-10-16 20:01:02',100,NULL,100),(7,'\0','\0','Gelb','2015-10-16 20:01:10',100,NULL,100),(8,'\0','\0','Rot','2015-10-16 20:01:14',100,NULL,100); 
 */

@ManagedBean
public class MessageEditorController {

	@Inject
	private Logger logger;

	@Inject
	private ChatService chatService;

	private String headline;
	private String richText;
	private String iconRef;

	private List<CommunityDTO> selectedCommunities = new ArrayList<CommunityDTO>();

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

			result.add(communityDTO);
		}

		logger.info("[MSG+] matching communities found: " + result.size());

		return result;
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

		logger.info("[MSG+] set selected with an itemcount of " + selectedCommunities.size() );
		
		this.selectedCommunities = selectedCommunities;
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
