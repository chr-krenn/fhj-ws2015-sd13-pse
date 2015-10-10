/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Message;

/**
 * @author florian.genser
 *
 */
public interface FeedService {

	List<Message> loadFeed();
}
