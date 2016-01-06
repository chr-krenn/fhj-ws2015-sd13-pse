/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.Date;
import java.util.List;

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

	List<MessageDTO> loadFeed();

	List<MessageDTO> loadFeedForUser(Person user);

	void saveMessage(String headline, String text, String username, Document document, Document icon, List<Community> communities, List<MessageTag> messageTags,
			Date validFrom, Date validUntil) throws EntityNotFoundException;

	void updateMessage(int messageId, String headline, String text, Document document, Document icon,
			List<MessageTag> messageTags, final Date validFrom, final Date validUntil) throws EntityNotFoundException;
	
	Message getMessageById(int messageId) throws EntityNotFoundException;

	MessageDTO getMessageDTOById(int messageId) throws EntityNotFoundException;

	/**
	 * Insert a "like" to a message by a specific person into database
	 * 
	 * @param messageId
	 * @param person
	 * @throws EntityNotFoundException
	 */
	void rateMessage(int messageId, Person person) throws EntityNotFoundException;

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
	 */
	List<MessageDTO> loadNews(int communityId) throws EntityNotFoundException;

	/**
	 * Check every message whether the logged-in person has liked it and check quantity of likes and prepare list of liking persons for that message
	 * 
	 * @param message
	 * @param username
	 * @return
	 * 		messageDTO with ratingList, quantityRatings and like set
	 */
	MessageDTO setMessageLikes(MessageDTO message, String username);

	/**
	 * The image ref is not available in the message itself and must therefore be
	 * set separately after creating the messageDTO based on the message
	 * 
	 * @return
	 * 		messageDTO with image ref
	 * @param messageDTO
	 */
	MessageDTO setImageRef(MessageDTO messageDTO);

	/**
	 * Returns all comments for a message
	 * 
	 * @param messageId
	 * @return
	 * 		list of messageDTOs
	 * @throws EntityNotFoundException
	 */
	List<MessageDTO> loadComments(int messageId) throws EntityNotFoundException;

	/**
	 * Sets the comments in the messageDTO
	 * 
	 * @param messageDTO
	 * @return
	 * 		messageDTO with comments
	 * @throws EntityNotFoundException
	 */
	MessageDTO setComments(MessageDTO messageDTO) throws EntityNotFoundException;

	/**
	 * Update DTO object after rating for correctly render xhtml
	 * 
	 * @param messageDTO
	 * @param person
	 */
	void updateDTOafterRating(MessageDTO messageDTO, Person person);

	/**
	 * Update DTO object after removing rate for correctly render xhtml
	 * 
	 * @param messageDTO
	 * @param person
	 */
	void updateDTOAfterRemove(MessageDTO messageDTO, Person person);

	/**
	 * Deletes a message from the database
	 * 
	 * @param messageId
	 */
	void removeMessage(int messageId);
}
