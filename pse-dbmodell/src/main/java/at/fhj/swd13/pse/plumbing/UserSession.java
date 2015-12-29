package at.fhj.swd13.pse.plumbing;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.domain.user.UserService;

@SessionScoped
public class UserSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sessionId = createSessionId();


	@Inject
	private Logger logger;

	@Inject
	private UserService userService;

	private String loggedInUser = null;
	private boolean isAdmin = false;
	private int privateCommunityId;

	@PostConstruct
	protected void init() {
		logger.info("[USERSESSION] constructed");
	}

	@PreDestroy
	@Transactional
	protected void preDestroy() {
		logger.info("[USERSESSION] preDestroy for " + loggedInUser);

		try {

			userService.logoutUser(getUsername());
		} catch (Exception e) {
			logger.error("[USERSESSION] error durin preDestroy: " + e.getMessage());
		}
	}
	
	public static String createSessionId() {
		return UUID.randomUUID().toString();
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void logout() {
		logger.info("[USERSESSION] logged out " + loggedInUser);
		loggedInUser = null;
		isAdmin = false;
	}

	public String login(final String username) {
		this.loggedInUser = username;

		return sessionId;
	}

	public boolean isLoggedIn() {

		return loggedInUser != null;
	}

	public boolean isAdmin() {
		return loggedInUser != null && isAdmin;
	}

	public String getUsername() {
		return loggedInUser == null ? "Not YOU!" : loggedInUser;
	}

	public void setPrivateCommunityId(int privateCommunityId) {
		this.privateCommunityId = privateCommunityId;
	}

	public int getPrivateCommunityId() {
		return privateCommunityId;
	}

	public boolean canEditNews() {
		return isAdmin();
	}
}
