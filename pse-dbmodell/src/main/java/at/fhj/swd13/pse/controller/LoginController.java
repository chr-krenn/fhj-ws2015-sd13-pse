package at.fhj.swd13.pse.controller;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@Stateless
public class LoginController {

	private String username;

	private String password;

	@Inject
	private Logger logger;

	@Inject
	private UserService userService;


	public void login(ActionEvent event) {

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
		context.addCallbackParam("loggedIn", loggedIn);
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
}
