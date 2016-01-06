package at.fhj.swd13.pse.xperimental;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.mail.MessagingException;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.plumbing.MailService;

@Stateless
@ManagedBean
public class MailerTest {

	@Inject
	private MailService mailService;

	@Inject
	private FeedService feedService;
	
	@Inject 
	private ChatService chatService;

	@Inject
	private Logger logger;

	public void send() {
		logger.info("[XPERIMENTAL] sending email with attachment");

		try {
			
			final Message message = feedService.getMessageById(15); 
			
			final String receipientList = chatService.resolveReceipientsMail( message ); 
			
			mailService.sendMail( message, receipientList, null );
		} catch (MessagingException | EntityNotFoundException e) {
			logger.error("[XPERIMENTAL] error sending message: " + e.getMessage() );
		}
	}
}
