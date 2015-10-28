package at.fhj.swd13.pse.controller;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import at.fhj.swd13.pse.domain.user.InvalidEmailAddressException;
import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@Stateless
public class ResetPasswordController {
	
	private String emailAddress;
	
	private String statusText;
	
	
	@Inject
	private UserService userService;
	
	public void resetPassword() {
		
		if(emailAddress != null) {
			try {
				userService.resetPassword(emailAddress);
				
				setStatusText("Sie bekommen in Kürze Ihr neues Passwort per E-Mail übermittelt.");
				
				
			} catch (InvalidEmailAddressException e) {
				setStatusText("E-Mail-Adresse nicht gefunden.");
			}			
		}		
	}
	
	
	public String test() {
		setStatusText("success");
		
		return null;
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
