package at.fhj.swd13.pse.plumbing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import java.io.Serializable;
import java.util.UUID;

@SessionScoped
public class UserSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String sessionId = UUID.randomUUID().toString();

	@Inject
	private Logger logger;

	
	private String loggedInUser = null;
	

	@PostConstruct
	protected void init() {
		logger.info("[USERSESSION] constructed");
	}
	
	public String login( final String username ) {
		this.loggedInUser = username;
		
		return sessionId;
	}
	
	public boolean isLoggedIn() {
		
		return loggedInUser != null; 
	}

	
	public String getUsername() {
		return loggedInUser == null ? "Not YOU!" : loggedInUser;
	}
}
