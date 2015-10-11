package at.fhj.swd13.pse.plumbing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import java.io.Serializable;

@SessionScoped
public class UserSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private Logger logger;

	
	private String loggedInUser = null;
	

	@PostConstruct
	protected void init() {
		logger.info("[USERSESSION] constructed");
	}
	
	public void login( final String username ) {
		this.loggedInUser = username;
	}
	
	public boolean isLoggedIn() {
		
		return loggedInUser != null; 
	}

	
	public String getUsername() {
		return loggedInUser == null ? "Not YOU!" : loggedInUser;
	}
}
