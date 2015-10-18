package at.fhj.swd13.pse.application;

import java.io.File;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.plumbing.UserSession;

@Stateless
@Path("/")
public class MediaController {

	@Inject
	private Logger logger;

	@Inject
	private UserSession userSession;

	@Inject
	private DocumentService documentService;

	@GET
	@Path("/media/{id:[0-9][0-9]*}")
	public Response getMedia(@PathParam("id") final int documentId) {

		logger.info(">>> [DOC] getting media for " + documentId);

		if (userSession.isLoggedIn()) {

			Document d = documentService.get(documentId);

			if (d != null) {

				File file = new File( documentService.getServerPath(d) );
				
				logger.info("[DOCS] MEDIA have file :" + d.getName());
				logger.info("[DOCS] storage location : " + file.getAbsolutePath() );

				ResponseBuilder responseBuilder = Response.ok((Object) file );
				responseBuilder.header("Content-Type", d.getMimeType());
				responseBuilder.header("Content-Disposition", "attachment; filename=\"" + d.getName() + "\"");

				return responseBuilder.build();
			} else {

				return Response.status(Status.NOT_FOUND).build();
			}
		} else {

			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@GET
	@Path("/mediadirect/{id:[0-9][0-9]*}")
	public Response getMediaDirect(@PathParam("id") final int documentId) {

		logger.info(">>> [DOC] getting media for " + documentId);

		if (userSession.isLoggedIn()) {

			Document d = documentService.get(documentId);

			if (d != null) {

				File file = new File( documentService.getServerPath(d) );
				
				logger.info("[DOCS] MEDIA have file :" + d.getName());
				logger.info("[DOCS] storage location : " + file.getAbsolutePath() );

				ResponseBuilder responseBuilder = Response.ok((Object) file );
				responseBuilder.header("Content-Type", d.getMimeType());
				return responseBuilder.build();
			} else {

				return Response.status(Status.NOT_FOUND).build();
			}
		} else {

			return Response.status(Status.FORBIDDEN).build();
		}
	}
}
