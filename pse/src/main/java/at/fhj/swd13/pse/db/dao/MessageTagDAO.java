package at.fhj.swd13.pse.db.dao;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.entity.MessageTag;

public interface MessageTagDAO {
	/**
	 * Insert the given messageTag into the persistent store
	 * 
	 * @param messageTag
	 *            the messageTag to persist
	 */
	void insert(MessageTag messageTag) throws ConstraintViolationException;

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
	 * remove a MessageTag identified by its messageTagId
	 * 
	 * @param messageTagId
	 *            id of the MessageTag to remove
	 */
	void remove(int messageTagId);
}
