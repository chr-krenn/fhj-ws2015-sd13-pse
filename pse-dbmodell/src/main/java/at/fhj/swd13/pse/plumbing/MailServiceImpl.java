package at.fhj.swd13.pse.plumbing;

import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.domain.document.DocumentService;

@Stateless
public class MailServiceImpl implements MailService {

	private static String fromMailAddress = "swd13pse@gmail.com";

	@Inject
	private Logger logger;

	@Resource(mappedName = "java:jboss/mail/gmail")
	private Session session;

	@Inject
	private DocumentService documentService;

	@PostConstruct
	protected void onPostConstruct() {
		fromMailAddress = ConfigurationHelper.saveSetProperty("at.fhj.swd13.pse.mailFrom", fromMailAddress);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.plumbing.MailService#sendMail(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMail(final String subject, final String htmlBody, final String recipient) throws MessagingException {
		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.setFrom(fromMailAddress);
			// FIXME
			mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipient));

			mimeMessage.setSubject("[PSE] " + subject);

			mimeMessage.setContent(htmlBody, "text/html; charsset=utf-8");

			Transport.send(mimeMessage);

		} catch (MessagingException e) {

			logger.error("[MAIL] unable to send message " + subject + " to " + recipient);
			logger.error(e);

			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.plumbing.MailService#sendMail(at.fhj.swd13.pse.db.entity.Message, java.lang.String)
	 */
	@Override
	@Asynchronous
	public void sendMail(final Message message, final String receipientList) throws MessagingException {
		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.setFrom( new InternetAddress( message.getPerson().getEmailAddress(), message.getPerson().getFullName() ) );

			mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(receipientList));

			mimeMessage.setSubject("[PSE] " + message.getHeadline());

			if (message.getAttachment() == null) {

				mimeMessage.setContent(message.getMessage(), "text/html; charsset=utf-8");
			} else {

				Multipart multipart = new MimeMultipart();

				MimeBodyPart part = new MimeBodyPart();
				part.setContent(message.getMessage(), "text/html; charsset=utf-8");
				multipart.addBodyPart(part);

				part = new MimeBodyPart();
				part.setDataHandler(new DataHandler(new FileDataSource(documentService.getServerPath(message.getAttachment()))));
				part.setFileName(message.getAttachment().getName());
				multipart.addBodyPart(part);

				mimeMessage.setContent(multipart);
			}

			Transport.send(mimeMessage);

			logger.info("[MAIL] sent message " + message.getMessageId() + " to " + receipientList);
		} catch (MessagingException e) {
//FIXME: due to async annotation, message is not shown to user!
			logger.error("[MAIL] unable to send message " + message.getMessageId() + " to " + receipientList);
			logger.error(e);

			throw e;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}