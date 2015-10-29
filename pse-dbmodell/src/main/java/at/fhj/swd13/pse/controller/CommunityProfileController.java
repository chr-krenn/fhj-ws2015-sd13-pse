package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
@ViewScoped
public class CommunityProfileController implements Serializable {

	private static final long serialVersionUID = -984282742840189477L;

	@Inject
	private ChatService chatService;

	private String communityId;
	private String communityName;

	@PostConstruct
	public void setup() {
		communityId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		getCommunity();
	}
	
	private void getCommunity() {
			//Community community = chatService.getCommunity(communityId);
			communityName = communityId;//community.getName();

	}
	
	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
}
