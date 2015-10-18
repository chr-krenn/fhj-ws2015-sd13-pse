package at.fhj.swd13.pse.xperimental;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ManagedBean
public class MessageListView {


	@Inject
	private MessageList messageList;
	
	@Inject Logger logger;
	
	public  List<MessageDTO> getMessages() {
		
		logger.info("[XPERIMENTAL] messageListView number of messages: " + messageList.getMessages().size() );
		
		return messageList.getMessages();
	}
}
