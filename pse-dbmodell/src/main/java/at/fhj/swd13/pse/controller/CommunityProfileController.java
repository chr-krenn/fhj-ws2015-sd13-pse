package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.domain.chat.ChatService;

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
	
	public void onCreateNewActivity(){
	    try 
	    {
	    	FacesContext.getCurrentInstance().getExternalContext().redirect("xperimental/AddCommunityMessage.jsf?community=" + community.getName());
		} 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
		}
	}	
}
