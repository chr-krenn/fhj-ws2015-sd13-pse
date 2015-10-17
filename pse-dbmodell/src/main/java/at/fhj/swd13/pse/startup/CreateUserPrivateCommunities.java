package at.fhj.swd13.pse.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.domain.chat.ChatService;

@Startup
@Singleton
public class CreateUserPrivateCommunities {

	@Inject
	private Logger logger;

	@Inject
	private ChatService chatService;

	@PostConstruct
	protected void onPostConstruct() {

		logger.info("[STARTUP] creating user private communities");

		final int createdCommunitiesCount = chatService.createAllPrivateCommunities();

		logger.info("[STARTUP] number of created communities" + createdCommunitiesCount);
	}
}
