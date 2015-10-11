package at.fhj.swd13.pse.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.domain.user.UserService;

@Startup
@Singleton
public class SetDefaultPasswords {

	@Inject
	private Logger logger;

	@Inject
	private UserService userService;

	@PostConstruct
	public void runOnce() {

		logger.info("[STARTUP] setting default passwords");

		logger.info("[STARTUP] number of changed passwords: " + userService.updateNullPasswords());
	}
}
