package at.fhj.swd13.pse.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.CurrentDbContext;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceImpl;

@Startup
@Singleton
public class SetDefaultPasswords {

	@Inject @CurrentDbContext
	private DbContext dbContext;
	
	@Inject
	private Logger logger;
	
	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
	public void runOnce() {
		
		logger.info("[STARTUP] setting default passwords" );
		

		final UserService userService = new UserServiceImpl(dbContext);
		
		logger.info( "[STARTUP] number of changed passwords: " +userService.updateNullPasswords() );		
	}
}
