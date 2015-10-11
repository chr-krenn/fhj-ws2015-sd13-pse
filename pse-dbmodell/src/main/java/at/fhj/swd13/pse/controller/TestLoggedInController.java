package at.fhj.swd13.pse.controller;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.CurrentDbContext;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceImpl;
import at.fhj.swd13.pse.plumbing.UserSession;

@ManagedBean
public class TestLoggedInController {

	@Inject
	private UserSession userSession;
	
	@Inject @CurrentDbContext
	private DbContext dbContext;
	
	private UserService userService;
	
	
	public String getUsername() {
		
		return userSession.getUsername();
	} 	
	
	public Person getUser()
	{
		userService = new UserServiceImpl(dbContext);
		return userService.getUser(userSession.getUsername());
	}
}
