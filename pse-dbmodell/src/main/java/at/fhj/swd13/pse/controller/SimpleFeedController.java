package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.plumbing.UserSession;

/**
 * 
 * @author florian.genser
 *
 */
@Model
public class SimpleFeedController {

    @Produces
    @Named
    @OrderBy("createdAt ASC")
	private List<Message> messages;
    
    @Inject
    private FeedService feedService;
    
    @Inject
	private UserService userService;
    
    @Inject
	private UserSession userSession;
    
    @Inject
	private Logger logger;
    
    @PostConstruct
    public void postConstruct() {
    	
    	messages = feedService.loadFeed();
    }
    
    public List<Message> getMessages () {
    	return messages;
    }
    
    public List<MessageDTO> getActivities() {
    	try {
    		List<MessageDTO> messageList = feedService.loadFeedForUser(userService.getUser(userSession.getUsername()));
    		List<MessageRating> ratingList;
    		for(int i = 0; i < messageList.size(); i++) {
    			ratingList = messageList.get(i).getRatingList();
    			
    			for(int j = 0; j < ratingList.size(); j++) {
    				if(ratingList.get(j).getPerson().getUserName().equals(userSession.getUsername())) {
    					messageList.get(i).setLike(true);
    				}
    			}
    		}
    		return messageList;
		} catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] getActivities failed for " + userSession.getUsername() + " from " + context.toString());
			return null;
		}
    	
    }
    
    public void rateMessage() {
    	String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
    	int id = Integer.parseInt(messageId);
    	try {
    		feedService.rateMessage(id, userService.getUser(userSession.getUsername()));
    	}
    	catch (EntityNotFoundException e) {
    		RequestContext context = RequestContext.getCurrentInstance();
    		logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
    	} catch (ConstraintViolationException e) {
    		RequestContext context = RequestContext.getCurrentInstance();
    		logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
	}
    
    public void removeRating() {
    	String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
    	int id = Integer.parseInt(messageId);
		try {
			feedService.removeRating(id, userService.getUser(userSession.getUsername()));
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
    		logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
	}
    
    public List<Message> getNews(int communityId) {
    	try {
    		List<Message> messageList = feedService.loadNews(communityId);
    		return messageList;
		} catch (EntityNotFoundException | ConstraintViolationException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] getNews failed for community " + communityId + " from " + context.toString());
			return null;
		}
    	
    }

}
