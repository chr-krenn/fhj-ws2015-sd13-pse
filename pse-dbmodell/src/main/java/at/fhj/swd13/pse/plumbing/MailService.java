package at.fhj.swd13.pse.plumbing;

import at.fhj.swd13.pse.db.entity.Message;

public interface MailService {

	/**
	 * send an email message to a single receipient 
	 * 
	 * @param subject mail sibject
	 * 
	 * @param htmlBody mail body in html, no <html> or <body> tag
	 * 
	 * @param recipient mail-adress of the receipient
	 */
	void sendMail(String subject, String htmlBody, String recipient);

	void sendMail(Message message, String receipientList);
}