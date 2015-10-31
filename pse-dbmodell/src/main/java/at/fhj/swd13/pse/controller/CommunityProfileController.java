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

	private int communityId;
	private String communityIdString;
	private Community community;
	
	@PostConstruct
	public void setup() {
		//communityId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		communityIdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		getCommunity();
	}
	
	private Community getCommunity() {
		communityId = Integer.parseInt(communityIdString);
		return community = chatService.getCommunity(communityId);
	}
	
	public  String getSelectedCommunityName(){
		return community.getName();	
	}
	
	public int getCommunityId(){
		return communityId;
	}
}
