package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.entity.MessageTag;

public interface MessageTagDAO {
	/**
	 * Insert the given messageTag into the persistent store
	 * 
	 * @param messageTag
	 *            the messageTag to persist
	 */
	void insert(MessageTag messageTag);

	/**
	 * Get a MessageTag by its id
	 * 
	 * @param messageTagId
	 *            the id of the MessageTag to fetch
	 * 
	 * @return MessageTag instance or null if none could be found
	 */
	MessageTag getById(int messageTagId);

	/**
	 * Get a list of MessageTag that start with the given string. Search is case
	 * insensitive
	 * 
	 * @param beginning
	 *            the beginning of the MessageTag-token
	 * 
	 * @return a list with matching tokens or an empty list if none were found
	 */
	List<MessageTag> getByTokenLike(String beginning);

	/**
	 * remove a MessageTag identified by its messageTagId
	 * 
	 * @param messageTagId
	 *            id of the MessageTag to remove
	 */
	void remove(int messageTagId);
}
