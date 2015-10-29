package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.event.SelectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
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
    private Logger logger;
    
    @Inject
    private UserSession userSession;
	
    @Inject
    private UserService userService;
    
    private transient Community selectedCommunity = null;
	
    private String searchFieldText = "";

	private String invitationOnly;
    private String communityName;
    private String privateUser;
    private boolean isMember;
    
    @PostConstruct
    public void postConstruct() {
    	communities = chatService.getAllCommunities();	
    	communityName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("communityName");
    	invitationOnly = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("invitationOnly");
    	privateUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("privateUser");
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
    	
	public String search() {
	 	communities = chatService.getAllCommunities(searchFieldText);
    	return "communities";
    }
	
	public Community getSelectedCommunity() {
		return selectedCommunity;
	}

	public void setSelectedCommunity(Community selectedCommunity) {
	this.selectedCommunity = selectedCommunity;
	}	
	
	public void onCommunitySelected(SelectEvent object){
	    try 
	    {
	    	FacesContext.getCurrentInstance().getExternalContext().redirect("Community.jsf?id=" + selectedCommunity.getCommunityId());
		} 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
		}
	}
	
	public void subscribeCommunity(){
		
		logger.info("######## Start - subscribeCommunity ########");
		
		if( !isinvitationOnly() ) // public Community
		{
			logger.info("#### Public Community ####");
			
			Community com = null;
			Person currentUser = null;
			
			try 
			{	
				com = chatService.getCommunity(communityName);
				currentUser = userService.getUser(userSession.getUsername());
				
				logger.info("  community: " + com.getCommunityId() + " - " +com.getName() );
				logger.info("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );
				
				//addCommunityMember
				CommunityMember member = chatService.createCommunityMember(currentUser, com);
				
				logger.info("  currentUser: " + member.getCommunityMemberId() );
				
			} catch (Exception e) {
				logger.error("ERROR-MESSAGE: " + e.getMessage());
			}
			
			logger.info("#### Done - subscribeCommunity ####");
			
		}else // private Community
		{
			logger.info("TODO -- Private Communities");
		}
		
		logger.info("######## DONE - subscribeCommunity ########");
		
	}
	
	private boolean isinvitationOnly() {
		return invitationOnly != null && invitationOnly.equals("true");
	}
	
	public boolean isMemberOfCommunity()
	{
		isMember = false;
		Person currentUser = null;
		try 
		{
			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );
			
			//TODO
			
			
		} catch (Exception e) {
			logger.error("ERROR-MESSAGE: " + e.getMessage());
		}
		return isMember;
	}
}
