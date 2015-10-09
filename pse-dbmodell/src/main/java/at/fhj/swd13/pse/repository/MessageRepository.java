/**
 * 
 */
package at.fhj.swd13.pse.repository;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Message;

/**
 * @author florian.genser
 *
 */
public interface MessageRepository {

	List<Message> loadAll();
}
