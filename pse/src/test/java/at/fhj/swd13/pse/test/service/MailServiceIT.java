package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.feed.FeedService;
import at.fhj.swd13.pse.domain.feed.FeedServiceFacade;
import at.fhj.swd13.pse.plumbing.MailService;
import at.fhj.swd13.pse.plumbing.MailServiceFacade;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class MailServiceIT extends RemoteTestBase {

	private static final String SMTP_TEST_PORT = "3025";
	private final String SUB_PREFIX = "[PSE] ";
	private final String SENDER = "swd13pse@gmail.com";
	private MailService mailService;
	private FeedService feedService;
	private GreenMail greenMail;
	private Properties props;
	
    @Before
    public void setup() throws NamingException {
    	prepareDatabase();
    	
    	props = new Properties();
        props.setProperty("mail.smtp.host", "localhost");
        props.setProperty("mail.smtp.port", SMTP_TEST_PORT);
        
    	mailService = lookup(MailServiceFacade.class, MailService.class);
    	
    	greenMail = new GreenMail(new ServerSetup(Integer.parseInt(SMTP_TEST_PORT), null, "smtp"));
        greenMail.start ();
        
        feedService = lookup(FeedServiceFacade.class, FeedService.class);
    }	
    
    @After
    public void tearDown() {
        greenMail.stop();
    }
	
    @Test
    public void sendMailTest() {
    	try {
			mailService.sendMail("Test Subject", "Mail body", "to@localhost.com", props);
			
			assertTrue(greenMail.waitForIncomingEmail(1000, 1));
			String subject = greenMail.getReceivedMessages()[0].getSubject();
			InternetAddress[] sender = (InternetAddress[]) greenMail.getReceivedMessages()[0].getFrom();
		    assertEquals(SUB_PREFIX +"Test Subject", subject);
		    assertEquals(SENDER, sender[0].getAddress());
		    
		} catch (MessagingException | InterruptedException e) {
			e.printStackTrace();
			fail();
		}
    }
    
    @Test
    public void sendMailTest2() {
    	
    	try {
    		Message m = feedService.getMessageById(1);
			mailService.sendMail(m, "recipient1@xyz.com, recipient2@xyz.com", props);
			
			assertTrue(greenMail.waitForIncomingEmail(1000, 1));
			String subject = greenMail.getReceivedMessages()[0].getSubject();
			InternetAddress[] recipient = (InternetAddress[]) greenMail.getReceivedMessages()[0].getAllRecipients();
		    assertEquals(SUB_PREFIX +m.getHeadline(), subject);
		    assertEquals("recipient1@xyz.com", recipient[0].getAddress());
		    assertEquals("recipient2@xyz.com", recipient[1].getAddress());
		    
		} catch (MessagingException | ServiceException | InterruptedException e) {
			e.printStackTrace();
			fail();
		}
    }

}
