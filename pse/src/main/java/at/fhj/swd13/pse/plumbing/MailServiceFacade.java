package at.fhj.swd13.pse.plumbing;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.mail.MessagingException;

import at.fhj.swd13.pse.db.entity.Message;

@Alternative
@Stateless
@Remote(MailService.class)
public class MailServiceFacade implements MailService {

	@EJB(beanName = "MailServiceImpl")
	private MailService mailService;
	
	@Override
	public void sendMail(String subject, String htmlBody, String recipient, Properties props) throws MessagingException {
		mailService.sendMail(subject, htmlBody, recipient, props);
	}

	@Override
	public void sendMail(Message message, String receipientList, Properties props) throws MessagingException {
		mailService.sendMail(message, receipientList, props);
	}
}
