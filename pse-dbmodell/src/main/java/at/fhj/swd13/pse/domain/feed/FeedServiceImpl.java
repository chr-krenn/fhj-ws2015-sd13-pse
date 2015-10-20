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
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.DeliverySystem;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
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
	
	public FeedServiceImpl(DbContext dbContext){
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
	public void saveMessage(String headline, String text, String username, Document document, Community community) {
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
		
		// TODO Correctly handle delivery system
		DeliverySystem deliverySystem = new DeliverySystem();
		deliverySystem.setName("TEST");
		deliverySystem.setToken("TEST");
		dbContext.persist(deliverySystem);
		
		message.setDeliverySystem(deliverySystem);
		message.setAttachment(document);
		message.setCommunity(community);
		
		dbContext.getMessageDAO().insert(message);
	}
}
