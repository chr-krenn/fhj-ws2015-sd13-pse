/**
 * 
 */
package at.fhj.swd13.pse.repository;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * @author florian.genser
 *
 */
public interface MessageRepository {

	List<Message> loadAll();
	
	List<Message> loadForUser(Person user);
}
