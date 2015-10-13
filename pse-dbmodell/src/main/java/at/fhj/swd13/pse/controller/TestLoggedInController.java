package at.fhj.swd13.pse.controller;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
public class TestLoggedInController {

	@Inject
	private UserSession userSession;
	
	public String getUsername() {

		return userSession.getUsername();
	}
}
