package at.fhj.swd13.pse.application;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailController {


	Properties properties;
	Session session;

	//TODO: Remove hard coded properties
	
	public EmailController() {
		
		properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("pseswd@gmail.com", "pse12345678");
			}
		});
	}

	public void sendNewPassword(String emailAddress, String password) throws MessagingException {

		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress("pseswd@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
		message.setSubject("Ihr Passwort");
		message.setText("Ihr neues Password: \n" + password);

		Transport.send(message);
	}

}
