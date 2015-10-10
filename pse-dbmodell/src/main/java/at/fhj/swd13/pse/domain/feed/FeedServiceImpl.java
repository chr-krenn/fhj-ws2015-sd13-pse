/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.List;

import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.repository.MessageRepository;

/**
 * @author florian.genser
 *
 */
public class FeedServiceImpl implements FeedService {

	@Inject
	private MessageRepository messageRepository;
	
	@Override
	public List<Message> loadFeed() {
		return messageRepository.loadAll();
	}
}
