package at.fhj.swd13.pse.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;


import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.service.UserService;

@Startup
@Singleton
public class SetDefaultPasswords {

	@Inject
	private DbContext dbContext;
	
	@PostConstruct
	public void runOnce() {
		
		final UserService userService = new UserService(dbContext);
		
		userService.updateNullPasswords();		
	}
}
