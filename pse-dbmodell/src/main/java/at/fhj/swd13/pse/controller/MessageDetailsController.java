package at.fhj.swd13.pse.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.dto.MessageDTO;

@ManagedBean
@ViewScoped
public class MessageDetailsController {

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
		} catch (EntityNotFoundException e) {
			logger.info("[MESSAGEDETAILS] message with id " + messageId + " not found");
		}
		return "/protected/MessageDetails.jsf";
	}
	
	public MessageDTO getMessageDTO() {
		return messageDTO;
	}
}
