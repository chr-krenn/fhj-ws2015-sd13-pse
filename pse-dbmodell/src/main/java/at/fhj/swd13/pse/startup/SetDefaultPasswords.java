package at.fhj.swd13.pse.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.CurrentDbContext;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceImpl;

@Startup
@Singleton
public class SetDefaultPasswords {

	@Inject @CurrentDbContext
	private DbContext dbContext;
	
	@PostConstruct
	public void runOnce() {
		
		final UserService userService = new UserServiceImpl(dbContext);
		userService.updateNullPasswords();		
	}
}
