/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.DeliverySystemDAO;
import at.fhj.swd13.pse.db.dao.DeliverySystemDAOImpl;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * @author florian.genser
 *
 */
@Stateless
public class FeedServiceImpl extends ServiceBase implements FeedService {

	@Inject
	private UserService userService;

	@Inject
	private DocumentService documentService;

	@Inject
	private Logger logger;

	
	public FeedServiceImpl() {}

	public FeedServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public List<MessageDTO> loadFeed() {
		return getMessageDTOs(dbContext.getMessageDAO().loadAll());
	}

	@Override
	public List<MessageDTO> loadFeedForUser(Person user) {
		return getMessageDTOs(dbContext.getMessageDAO().loadForUser(user));
	}

	@Override
	public void saveMessage(String headline, String text, String username, Document document, Document icon, List<Community> communities,
			List<MessageTag> messageTags, final Date validFrom, final Date validUntil) throws EntityNotFoundException {
		Message message = new Message();
		message.setHeadline(headline);
		message.setMessage(text);

		message.setValidFrom(validFrom == null ? new Date() : validFrom);
		message.setExpiresOn(validUntil);

		message.setPerson(userService.getUser(username));

		Date createdDate = new Date();

		message.setCreatedAt(createdDate);

		DeliverySystemDAO deliverySystemDAO = new DeliverySystemDAOImpl(dbContext);
		message.setDeliverySystem(deliverySystemDAO.getPseService());

		message.setAttachment(document);
		message.setIcon(icon);

		try {

			dbContext.getMessageDAO().insert(message);
			message.setMessageTags(messageTags);
			message.setCommunities(communities);

		} catch (ConstraintViolationException e) {
			logger.error("[FEED] Could not persist message (ConstraintViolation ??" + message.getHeadline());
		}
	}

	@Override
	public void updateMessage(int messageId, String headline, String text, Document document, Document icon,
			List<MessageTag> messageTags, final Date validFrom, final Date validUntil) throws EntityNotFoundException{
		Message m = dbContext.getMessageDAO().getById(messageId);
		
		m.setHeadline(headline);
		m.setMessage(text);
		m.setIcon(icon);
		m.setAttachment(document);
		//TODO update message tags
		//m.setMessageTags(messageTags);	
		m.setValidFrom(validFrom);
		m.setExpiresOn(validUntil);

		try {
			dbContext.getMessageDAO().update(m);
			
		} catch (ConstraintViolationException e) {
			logger.error("[FEED] Could not persist message (ConstraintViolation ??" + m.getHeadline());
		}
		
	}
	
	@Override
	public Message getMessageById(int messageId) throws EntityNotFoundException {
		Message byId = dbContext.getMessageDAO().getById(messageId);

		return byId;
	}

	@Override
	public MessageDTO getMessageDTOById(int messageId) throws EntityNotFoundException {
		MessageDTO messageDto = new MessageDTO(dbContext.getMessageDAO().getById(messageId));
		return messageDto;
	}

	@Override
	public void rateMessage(int messageId, Person person) throws EntityNotFoundException, ConstraintViolationException {
		Date createdDate = new Date();
		Message m = dbContext.getMessageDAO().getById(messageId);
		Person p = dbContext.getPersonDAO().getById(person.getPersonId());
		MessageRating rating = new MessageRating();
		rating.setMessage(m);
		rating.setPerson(p);
		rating.setCreatedAt(createdDate);
		m.addMesasgeRating(rating);
		p.addMesasgeRating(rating);
		dbContext.getMessageRatingDAO().insert(rating);
	}

	@Override
	public void removeRating(int messageId, Person person) throws EntityNotFoundException {
		Message m = dbContext.getMessageDAO().getById(messageId);
		Person p = dbContext.getPersonDAO().getById(person.getPersonId());
		MessageRating rating = dbContext.getMessageRatingDAO().findRatingByPersonAndMessage(m, p);
		m.removeMesasgeRating(rating);
		p.removeMesasgeRating(rating);
		dbContext.getMessageRatingDAO().remove(rating);
	}

	@Override
	public List<MessageDTO> loadNews(int communityId) throws EntityNotFoundException, ConstraintViolationException {
		return getMessageDTOs(dbContext.getMessageDAO().loadNews(communityId));
	}

	@Override
	public void setMessageLikes(MessageDTO message, String username) {
		List<MessageRating> ratingList = message.getRatingList();
		List<Person> personList = new ArrayList<Person>();
		
		if(ratingList == null) {
			ratingList = new ArrayList<MessageRating>();
			message.setQuantityRatings(0);
			message.setRatingPersonsList(personList);
		}
		else {
			int quantityRatings = ratingList.size();
			
			for (int j = 0; j < quantityRatings; j++) {
				personList.add(ratingList.get(j).getPerson());
				if (ratingList.get(j).getPerson().getUserName().equals(username)) {
					message.setLike(true);
				}
			}
			message.setQuantityRatings(quantityRatings);
			message.setRatingPersonsList(personList);
		}
	}

	@Override
	public List<MessageDTO> loadComments(int messageId) throws EntityNotFoundException {
		return getMessageDTOs(dbContext.getMessageDAO().loadComments(getMessageById(messageId)));
	}

	private List<MessageDTO> getMessageDTOs(List<Message> messages) {
		return getMessageDTOs(messages, true, true);
	}

	/**
	 * Creates messageDTOs for the messages
	 * Optionally sets the ImageRef
	 * and load the comments for each message
	 * 
	 * @param messages
	 * @param loadImageRefs
	 * @param loadComments
	 * @return
	 */
	private List<MessageDTO> getMessageDTOs(List<Message> messages, boolean loadImageRefs, boolean loadComments) {
		List<MessageDTO> result = new ArrayList<MessageDTO>();
		for (Message m : messages) {
			MessageDTO mDTO = new MessageDTO(m);
			if (loadImageRefs) {
				setImageRef(mDTO);
			}
			if (loadComments) {
				try {
					setComments(mDTO);
				} catch (EntityNotFoundException e) {
					logger.warn("[FEED] Could not load Comments for message with ID " + "[" + mDTO.getId() + "] because entity was not found");
				}
			}
			result.add(mDTO);
		}
		return result;
	}

	@Override
	public void setImageRef(MessageDTO messageDTO) {
		if (messageDTO.getImage() != null) {
			messageDTO.setImageRef(documentService.buildServiceUrl(messageDTO.getImage().getDocumentId()));
		}
	}

	@Override
	public void setComments(MessageDTO messageDTO) throws EntityNotFoundException {
		messageDTO.setComments(loadComments(messageDTO.getId()));

	}

	@Override
	public void updateDTOafterRating(MessageDTO messageDTO, Person person) {
		messageDTO.getRatingPersonsList().add(person);
		messageDTO.setLike(true);
		messageDTO.setQuantityRatings(messageDTO.getRatingPersonsList().size());
	}

	@Override
	public void updateDTOAfterRemove(MessageDTO messageDTO, Person person) {
		List<Person> ratingPersonsList = messageDTO.getRatingPersonsList();
		for (int i = 0; i < ratingPersonsList.size(); i++) {
			if (ratingPersonsList.get(i).getUserName().contentEquals(person.getUserName())) {
				messageDTO.getRatingPersonsList().remove(i);
				break;
			}
		}
		messageDTO.setLike(false);
		messageDTO.setQuantityRatings(messageDTO.getRatingPersonsList().size());
	}

	@Override
	public void removeMessage(int messageId) {
		dbContext.getMessageDAO().remove(messageId);
	}

}
