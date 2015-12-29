package at.fhj.swd13.pse.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@RequestScoped
public class ResetPasswordController {
	
	private String emailAddress;
	private String statusText;
	
	@Inject
	private UserService userService;
	
	public void resetPassword() {		
		if (emailAddress != null) {
			try {
				FacesContext context = FacesContext.getCurrentInstance();
				HttpServletRequest rq = (HttpServletRequest)context.getExternalContext().getRequest();
				int port = rq.getServerPort();
				String serverName = rq.getServerName();

				userService.resetPassword(emailAddress, serverName, port);
				setStatusText("Sie bekommen in Kürze Ihr neues Passwort per E-Mail übermittelt.");
			} catch (ServiceException ex) {
				setStatusText("An die E-Mail-Adresse konnte nicht gesendet werden.");
			}
		}		
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
}
