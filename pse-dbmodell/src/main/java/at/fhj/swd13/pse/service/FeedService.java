/**
 * 
 */
package at.fhj.swd13.pse.service;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Message;

/**
 * @author florian.genser
 *
 */
public interface FeedService {

	List<Message> loadFeed();
}
