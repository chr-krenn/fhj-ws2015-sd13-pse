/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * @author florian.genser
 *
 */
public interface FeedService {

	List<Message> loadFeed();
	
	List<Message> loadFeedForUser(Person user);
	
	void saveMessage(String headline, String text, String username);
}
