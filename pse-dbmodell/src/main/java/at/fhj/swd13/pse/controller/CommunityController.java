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
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.event.SelectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.UserSession;
import javassist.bytecode.ConstantAttribute;


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

    public static final String answerYes = "Yes";
    public static final String answerNo = "No";
    
    private int communityId;
	private String invitationOnly;
    private String communityIdString;
    private String privateUser;
    private boolean isMember;
    
    @PostConstruct
    public void postConstruct() {
    	communities = chatService.getAllAccessibleCommunities();	
    	
    	communityIdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("communityId");
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
	 	communities = chatService.getAllAccessibleCommunities(searchFieldText);
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
	    	if(isMemberOfCommunity(selectedCommunity.getCommunityId()))
	    	{
	    		FacesContext.getCurrentInstance().getExternalContext().redirect("Community.jsf?id=" + selectedCommunity.getCommunityId());
	    	}
	    	else
	    	{
	    		error();
	    		logger.info("User " + userSession.getUsername() + " is not a member of the community " + selectedCommunity.getName());
	    	}
		} 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
		}
	}
	
	public void subscribeCommunity(){
		
		logger.info("######## Start - subscribeCommunity ########");
		
		Community com = null;
		Person currentUser = null;
		communityId = Integer.parseInt(communityIdString);
		
		try 
		{	
			com = chatService.getCommunity(communityId);
			logger.debug("  community: " + com.getCommunityId() + " - " +com.getName() );
			
			currentUser = userService.getUser(userSession.getUsername());
			logger.debug("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );
			
			if( !isinvitationOnly() ) // public Community
			{
				logger.info("#### Public Community ####");
	
					//addCommunityMember
					CommunityMember member = chatService.createCommunityMember(currentUser, com);
					logger.debug("  currentUser: " + member.getCommunityMemberId() );
					
					if(member != null)
					{
						setMember( isMemberOfCommunity( com.getCommunityId() ) );
					}
				
				info("You are now a member of the community '" + com.getName() + "'");
				logger.info("#### Done - Public Community ####");
				
			}else // private Community
			{
				info("Your request has been sent to the administrator!");
				logger.info("#### Private Community ####");
				
				logger.info("private user: " + privateUser);
				
				
				logger.info("#### Done - Private Community ####");
				
			}
			
		} catch (Exception e) {
			logger.error("ERROR-MESSAGE: " + e.getMessage());
		}
		
		
		logger.info("######## DONE - subscribeCommunity ########");
		
	}
	
	private boolean isinvitationOnly() {
		return invitationOnly != null && invitationOnly.equals("true");
	}
	
	public Boolean isMemberOfCommunity(int comId)
	{
		setMember(false);
		Person currentUser = null;
		Community com = null;
		
		try 
		{
			currentUser = userService.getUser(userSession.getUsername());
			logger.debug("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );
			
			com = chatService.getCommunity(comId);
			logger.debug("  community: " + com.getCommunityId() + " - " + com.getName() );
			
			setMember( chatService.isPersonMemberOfCommunity(currentUser, com) );
			logger.debug("isMember: " + isMember());
			
		} catch (Exception e) {
			logger.error("ERROR-MESSAGE: " + e.getMessage() );
		}

		return isMember();
	}

	public String isMemberToString(int communityId)
	{
		setMember( isMemberOfCommunity( communityId ) );
		
		if(isMember())
		{
			return answerYes;
		}
		else
		{
			return answerNo;
		}
		
		
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


	public static String getAnsweryes() {
		return answerYes;
	}


	public static String getAnswerno() {
		return answerNo;
	}
}
