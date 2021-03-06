package at.fhj.swd13.pse.controller;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.UserSession;


@ManagedBean
public class LoginController extends ControllerBase{

	private String username;

	private String password;

	private String passwordNew;
	private String passwordNewConfirmation;

	@Inject
	private Logger logger;

	@Inject
	private UserService userService;
	
	@Inject
	private ChatService chatService;
	

	@Inject
	private UserSession userSession;

	@SuppressWarnings("squid:S1166")
	public String login() {

		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean loggedIn = false;

		try {
			if (username != null && password != null) {
				Person user = userService.loginUser(username, password, userSession.getSessionId());

				if (user != null) {
					userSession.login(username);
					userSession.setAdmin(user.isAdmin());
					
					//get private community
					Community community = chatService.getPrivateCommunity(user);
					if (community != null)
						userSession.setPrivateCommunityId(community.getCommunityId());
					
					loggedIn = true;
					logger.info("[LOGIN] logged-in-user " + user);
					message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome",
							user.getFirstName() + " " + user.getLastName());
				} else {
					loggedIn = false;
					logger.info("[LOGIN] login failed for " + username + " from " + context.toString());
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
				}
			} else {
				loggedIn = false;
				logger.info("[LOGIN] login failed for " + username + " from " + context.toString());
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
			}
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (ServiceException e) {
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}		
		
		return loggedIn ? "/protected/Main" : "NotLoggedIn" + "?faces-redirect=true";
	}

	public String changePassword() {

		boolean passwordChanged = false;
		FacesMessage message = null;

		if ((password != null && passwordNew != null && passwordNewConfirmation != null)
				&& passwordNew.equals(passwordNewConfirmation)) {

			passwordChanged = userService.changePassword(getLoggedInUsername(), password, passwordNew);

		}
		if (!passwordChanged)
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Password change Error", "Invalid input");
		else
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been changed!", "");

		FacesContext.getCurrentInstance().addMessage(null, message);
		return passwordChanged ? "/protected/Main" : "/protected/UserPasswordChange";
	}

	@SuppressWarnings("squid:S1166")
	public void logout() {

		userService.logoutUser(getLoggedInUsername());
		userSession.logout();

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			String url = extContext
					.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/index.xhmtl"));
			extContext.redirect(url);
		} catch (IOException e) {
			logger.error("[LOGIN] error redirecting after logout: " + e.getMessage());
		}
	}
	

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {

		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {

		this.password = password;
	}

	/**
	 * @return the passwordNew
	 */
	public String getPasswordNew() {
		return passwordNew;
	}

	/**
	 * @param passwordNew
	 *            the passwordNew to set
	 */
	public void setPasswordNew(String passwordNew) {

		this.passwordNew = passwordNew;
	}

	/**
	 * @return the passwordNewConfirmation
	 */
	public String getPasswordNewConfirmation() {
		return passwordNewConfirmation;
	}

	/**
	 * @param passwordNewConfirmation
	 *            the passwordNewConfirmation to set
	 */
	public void setPasswordNewConfirmation(String passwordNewConfirmation) {

		this.passwordNewConfirmation = passwordNewConfirmation;
	}

	/**
	 * @return the IsLoggedIn
	 */
	public boolean getIsLoggedIn() {
		return userSession.isLoggedIn();
	}

	/**
	 * @return the LoggedInUsername
	 */
	public String getLoggedInUsername() {
		return userSession.getUsername();
	}

	/**
	 * Indicates whether the logged-in user is an admin
	 * @return isAdmin
	 */
	@SuppressWarnings("squid:S1166")	
	public boolean getIsAdmin() {
		try {
			return userService.getUser(getLoggedInUsername()).isAdmin();
		} catch (ServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "GetIsAdmin Error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			return false;
		}
	}
	
	/**
	 * @return the Id of the private community of the logged in user
	 */
	public int getPrivateCommunityId() {
		return userSession.getPrivateCommunityId();
	}
	
	/**
	 * Navigate from Username Button to correct UserProfile
	 * @return
	 */
	public String navigateToOwnProfile() {
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("userForm");
		return "/protected/User.jsf";
	}
}
