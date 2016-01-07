package at.fhj.swd13.pse.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
@SessionScoped
public class MessageDetailsController {

	@Inject
	UserService userService;
	
	@Inject
	UserSession userSession;
	
	@Inject
	private FeedService feedService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private ChatService chatService;

	private String text;

	private String headline;

	private boolean showPanel = true;
	
	private MessageDTO messageDTO;

	public String openDetailView() {
		String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
		int id = Integer.parseInt(messageId);
		try {
			messageDTO = feedService.getMessageDTOById(id);
			feedService.setImageRef(messageDTO);
			feedService.setMessageLikes(messageDTO, userSession.getUsername());
			feedService.setComments(messageDTO);
			
			if(messageDTO.getComments().size()>0) {
				fillUpComments(messageDTO);
				
			}
			
		} catch (ServiceException e) {
			logger.info("[MESSAGEDETAILS] message with id " + messageId + " not found");
		}
		return "/protected/MessageDetails.jsf";
	}
	
	public MessageDTO getMessageDTO() {
		return messageDTO;
	}
	
	/**
	 * Adds a new Comment to a specific message and updates the messageDTO for correct xhtml render
	 * 
	 */
	public void addComment() {	
		final int parentMessageId = Integer.parseInt( FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentMessageId") );
		logger.info("[CMT] adding comment for " + parentMessageId + " from " + userSession.getUsername() + "  " + text );
		
		Message message = chatService.addComment(userSession.getUsername(), parentMessageId, headline, text);
		MessageDTO newMessageDTO = new MessageDTO(message);
		
		try {
			feedService.setImageRef(newMessageDTO);
			feedService.setMessageLikes(newMessageDTO, userSession.getUsername());
			feedService.setComments(newMessageDTO);
			
			getMessageDTO().getComments().add(newMessageDTO);
			getMessageDTO().setNumberOfComments(getMessageDTO().getComments().size());
			
			headline = "";
			text = "";
			
			showPanel = true;

			FacesContext context = FacesContext.getCurrentInstance();        
	        context.addMessage(null, new FacesMessage("Successful",  "Kommentar wurde gespeichert") );
		} catch (ServiceException e) {
			logger.info("[MESSAGEDETAILS] addComment failed for " + userSession.getUsername());
		}
	}	
	
	/**
	 * Adds "like" from actual message for the person currently logged-in
	 * 
	 */
	public void rateMessageDetailedView() {
		String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");		
	    int id = Integer.parseInt(messageId);
	    
		try {
			Person p = userService.getUser(userSession.getUsername());
			if(getMessageDTO().getId() == id) {
				feedService.rateMessage(getMessageDTO().getId(), p);
				feedService.updateDTOafterRating(getMessageDTO(), p);
			}
			else {
				if(getMessageDTO().getComments().size() > 0) {
					commentsRatingRecursive(getMessageDTO(), id, p);
				}
			}
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[MESSAGEDETAILS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		} catch (ConstraintViolationException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[MESSAGEDETAILS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}

	}

	/**
	 * Removes the "like" from actual message for the person currently logged-in
	 * 
	 */
	 public void removeRatingDetailedView() {
		String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");		
		int id = Integer.parseInt(messageId);
			 
		try {
			Person p = userService.getUser(userSession.getUsername());
			if(getMessageDTO().getId() == id) {
				feedService.removeRating(getMessageDTO().getId(), p);
				feedService.updateDTOAfterRemove(getMessageDTO(), p);
			}
			else {
				if(getMessageDTO().getComments().size() > 0) {
					commentsRemovingRecursive(getMessageDTO(), id, p);
				}
			}
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
		
	}
	 
	/*
	* Helper for recursively fill up the comments of a messageDTO with like-info
	*/
	 
	private void fillUpComments(MessageDTO messageDTO) {
		for(int i = 0;i < messageDTO.getComments().size(); i++) {
			feedService.setMessageLikes(messageDTO.getComments().get(i), userSession.getUsername());
			if(messageDTO.getComments().get(i).getComments().size() > 0) {
				fillUpComments(messageDTO.getComments().get(i));
			}
		}
	}
	 
	/*
	 * Helper for recursively check where the right message (id) is to add the new "like"-info (p) 
	 */
	private void commentsRatingRecursive(MessageDTO message, int id, Person p) throws EntityNotFoundException {
		for(int l = 0; l < message.getComments().size(); l++) {
			if(message.getComments().get(l).getId() == id) {
    			feedService.rateMessage(id, p);
    			feedService.updateDTOafterRating(message.getComments().get(l), p);
    			message.getComments().get(l).setLike(true);
			} else {
				if(message.getComments().get(l).getComments() != null) {
					if(message.getComments().get(l).getComments().size() > 0) {
					commentsRatingRecursive(message.getComments().get(l), id, p);
					}
				}
			}
		}
	}
	
	/*
	 * Helper for recursively check where the right message (id) is to remove the "like"-info (p)
	 */
	private void commentsRemovingRecursive(MessageDTO message, int id, Person p) throws EntityNotFoundException {
		for(int l = 0; l < message.getComments().size(); l++) {
			if(message.getComments().get(l).getId() == id) {
				feedService.removeRating(id, p);
				feedService.updateDTOAfterRemove(message.getComments().get(l), p);
				message.getComments().get(l).setLike(false);
			} else {
				if(message.getComments().get(l).getComments().size() > 0) {
					commentsRemovingRecursive(message.getComments().get(l), id, p);
				}
			}
		}
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the showPanel
	 */
	public boolean isShowPanel() {
		return showPanel;
	}

	/**
	 * @param showPanel
	 *            the showPanel to set
	 */
	public void setShowPanel(boolean showPanel) {
		this.showPanel = showPanel;
	}

	/**
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * @param headline
	 *            the headline to set
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}
}
