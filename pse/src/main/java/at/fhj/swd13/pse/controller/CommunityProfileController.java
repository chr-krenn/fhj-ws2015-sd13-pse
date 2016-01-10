package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
@ViewScoped
public class CommunityProfileController extends ControllerBase implements Serializable {

	private static final long serialVersionUID = -984282742840189477L;

	@Inject
	private ChatService chatService;

	@Inject
	private Logger logger;

	@Inject
	private UserSession userSession;

	private int communityId;
	private String communityIdString;
	private Community community;

	/**
	 * Returns selected community of communities site
	 * 
	 */
	private Community getCommunity() {
		communityId = Integer.parseInt(communityIdString);
		try {
			community = chatService.getCommunity(communityId);
		} catch (ServiceException e) {
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
		return community;
	}

	/**
	 * sets Parameter for new selected community
	 * 
	 */
	public void setParameter() {
		communityIdString = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("id");
		getCommunity();
	}

	/**
	 * Returns community name
	 * 
	 */
	public String getSelectedCommunityName() {
		return community.getName();
	}

	/**
	 * Returns community id
	 * 
	 */
	public int getCommunityId() {
		return communityId;
	}

	/**
	 * Redirects to create Message site for selected community
	 * 
	 */
	public void onCreateNewActivity() {
		try {
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"chat/AddMessage.jsf?community="
									+ community.getName()
									+ "&lockCommunity=true");
		} catch (IOException e) {
			logger.error("[COMMUNITY] Failed to load create new community Message site for community"
					+ community.getName()
					+ "and user"
					+ userSession.getUsername() + ": " + e.getMessage());
		}
	}
}
