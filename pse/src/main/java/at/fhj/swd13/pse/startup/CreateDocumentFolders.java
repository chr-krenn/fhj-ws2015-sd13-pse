package at.fhj.swd13.pse.startup;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.domain.document.DocumentService;

@Startup
@Singleton
public class CreateDocumentFolders {

	@Inject
	private Logger logger;
	
	@Inject
	private DocumentService documentService;
	
	@PostConstruct
	protected void onPostConstruct() {
		logger.info("[STARTUP] checking document folders");
		
		try {
			
			documentService.assertDocumentFolders();
		} catch (IOException e) {
			logger.error("[STARTUP] error creating document directories: " + e.getMessage() );
		}		
	}
}
