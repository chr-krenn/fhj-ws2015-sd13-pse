package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OrderBy;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.user.UserService;
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
    @OrderBy("createdOn ASC")
	private List<Message> messages;
    
    @Inject
    private FeedService feedService;
    
    @Inject
	private UserService userService;
    
    @Inject
	private UserSession userSession;
    
    @PostConstruct
    public void postConstruct() {
    	
    	messages = feedService.loadFeed();
    }
    
    public List<Message> getMessages () {
    	return messages;
    }
    
    public List<Message> getActivities() {
    	return feedService.loadFeedForUser(userService.getUser(userSession.getUsername()));
    	
    }
}
