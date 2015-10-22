package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;

public interface MessageDAO {
	/**
	 * Insert the given message into the persistent store
	 * 
	 * @param message
	 *            the message to persist
	 */
	void insert(Message message);

	/**
	 * Get a message by its id
	 * 
	 * @param messageId
	 *            the id of the message to fetch
	 * 
	 * @return message instance or null if none could be found
	 */
	Message getById(int messageId);

	/**
	 * remove a message identified by its messageId
	 * 
	 * @param messageId
	 *            id of the message to remove
	 */
	void remove(int messageId);
	
	List<Message> loadAll();
	
	List<Message> loadForUser(Person user);
}
