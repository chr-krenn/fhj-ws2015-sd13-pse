/**
 * 
 */
package at.fhj.swd13.pse.domain.feed;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.DeliverySystemDAO;
import at.fhj.swd13.pse.db.dao.DeliverySystemDAOImpl;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.DeliverySystem;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * @author florian.genser
 *
 */
@Stateless
public class FeedServiceImpl extends ServiceBase implements FeedService {

	@Inject
	private UserService userService;

	public FeedServiceImpl() {
	}

	public FeedServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public List<Message> loadFeed() {
		return dbContext.getMessageDAO().loadAll();
	}

	@Override
	public List<Message> loadFeedForUser(Person user) {
		return dbContext.getMessageDAO().loadForUser(user);
	}

	@Override
	public void saveMessage(String headline, String text, String username,
			Document document, Document icon, List<Community> communities, List<MessageTag> messageTags) {
		Message message = new Message();
		message.setHeadline(headline);
		message.setMessage(text);

		try {
			message.setPerson(userService.getUser(username));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}

		Date createdDate = new Date();

		message.setCreatedAt(createdDate);
		message.setCreatedOn(createdDate);
		message.setValidFrom(createdDate);		

		DeliverySystemDAO deliverySystemDAO = new DeliverySystemDAOImpl(dbContext);
		message.setDeliverySystem(deliverySystemDAO.getPseService());
		
		message.setAttachment(document);
		message.setIcon(icon);

		dbContext.getMessageDAO().insert(message);
		message.setMessageTags(messageTags);		
		message.setCommunities(communities);	
	}
}
