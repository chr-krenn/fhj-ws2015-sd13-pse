package at.fhj.swd13.pse.presentation.user;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@SessionScoped
public class LoginView implements Serializable {
	
	@Inject
	private UserService userService;
	
	private Person person;

	private static final long serialVersionUID = 6662156648971797106L;
	
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlainPassword() {
		return plainPassword;
	}
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	private String plainPassword;
	
	public String login() {
		person = userService.loginUser(getUsername(), getPlainPassword());		
		return (person == null) ? "loginfailed" : "loginsuccessful";
	}
}