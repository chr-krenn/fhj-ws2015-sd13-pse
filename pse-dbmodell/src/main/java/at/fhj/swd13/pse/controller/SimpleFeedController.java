package at.fhj.swd13.pse.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * 
 * @author florian.genser
 *
 */
@Model
public class SimpleFeedController {

    @Produces
    @Named
	private List<FeedContent> feedContents;
    
    @PostConstruct
    public void postConstruct() {
    	feedContents = new ArrayList<>();
    	
    	FeedContent content = new FeedContent("Hallo");
    	feedContents.add(content);
    	feedContents.add(new FeedContent("Toll"));
    }
    
}
