package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.event.SelectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.domain.chat.ChatService;

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
	private Logger logger;
    
	private transient Community selectedRequest = null;
	
    private String searchFieldText = "";

	@PostConstruct
    public void postConstruct() {
    	requests = chatService.getAllCommunities();
    }
    
    public List<Community> getCommunities () {
    	return requests;
    }
	
	public String search() {
	 	requests = chatService.getAllCommunities(searchFieldText);
    	return "requests";
    }
	
	public Community getSelectedCommunity() {
		return selectedRequest;
	}

	public void setSelectedCommunity(Community selectedRequest) {
		this.selectedRequest = selectedRequest;
	}
	
	public void onRequestSelected(SelectEvent object){
    
    }
	    
}
