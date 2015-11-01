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
    private boolean isMember;
    
    @PostConstruct
    public void postConstruct() {
    	communities = chatService.getAllAccessibleCommunities();	
    	
    	communityIdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("communityId");
    	invitationOnly = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("invitationOnly");
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
			logger.info("  community: " + com.getCommunityId() + " - " +com.getName() );
			
			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );

			//addCommunityMember
			CommunityMember member = chatService.createCommunityMember(currentUser, com);
			logger.debug("  CommunityMemberId : " + member.getCommunityMemberId() );

			if( !isinvitationOnly() ) // public Community
			{
				logger.info("#### Public Community ####");
			
				if(member != null)
				{
					setMember( isMemberOfCommunity( com.getCommunityId() ) );
				}
				
				info("You are now a member of the community '" + com.getName() + "'");

				logger.info("#### Done - Public Community ####");
				
			}else // private Community
			{
				logger.info("#### Private Community ####");
				
				List<CommunityMember> memberList = chatService.getCommunityMembersList(com);
				
				logger.debug("  memberListSize: <" + memberList.size() + ">" );
				
				for( CommunityMember m : memberList )
				{
					if( m.getIsAdministrator() )
					{
						logger.debug("  adminMemberLastname: " + m.getMember().getLastName() );
						info("Your request has been sent to the administrator: " + m.getMember().getFirstName() + " " + m.getMember().getLastName() );
						
						break;
					}else
					{
						info("Your request has been sent to the administrator");
					}
					
				}
				
				
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
			
			
			if(!com.getInvitationOnly()) //Public community
			{
				setMember( chatService.isPersonMemberOfCommunity(currentUser, com) );
			}
			else
			{
				//Private community
				
				try 
				{
					CommunityMember m = chatService.getCommunityMember(com, currentUser);
				
					if(m.getConfirmer() != null)
					{
						setMember( chatService.isPersonMemberOfCommunity(currentUser, com) );
					}
					else
					{
						setMember(false);
					}

				
				} catch (NullPointerException e) {
					// communityMember is Null
					setMember(false);
				}
				
								
			}
			
		} catch (Exception e) {
			logger.debug(" Error_Message: " + e.getMessage());
		}
		
		logger.info("## isMemberOfCommunity <" + isMember() + ">; community: <" + com.getName() + ">; person <"+ currentUser.getLastName() + "> ##");
		
		return isMember();
		
	}

	public String isMemberToString()
	{
		logger.debug("## isMemberToString - isMember <" + isMember() + ">");
		if(isMember())
		{
			return answerYes;
		}
		else
		{
			return answerNo;
		}
		
	}

	public Boolean disableAskCommunity(int comId)
	{
		boolean disable = false;
		setMember(isMemberOfCommunity(comId));
		
		if(isMember())
		{
			disable = true; //disable button askCommunity
		}
		else
		{
			Person currentUser = null;
			Community com = null;
			try 
			{
				currentUser = userService.getUser(userSession.getUsername());
				logger.debug("  currentUser: " + currentUser.getPersonId() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName() );
				
				com = chatService.getCommunity(comId);
				logger.debug("  community: " + com.getCommunityId() + " - " + com.getName() );
					
				try 
				{
					CommunityMember m = chatService.getCommunityMember(com, currentUser);
					
					if(m.getConfirmer() == null)
						disable = true; // enable button askCommunity because it was pressed one time.
					
				} catch (NullPointerException e) {
					// communityMember does not exists means the button askCommunity was never pressed.
					disable = false;
				}
				
			} catch (Exception e) {
				logger.debug(" Error_Message: " + e.getMessage());
			}
		}
		
		logger.info("## disableAskCommunity - disable <"+ disable + ">");
		
		return disable;
			
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
