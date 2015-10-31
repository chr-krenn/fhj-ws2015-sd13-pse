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
import at.fhj.swd13.pse.dto.UserDTO;

/**
 * @author florian.genser
 *
 */
public interface FeedService {

	List<MessageDTO> loadFeed();
	
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
	List<MessageDTO> loadNews(int communityId) throws EntityNotFoundException, ConstraintViolationException;
	
	/**
	 * Check every message whether the logged-in person has liked it and check quantity of likes and prepare list of liking persons for that message 
	 * 
	 * @param message
	 * @param username
	 */
	void setMessageLikes(MessageDTO message, String username);
	
	/**
	 * The image ref is not available in the message itself and must therefore be
	 * set separately after creating the messageDTO based on the message
	 * 
	 * @param messageDTO
	 */
	void setImageRef(MessageDTO messageDTO);
	
	/**
	 * Returns all comments for a message
	 * 
	 * @param messageId
	 * @return
	 * 			list of messageDTOs
	 * @throws EntityNotFoundException
	 */
	List<MessageDTO> loadComments(int messageId) throws EntityNotFoundException;
	
	/**
	 * Sets the comments in the messageDTO
	 * 
	 * @param messageDTO
	 */
	void setComments(MessageDTO messageDTO) throws EntityNotFoundException;

	/**
	 * Update DTO object after rating for correctly render xhtml
	 * 
	 * @param messageDTO
	 * @param userDTO
	 */
	void updateDTOafterRating(MessageDTO messageDTO, UserDTO userDTO);

	/**
	 * Update DTO object after removing rate for correctly render xhtml
	 * 
	 * @param messageDTO
	 * @param userDTO
	 */
	void updateDTOAfterRemove(MessageDTO messageDTO, UserDTO userDTO);
	
}
