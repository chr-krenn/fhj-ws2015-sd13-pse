package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.Person;

public interface MessageRatingDAO {

	/**
	 * Add a rating (like) to a message to the persistent storage
	 * 
	 * @param messageRating
	 * 
	 */
	void insert(MessageRating rating) throws ConstraintViolationException;
	
	/**
	 * Remove a "like" from a message from persistent storage
	 * 
	 * @param messageRating
	 */
	void remove(MessageRating rating);
	
	/**
	 * Remove a "like" by Id from a message from persistent storage
	 * 
	 * @param messageRatingID
	 */
	void remove(final int ratingId);

	/**
	 * Find all raters by specific message
	 * 
	 * @param message
	 */
	List<Person> loadAllRatersByMessage(Message message);

	/**
	 * Find rating by specific message and person
	 * 
	 * @param message
	 * @param person
	 * @return rating
	 */
	MessageRating findRatingByPersonAndMessage(Message message, Person person);
}
