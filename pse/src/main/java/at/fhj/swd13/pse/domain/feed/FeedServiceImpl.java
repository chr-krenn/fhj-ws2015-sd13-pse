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
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.MessageDTO;
import at.fhj.swd13.pse.plumbing.ArgumentChecker;
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

		try {

			return getMessageDTOs(dbContext.getMessageDAO().loadAll());

		} catch (Throwable ex) {
			logger.info("[FeedService] cannot load feed : " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<MessageDTO> loadFeedForUser(Person user) {

		try {
			return getMessageDTOs(dbContext.getMessageDAO().loadForUser(user));
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot load feed for use " + user.getFullName() + " : " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void saveMessage(String headline, String text, String username, Document document, Document icon, List<Community> communities,
			List<MessageTag> messageTags, final Date validFrom, final Date validUntil) throws EntityNotFoundException {

		try {

			Date validFromDate = validFrom == null ? new Date() : validFrom;

			Message message = new Message(new Date(), headline, text, validFromDate, dbContext.getDeliverySystemDAO().getPseService(),
					userService.getUser(username));
			message.setExpiresOn(validUntil);
			message.setAttachment(document);
			message.setIcon(icon);
			dbContext.getMessageDAO().insert(message);
			message.setMessageTags(messageTags);
			message.setCommunities(communities);

		} catch (ConstraintViolationException e) {
			logger.error("[FEED] Could not persist message (ConstraintViolation ??" + headline);
			throw new ServiceException(e);
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot save message '" + headline + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void updateMessage(int messageId, String headline, String text, Document document, Document icon, List<MessageTag> messageTags, final Date validFrom,
			final Date validUntil) throws EntityNotFoundException {
		try {
			Message m = dbContext.getMessageDAO().getById(messageId);

			m.setHeadline(headline);
			m.setMessage(text);
			m.setIcon(icon);
			m.setAttachment(document);
			// TODO update message tags
			// m.setMessageTags(messageTags);
			m.setValidFrom(validFrom);
			m.setExpiresOn(validUntil);

			dbContext.getMessageDAO().update(m);

		} catch (ConstraintViolationException e) {
			logger.error("[FEED] Could not persist message (ConstraintViolation ??" + headline);
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot update message '" + headline + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}

	}

	@Override
	public Message getMessageById(int messageId) throws EntityNotFoundException {
		try {

			Message byId = dbContext.getMessageDAO().getById(messageId);

			return byId;
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot get message '" + messageId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}

	}

	@Override
	public MessageDTO getMessageDTOById(int messageId) throws EntityNotFoundException {
		try {
			MessageDTO messageDto = new MessageDTO(dbContext.getMessageDAO().getById(messageId));
			return messageDto;
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot get message '" + messageId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void rateMessage(int messageId, Person person) throws EntityNotFoundException {
		try {
			ArgumentChecker.assertNotNull(person, "person");

			Date createdDate = new Date();
			Message m = dbContext.getMessageDAO().getById(messageId);
			Person p = dbContext.getPersonDAO().getById(person.getPersonId());
			MessageRating rating = new MessageRating(createdDate, m, p);
			m.addMesasgeRating(rating);
			p.addMesasgeRating(rating);
			dbContext.getMessageRatingDAO().insert(rating);
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot rate message '" + messageId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void removeRating(int messageId, Person person) throws EntityNotFoundException {
		try {
			ArgumentChecker.assertNotNull(person, "person");

			Message m = dbContext.getMessageDAO().getById(messageId);
			Person p = dbContext.getPersonDAO().getById(person.getPersonId());
			MessageRating rating = dbContext.getMessageRatingDAO().findRatingByPersonAndMessage(m, p);
			m.removeMesasgeRating(rating);
			p.removeMesasgeRating(rating);
			dbContext.getMessageRatingDAO().remove(rating);
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot remove rating for message '" + messageId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<MessageDTO> loadNews(int communityId) throws EntityNotFoundException {
		try {
			return getMessageDTOs(dbContext.getMessageDAO().loadNews(communityId));
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot load news for community '" + communityId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public MessageDTO setMessageLikes(MessageDTO messageDto, String username) {
		try {
			ArgumentChecker.assertNotNull(messageDto, "messageDTO");
			ArgumentChecker.assertContent(username, "username");

			List<MessageRating> ratingList = messageDto.getRatingList();
			List<Person> personList = new ArrayList<Person>();

			if (ratingList == null) {
				ratingList = new ArrayList<MessageRating>();
				messageDto.setQuantityRatings(0);
				messageDto.setRatingPersonsList(personList);
			} else {
				int quantityRatings = ratingList.size();

				for (int j = 0; j < quantityRatings; j++) {
					personList.add(ratingList.get(j).getPerson());
					if (ratingList.get(j).getPerson().getUserName().equals(username)) {
						messageDto.setLike(true);
					}
				}
				messageDto.setQuantityRatings(quantityRatings);
				messageDto.setRatingPersonsList(personList);
			}
			return messageDto;
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot set message likes for message '" + messageDto.getId() + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<MessageDTO> loadComments(int messageId) throws EntityNotFoundException {
		try {
			return getMessageDTOs(dbContext.getMessageDAO().loadComments(getMessageById(messageId)));
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot load comments for message '" + messageId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	private List<MessageDTO> getMessageDTOs(List<Message> messages) {
		try {
			return getMessageDTOs(messages, true, true);
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot create message dto's :" + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
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
		try {
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
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot create message DTOs: " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public MessageDTO setImageRef(MessageDTO messageDTO) {
		try {
			ArgumentChecker.assertNotNull(messageDTO, "messageDTO");

			if (messageDTO.getImage() != null) {
				messageDTO.setImageRef(documentService.buildServiceUrl(messageDTO.getImage().getDocumentId()));
			}
			return messageDTO;
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot set image for message '" + messageDTO.getId() + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public MessageDTO setComments(MessageDTO messageDTO) throws EntityNotFoundException {
		try {
			ArgumentChecker.assertNotNull(messageDTO, "messageDO");

			messageDTO.setComments(loadComments(messageDTO.getId()));
			return messageDTO;
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot set comments for message '" + messageDTO.getId() + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void updateDTOafterRating(MessageDTO messageDTO, Person person) {
		try {
			ArgumentChecker.assertNotNull(messageDTO, "messageDO");
			ArgumentChecker.assertNotNull(person, "person");

			messageDTO.getRatingPersonsList().add(person);
			messageDTO.setLike(true);
			messageDTO.setQuantityRatings(messageDTO.getRatingPersonsList().size());
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot update message '" + messageDTO.getId() + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void updateDTOAfterRemove(MessageDTO messageDTO, Person person) {
		try {
			ArgumentChecker.assertNotNull(messageDTO, "messageDO");
			ArgumentChecker.assertNotNull(person, "person");

			List<Person> ratingPersonsList = messageDTO.getRatingPersonsList();
			for (int i = 0; i < ratingPersonsList.size(); i++) {
				if (ratingPersonsList.get(i).getUserName().contentEquals(person.getUserName())) {
					messageDTO.getRatingPersonsList().remove(i);
					break;
				}
			}
			messageDTO.setLike(false);
			messageDTO.setQuantityRatings(messageDTO.getRatingPersonsList().size());
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot update message '" + messageDTO.getId() + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@Override
	public void removeMessage(int messageId) {
		try {
			dbContext.getMessageDAO().remove(messageId);
		} catch (Throwable ex) {
			logger.info("[FeedService] cannot remove message '" + messageId + "': " + ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

}
