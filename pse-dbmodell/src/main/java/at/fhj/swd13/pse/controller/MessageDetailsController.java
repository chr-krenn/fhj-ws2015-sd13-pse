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
	public String rateMessageDetailedView() {
		try {
			Person p = userService.getUser(userSession.getUsername());
			UserDTO userDTO = new UserDTO(p);
			feedService.rateMessage(getMessageDTO().getId(), p);
			getMessageDTO().setLike(true);
			getMessageDTO().setQuantityRatings(messageDTO.getQuantityRatings()+1);
			getMessageDTO().getRatingPersonsList().add(userDTO);
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[MESSAGEDETAILS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		} catch (ConstraintViolationException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[MESSAGEDETAILS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
		
		return "/protected/MessageDetails.jsf";
	}

	/**
	 * Removes the "like" from actual message for the person currently logged-in
	 * 
	 */
	 public String removeRatingDetailedView() {
		try {
			Person p = userService.getUser(userSession.getUsername());
			feedService.removeRating(getMessageDTO().getId(), p);
			getMessageDTO().setLike(false);
			getMessageDTO().setQuantityRatings(messageDTO.getQuantityRatings()-1);
			List<UserDTO> ratingPersonsList = getMessageDTO().getRatingPersonsList();

			for(int i = 0; i < ratingPersonsList.size(); i++) {
				if(ratingPersonsList.get(i).getUserName().contentEquals(p.getUserName())) {
					ratingPersonsList.remove(i);
					break;
				}
			}
		}
		catch (EntityNotFoundException e) {
			RequestContext context = RequestContext.getCurrentInstance();
			logger.info("[FEEDS] rateMessage failed for " + userSession.getUsername() + " from " + context.toString());
		}
		
		return "/protected/MessageDetails.jsf";
	}

}
