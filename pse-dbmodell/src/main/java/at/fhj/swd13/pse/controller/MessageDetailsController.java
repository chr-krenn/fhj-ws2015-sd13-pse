package at.fhj.swd13.pse.controller;

import java.util.List;

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
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.dto.UserDTO;
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

	private MessageDTO messageDTO;

	public String openDetailView() {
		String messageId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("messageId");
		int id = Integer.parseInt(messageId);
		try {
			Message message = feedService.getMessageById(id);
			messageDTO = new MessageDTO(message);
			feedService.setImageRef(messageDTO);
			feedService.setMessageLikes(messageDTO, userSession.getUsername());
			feedService.setComments(messageDTO);
			
			if(messageDTO.getComments().size()>0) {
				fillUpComments(messageDTO);
				
			}
			
		} catch (EntityNotFoundException e) {
			logger.info("[MESSAGEDETAILS] message with id " + messageId + " not found");
		}
		return "/protected/MessageDetails.jsf";
	}
	
	public MessageDTO getMessageDTO() {
		return messageDTO;
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
			UserDTO userDTO = new UserDTO(p);
			if(getMessageDTO().getId() == id) {
				feedService.rateMessage(getMessageDTO().getId(), p);
				feedService.updateDTOafterRating(getMessageDTO(), userDTO);
			}
			else {
				if(getMessageDTO().getComments().size() > 0) {
					commentsRatingRecursive(getMessageDTO(), id, p, userDTO);
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
			UserDTO userDTO = new UserDTO(p);
			if(getMessageDTO().getId() == id) {
				feedService.removeRating(getMessageDTO().getId(), p);
				feedService.updateDTOAfterRemove(getMessageDTO(), userDTO);
			}
			else {
				if(getMessageDTO().getComments().size() > 0) {
					commentsRemovingRecursive(getMessageDTO(), id, p, userDTO);
				}
			}
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
		
	}

	 public void reloadMessage() {
		 try {
			feedService.setComments(messageDTO);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	 }
	 
	 private void fillUpComments(MessageDTO messageDTO) {
		for(int i = 0;i < messageDTO.getComments().size(); i++) {
			feedService.setMessageLikes(messageDTO.getComments().get(i), userSession.getUsername());
			if(messageDTO.getComments().get(i).getComments().size() > 0) {
				fillUpComments(messageDTO.getComments().get(i));
			}
		}
	}
	private void commentsRatingRecursive(MessageDTO message, int id, Person p, UserDTO userDTO) throws EntityNotFoundException, ConstraintViolationException {
		for(int l = 0; l < message.getComments().size(); l++) {
			if(message.getComments().get(l).getId() == id) {
    			feedService.rateMessage(id, p);
    			feedService.updateDTOafterRating(message.getComments().get(l), userDTO);
    			message.getComments().get(l).setLike(true);
			} else {
				if(message.getComments().get(l).getComments().size() > 0) {
					commentsRatingRecursive(message.getComments().get(l), id, p, userDTO);
				}
			}
		}
	}
	
	private void commentsRemovingRecursive(MessageDTO message, int id, Person p, UserDTO userDTO) throws EntityNotFoundException {
		for(int l = 0; l < message.getComments().size(); l++) {
			if(message.getComments().get(l).getId() == id) {
				feedService.removeRating(id, p);
				feedService.updateDTOAfterRemove(message.getComments().get(l), userDTO);
				message.getComments().get(l).setLike(false);
			} else {
				if(message.getComments().get(l).getComments().size() > 0) {
					commentsRemovingRecursive(message.getComments().get(l), id, p, userDTO);
				}
			}
		}
	}
}
