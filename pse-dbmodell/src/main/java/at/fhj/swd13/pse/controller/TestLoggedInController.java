package at.fhj.swd13.pse.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
public class TestLoggedInController {

	@Inject
	private UserSession userSession;

	@Inject
	private UserService userService;

	@PostConstruct
	protected void validateLogin() {
		if ( ! userSession.isLoggedIn() ) {
			throw new NotAuthorizedException("User has not logged in and is not authorized to view this page");
		}
	}
	
	public String getUsername() {

		return userSession.getUsername();
	}

	public Person getUser() {
		return userService.getUser(userSession.getUsername());
	}
}
