package at.fhj.swd13.pse.plumbing;

import java.util.Properties;

import javax.mail.MessagingException;

import at.fhj.swd13.pse.db.entity.Message;

public interface MailService {

	/**
	 * send an email message to a single receipient
	 * 
	 * @param subject
	 *            mail sibject
	 * 
	 * @param htmlBody
	 *            mail body in html, no <html> or <body> tag
	 * 
	 * @param recipient
	 *            mail-adress of the receipient
	 */
	void sendMail(String subject, String htmlBody, String recipient, Properties props) throws MessagingException;

	void sendMail(Message message, String receipientList, Properties props) throws MessagingException;
}