/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

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
import at.fhj.swd13.pse.dto.UserDTO;
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
	
	public FeedServiceImpl() {
	}

	public FeedServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public List<MessageDTO> loadFeed() {
		return getMessageDTOsWithImageRefs(dbContext.getMessageDAO().loadAll());
	}

	@Override
	public List<MessageDTO> loadFeedForUser(Person user) {
		return getMessageDTOsWithImageRefs(dbContext.getMessageDAO().loadForUser(user));
	}

	@Override
	public void saveMessage(String headline, String text, String username,
			Document document, Document icon, List<Community> communities, List<MessageTag> messageTags) {
		Message message = new Message();
		message.setHeadline(headline);
		message.setMessage(text);

		try {
			message.setPerson(userService.getUser(username));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}

		Date createdDate = new Date();

		message.setCreatedAt(createdDate);
		message.setValidFrom(createdDate);		

		DeliverySystemDAO deliverySystemDAO = new DeliverySystemDAOImpl(dbContext);
		message.setDeliverySystem(deliverySystemDAO.getPseService());
		
		message.setAttachment(document);
		message.setIcon(icon);

		try {

			dbContext.getMessageDAO().insert(message);
			message.setMessageTags(messageTags);		
			message.setCommunities(communities);
			
		} catch (ConstraintViolationException e) {
			logger.error("[FEED] Could not persist message (ConstraintViolation ??" + message.getHeadline() );
		}
	}

	@Override
	public Message getMessageById(int messageId) throws EntityNotFoundException {
		Message byId = dbContext.getMessageDAO().getById(messageId);
		
		return byId;
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
		Query q = dbContext.createNamedQuery("MessageRating.findRatingByPersonAndMessage");
		q.setParameter("message", m);
		q.setParameter("person", p);
		MessageRating rating = (MessageRating) q.getSingleResult();
		m.removeMesasgeRating(rating);
		p.removeMesasgeRating(rating);
		dbContext.getMessageRatingDAO().remove(rating);
	}

	@Override
	public List<MessageDTO> loadNews(int communityId)
			throws EntityNotFoundException, ConstraintViolationException {
		return getMessageDTOsWithImageRefs(dbContext.getMessageDAO().loadNews(communityId));
	}

	@Override
	public void setMessageLikes(MessageDTO message, String username) {
		List<MessageRating> ratingList = message.getRatingList();
		
		int quantityRatings = ratingList.size();
		List<UserDTO> personList = new ArrayList<UserDTO>();
		
		for(int j = 0; j < quantityRatings; j++) {
			personList.add(new UserDTO(ratingList.get(j).getPerson()));
			if(ratingList.get(j).getPerson().getUserName().equals(username)) {
				message.setLike(true);
			}
		}
		message.setQuantityRatings(quantityRatings);
		message.setRatingPersonsList(personList);
	}

	/**
	 * Creates messageDTOs for the messages and sets the imageRef for each
	 *  
	 * @param messages
	 * 					list of messages
	 * @return
	 * 			list of messageDTOs
	 */
	private List<MessageDTO> getMessageDTOsWithImageRefs(List<Message> messages) {
		List<MessageDTO> result = new ArrayList<MessageDTO>();
		for(Message m: messages) {
			MessageDTO mDTO = new MessageDTO(m);
			setImageRef(mDTO);
			result.add(mDTO);
		}
		return result;
	}
	
	@Override
	public void setImageRef(MessageDTO messageDTO) {
		if(messageDTO.getImage() != null) {
			messageDTO.setImageRef(documentService.buildServiceUrl(messageDTO.getImage().getDocumentId()));
		}
	}
}
