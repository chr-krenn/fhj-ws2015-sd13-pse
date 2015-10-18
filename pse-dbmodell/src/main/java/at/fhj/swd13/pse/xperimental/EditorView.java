package at.fhj.swd13.pse.xperimental;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ManagedBean
public class EditorView {

	private String text;

	
	@Inject
	private MessageList messageList;
	
	@Inject Logger logger;
	
	public EditorView() {
		
	}
	
	public void done() {
		
		messageList.addMessage( text );
		logger.info("[XPERIMENT] added message " + text );
	}
	
	/**
	 * @return the messageBody
	 */
	public String getText() {
		return text;
	}

	
	/**
	 * @param messageBody the messageBody to set
	 */
	public void setText(String messageBody) {
		
		logger.info("[XPERIMENT] set messageBody " + messageBody );

		this.text = messageBody;
	}
	
}
