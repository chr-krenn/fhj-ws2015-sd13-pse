package at.fhj.swd13.pse.plumbing;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.CurrentDbContext;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;

@SessionScoped
public class UserSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sessionId = UUID.randomUUID().toString();

	@Inject
	private Logger logger;

	@Inject
	@CurrentDbContext
	private DbContext dbContext;

	private String loggedInUser = null;

	@PostConstruct
	protected void init() {
		logger.info("[USERSESSION] constructed");
	}

	@PreDestroy
	protected void preDestroy() {
		logger.info("[USERSESSION] preDestroy for " + loggedInUser );
		
		if (isLoggedIn()) {
			Person p = dbContext.getPersonDAO().getByUsername(loggedInUser);

			p.setCurrentSessionId(null);
			p.setIsOnline(false);
			
			logout();
		}
	}

	public void logout() {
		
		logger.info("[USERSESSION] logged out " + loggedInUser );
		loggedInUser = null;
	}
	
	public String login(final String username) {
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
