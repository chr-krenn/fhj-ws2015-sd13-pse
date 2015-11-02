package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.event.SelectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.MailService;
import at.fhj.swd13.pse.plumbing.UserSession;

/**
 * 
 * @author manuelkirsch
 *
 */
@Model
public class RequestController {

    @Produces
    @Named
    @OrderBy("createdAt ASC")
	private List<Community> requests;
    
    @Inject
    private ChatService chatService;
    

	@Inject
	private MailService mailService;
	
    @Inject
    private Logger logger;
    
    @Inject
    private UserSession userSession;
	
    @Inject
    private UserService userService;
    
    private transient Community selectedRequest = null;
    
    private int communityId;
    private String communityIdString;
    private boolean isMember;
    
    @PostConstruct
    public void postConstruct() {
    	requests = chatService.getUnconfirmedCommunities();	
    	
    	communityIdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("communityId");
    }

    
    public List<Community> getRequests () {
    	return requests;
    }
	
	public Community getSelectedRequest() {
		return selectedRequest;
	}

	public void setSelectedRequest(Community selectedRequest) {
	this.selectedRequest = selectedRequest;
	}	
	
	public void onCommunitySelected(SelectEvent object){
	    try 
	    {
	    	FacesContext.getCurrentInstance().getExternalContext().redirect("Community.jsf?id=" + selectedRequest.getCommunityId());
		} 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
		}
	}
	
	public void approveCommunity(){
				
		Community com = null;
		Person currentUser = null;
		communityId = Integer.parseInt(communityIdString);
		
		try 
		{	
			com = chatService.getCommunity(communityId);
			logger.info("  community: " + com.getCommunityId() + " - " +com.getName() );
			
			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );

			chatService.confirmCommunity(currentUser, com);
			logger.info(" Community Confirmed: " +com.getCommunityId() +" by: "+currentUser.getPersonId() );
			
		} catch (Exception e) {
			logger.error("ERROR: " + e.getMessage());
		}
		
    	requests = chatService.getUnconfirmedCommunities();	

    	try {
			mailService.sendMail("Community Antrag "+com.getName()+" Freigegeben",
					"Die Community " + com.getName() + " ist jetzt freigegeben!", com.getCreatedBy().getEmailAddress());
			logger.info("Email sent");

    	} catch (MessagingException e) {
			logger.error("ERROR: " + e.getMessage());
		}
		
    	info("Community Antrag angenommen!");
	}
	
	public void declineCommunity(){
		
		Community com = null;
		Person currentUser = null;
		communityId = Integer.parseInt(communityIdString);
		
		try 
		{	
			com = chatService.getCommunity(communityId);
			logger.info("  community: " + com.getCommunityId() + " - " +com.getName() );
			
			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );

			chatService.declineCommunity(currentUser, com);
			logger.info(" Community Declined: " +com.getCommunityId() +" by: "+currentUser.getPersonId() );
			
		} catch (Exception e) {
			logger.error("ERROR: " + e.getMessage());
		}
		
    	requests = chatService.getUnconfirmedCommunities();	

    	try {
			mailService.sendMail("Community Antrag "+com.getName()+" Abgelehnt",
					"Die Community " + com.getName() + " wurde nicht freigegeben!", com.getCreatedBy().getEmailAddress());
			logger.info("Email sent");

    	} catch (MessagingException e) {
			logger.error("ERROR: " + e.getMessage());
		}
    	info("Community Antrag abgelehnt!");
	}

	public void error() {
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!", "You are not a member of this community!"));
	}
	
	public void info(String text) {
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", text));
	}
	 
	public boolean isMember() {
		return isMember;
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}
}
