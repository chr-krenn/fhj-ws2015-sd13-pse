package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.service.FeedService;

/**
 * 
 * @author florian.genser
 *
 */
@Model
public class SimpleFeedController {

    @Produces
    @Named
	private List<Message> messages;
    
    @Inject
    private FeedService feedService;
    
    @PostConstruct
    public void postConstruct() {
    	
    	messages = feedService.loadFeed();
    }
}
