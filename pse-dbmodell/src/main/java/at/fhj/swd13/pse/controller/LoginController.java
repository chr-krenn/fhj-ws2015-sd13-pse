package at.fhj.swd13.pse.controller;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
@Stateless
public class LoginController {

	private String username;

	private String password;
	
	@Inject
	private Logger logger;

	@Inject
	private UserService userService;

	@Inject
	private UserSession userSession;
	
	public String login() {

		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean loggedIn = false;

		if (username != null && password != null) {

			Person user = userService.loginUser(username, password);

			if (user != null) {
				loggedIn = true;
				logger.info("[LOGIN] logged-in-user " + user);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getFirstName() + " " + user.getLastName());
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
		return loggedIn ? "/protected/TestLoggedIn.jsf" : "NotLoggedIn";
	}

	public void logout() {

		userService.logoutCurrentUser();

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		FacesContext context = FacesContext.getCurrentInstance();
		try {
	        String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, "/index.xhmtl"));
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
	 * @return the IsLoggedIn
	 */     
	public boolean getIsLoggedIn(){
		return userSession.isLoggedIn();
	}
	
	/**
	 * @return the LoggedInUsername
	 */     
	public String getLoggedInUsername(){
		return userSession.getUsername();
	}

}
