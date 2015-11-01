package at.fhj.swd13.pse.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
@ViewScoped
public class MessageCommentController {

	@Inject
	private Logger logger;
	
	@Inject
	private UserSession session;
	
	@Inject
	private ChatService chatService;
	
	private String text;
	
	private String headline;
	
	private boolean showPanel = false;
	
	public void addComment() {
		
		final int parentMessageId = Integer.parseInt( FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentMessageId") );
		
		logger.info("[CMT] adding comment for " + parentMessageId + " from " + session.getUsername() + "  " + text );
		
		chatService.addComment( session.getUsername(), parentMessageId, headline, text );
		
		
		showPanel = false;
	}
	
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	
	/**
	 * @return the showPanel
	 */
	public boolean isShowPanel() {
		return showPanel;
	}


	
	/**
	 * @param showPanel the showPanel to set
	 */
	public void setShowPanel(boolean showPanel) {
		this.showPanel = showPanel;
	}


	
	/**
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}


	
	/**
	 * @param headline the headline to set
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
}
