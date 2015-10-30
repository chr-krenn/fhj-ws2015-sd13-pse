/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.List;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.dto.MessageDTO;

/**
 * @author florian.genser
 *
 */
public interface FeedService {

	List<Message> loadFeed();
	
	List<MessageDTO> loadFeedForUser(Person user);
	
	void saveMessage(String headline, String text, String username, Document document, Document icon, List<Community> communities, List<MessageTag> messageTags);
	
	Message getMessageById(int messageId) throws EntityNotFoundException;

	/**
	 * Insert a "like" to a message by a specific person into database
	 * 
	 * @param messageId
	 * @param person
	 * @throws EntityNotFoundException
	 * @throws ConstraintViolationException
	 */
	void rateMessage(int messageId, Person person) throws EntityNotFoundException, ConstraintViolationException;

	/**
	 * Removing a "like" to a message by a specific person from database
	 * 
	 * @param messageId
	 * @param person
	 * @throws EntityNotFoundException
	 */
	void removeRating(int messageId, Person person) throws EntityNotFoundException;
	
	/**
	 * Getting the news from database
	 * 
	 * @param communityId
	 * @throws EntityNotFundException
	 * @throws ConstraintViolationException
	 */
	List<Message> loadNews(int communityId) throws EntityNotFoundException, ConstraintViolationException;
	
	/**
	 * Check every message whether the logged-in person has liked it and check quantity of likes and prepare list of liking persons for that message 
	 * 
	 * @param message
	 * @param username
	 */
	void setMessageLikes(MessageDTO message, String username);
	
}
