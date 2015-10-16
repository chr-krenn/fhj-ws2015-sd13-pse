package at.fhj.swd13.pse.xperimental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class MessageList implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<MessageDTO> messages = new ArrayList<MessageDTO>();

	public void addMessage(final String body) {
		MessageDTO message = new MessageDTO();

		message.setHeadLine("Testnachricht");
		message.setBody(body);

		messages.add(message);
	}
	
	
	public List<MessageDTO> getMessages() {
		
		return messages;
	}
}
