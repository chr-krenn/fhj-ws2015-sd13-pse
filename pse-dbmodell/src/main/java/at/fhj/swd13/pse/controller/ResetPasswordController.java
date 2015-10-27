package at.fhj.swd13.pse.controller;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@Stateless
public class ResetPasswordController {
	
	private String emailAddress;
	
	
	
	@Inject
	private UserService userService;
	
	public String resetPassword() {
		
		if(emailAddress != null) {
			userService.resetPassword(emailAddress);
			
			return "index.html";
		}
		return null;
	}

	
	
	
	public String getEmailAddress() {
		return emailAddress;
	}

	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}	

}
