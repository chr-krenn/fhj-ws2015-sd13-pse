package at.fhj.swd13.pse.domain.feed;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.dto.MessageDTO;

@Alternative
@Stateless
@Remote(FeedService.class)
public class FeedServiceFacade implements FeedService {

	@EJB(beanName="FeedServiceImpl")
	private FeedService feedService;
	
	@Override
	public List<MessageDTO> loadFeed() {
		return feedService.loadFeed();
	}

	@Override
	public List<MessageDTO> loadFeedForUser(Person user) {
		return feedService.loadFeedForUser(user);
	}

	@Override
	public void saveMessage(String headline, String text, String username, Document document, Document icon, List<Community> communities,
			List<MessageTag> messageTags, Date validFrom, Date validUntil) {
		feedService.saveMessage(headline, text, username, document, icon, communities, messageTags, validFrom, validUntil);
	}

	@Override
	public void updateMessage(int messageId, String headline, String text, Document document, Document icon, List<MessageTag> messageTags, Date validFrom,
			Date validUntil) {
		feedService.updateMessage(messageId, headline, text, document, icon, messageTags, validFrom, validUntil);
	}

	@Override
	public Message getMessageById(int messageId) {
		return feedService.getMessageById(messageId);
	}

	@Override
	public MessageDTO getMessageDTOById(int messageId) {
		return feedService.getMessageDTOById(messageId);
	}

	@Override
	public void rateMessage(int messageId, Person person) {
		feedService.rateMessage(messageId, person);
	}

	@Override
	public void removeRating(int messageId, Person person) {
		feedService.removeRating(messageId, person);
	}

	@Override
	public List<MessageDTO> loadNews(int communityId) {
		return feedService.loadNews(communityId);
	}

	@Override
	public MessageDTO setMessageLikes(MessageDTO message, String username) {
		return feedService.setMessageLikes(message, username);
	}

	@Override
	public MessageDTO setImageRef(MessageDTO messageDTO) {
		return feedService.setImageRef(messageDTO);
	}

	@Override
	public List<MessageDTO> loadComments(int messageId) {
		return feedService.loadComments(messageId);
	}

	@Override
	public MessageDTO setComments(MessageDTO messageDTO) {
		return feedService.setComments(messageDTO);
	}

	@Override
	public MessageDTO updateDTOafterRating(MessageDTO messageDTO, Person person) {
		return feedService.updateDTOafterRating(messageDTO, person);
	}

	@Override
	public MessageDTO updateDTOAfterRemove(MessageDTO messageDTO, Person person) {
		return feedService.updateDTOAfterRemove(messageDTO, person);
	}

	@Override
	public void removeMessage(int messageId) {
		feedService.removeMessage(messageId);
	}

}
