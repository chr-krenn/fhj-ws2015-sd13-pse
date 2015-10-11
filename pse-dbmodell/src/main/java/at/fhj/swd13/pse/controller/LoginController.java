package at.fhj.swd13.pse.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
public class LoginController {

	private String username;
	
	private String password;

	@Inject
	private Logger logger;
	
	
	@Inject
	private UserSession userSession;
	
	public void login(ActionEvent event) {
		
		
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        boolean loggedIn = false;
         
        if(username != null && username.equals("admin") && password != null && password.equals("admin")) {
            loggedIn = true;
            
    		logger.info("[LOGIN] logged-in-user " + username );
            userSession.login( username );
            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
        } else {
            loggedIn = false;

            logger.info("[LOGIN] login failed for " + username + " from " + context.toString() );
            
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
	 * @param username the username to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		
		this.password = password;
	}	
}
