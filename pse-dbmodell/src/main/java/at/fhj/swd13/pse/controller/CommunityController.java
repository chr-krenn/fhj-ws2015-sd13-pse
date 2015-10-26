package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.CommunityDTO;
import at.fhj.swd13.pse.plumbing.UserSession;

/**
 * 
 * @author patrick.almer
 *
 */
@Model
public class CommunityController {

    @Produces
    @Named
    @OrderBy("createdAt ASC")
	private List<Community> communities;
    
    @Inject
    private ChatService chatService;
    
    @Inject
	private UserService userService;
    
    @Inject
	private UserSession userSession;
    
    @Inject
	private Logger logger;
    
    private String searchFieldText = "";

	@PostConstruct
    public void postConstruct() {
    	communities = chatService.getAllCommunities();
    }
    
    public List<Community> getCommunities () {
    	return communities;
    }
    
    public String getSearchFieldText() {
		return searchFieldText;
	}

	public void setSearchFieldText(String searchFieldText) {
		this.searchFieldText = searchFieldText;
	}
	
	/*
	 public String search() {s
	    	communities = chatService.getSubscribedCommunitiesForUser(searchFieldText, CookieHelper.getAuthTokenValue());
	    	return "communities";
	 }*/
	    
}
