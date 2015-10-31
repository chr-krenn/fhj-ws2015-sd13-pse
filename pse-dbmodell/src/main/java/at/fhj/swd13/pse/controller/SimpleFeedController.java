package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.dto.UserDTO;
import at.fhj.swd13.pse.plumbing.UserSession;

/**
 * 
 * @author florian.genser
 *
 */
@ManagedBean
@ViewScoped
public class SimpleFeedController {

    @Produces
    @Named
    @OrderBy("createdAt ASC")
	private List<MessageDTO> messages;
    
    @Inject
    private FeedService feedService;
    
    @Inject
	private UserService userService;
    
    @Inject
	private UserSession userSession;
    
    @Inject
	private Logger logger;
    
    private List<MessageDTO> messageList;
    
    @PostConstruct
    public void postConstruct() {
    	try {
    		this.messageList = feedService.loadFeedForUser(userService.getUser(userSession.getUsername()));
    		for(int i = 0; i < this.messageList.size(); i++) {
    			feedService.setMessageLikes(this.messageList.get(i), userSession.getUsername());
    			feedService.setComments(this.messageList.get(i));
    		}
    	}
    	catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] getActivities failed for " + userSession.getUsername() + " from " + context.toString());
		}
    }
    
    public List<MessageDTO> getMessages () {
    	return messages;
    }
    
    public List<MessageDTO> getActivities() {
   		return messageList;
    }
    
    /**
     * Adds "like" for clicked message in activity stream for the currently logged-in person
     * 
     */
    public void rateMessage() {
    	String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
    	int id = Integer.parseInt(messageId);
    	try {
	    	for(int i = 0; i < messageList.size(); i++) {
	    		if(messageList.get(i).getId() == id) {
	    			Person p = userService.getUser(userSession.getUsername());
	    			MessageDTO messageDTO = messageList.get(i);
	    			UserDTO userDTO = new UserDTO(p);
	    			feedService.rateMessage(id, p);
	    			feedService.updateDTOafterRating(messageDTO, userDTO);
	    			break;
	    		}
	    	}
    	}
    	catch (EntityNotFoundException e) {
    		RequestContext context = RequestContext.getCurrentInstance();
    		logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
    	} catch (ConstraintViolationException e) {
    		RequestContext context = RequestContext.getCurrentInstance();
    		logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
	}
    
    /**
     * Removes "like" for clicked message in activity stream for the currently logged-in person
     * 
     */
    public void removeRating() {
    	String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
    	int id = Integer.parseInt(messageId);
		try {
			for(int j = 0; j < messageList.size(); j++) {
				if(messageList.get(j).getId() == id) {
					Person p = userService.getUser(userSession.getUsername());
					MessageDTO messageDTO = messageList.get(j);
					UserDTO userDTO = new UserDTO(p);
					feedService.removeRating(id, p);
					feedService.updateDTOAfterRemove(messageDTO, userDTO);
					break;
				}
			}
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
    		logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
	}
    
    public List<MessageDTO> getNews(int communityId) {
    	try {
    		List<MessageDTO> messageList = feedService.loadNews(communityId);
    		return messageList;
		} catch (EntityNotFoundException | ConstraintViolationException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] getNews failed for community " + communityId + " from " + context.toString());
			return null;
		}
    	
    }
    
    public List<MessageDTO> getCommunityAcitivities(int communityId) {
    	try {
    		List<MessageDTO> messageList = feedService.loadNews(communityId);
    		for(int i = 0; i < messageList.size(); i++) {
    			feedService.setMessageLikes(messageList.get(i), userSession.getUsername());
    			feedService.setComments(messageList.get(i));
    		}
    		return messageList;
		} catch (EntityNotFoundException | ConstraintViolationException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] getNews failed for community " + communityId + " from " + context.toString());
			return null;
		}
    	
    }

}
