package at.fhj.swd13.pse.domain.document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.service.ServiceBase;

@Stateless
public class DocumentServiceImpl extends ServiceBase implements DocumentService {

	@Inject
	private Logger logger;
	
	@Inject
	private UserSession userSession;

	private static Random random = new Random();

	// TODO move to configuration
	private static final String imageFolder = "/tmp/pse/documents";

	/**
	 * How many subdirectories will be used beneath imageFolder
	 *
	 */
	private static int maxSubIndices = 9;

	/**
	 * Create an instance of the user service
	 * 
	 * @param dbContext
	 *            the connection to the persistent storage with which to work
	 */
	public DocumentServiceImpl() {
		super();
	}

	public DocumentServiceImpl(DbContext dbContext) {

		super(dbContext);
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.domain.document.DocumentService#store(java.lang.String, java.io.InputStream)
	 */
	@Override
	public Document store(final String filename, InputStream data) {

		Document document = new Document();

		try {
			File file = new File(filename);

			document.setName(file.getName());
			document.setMimeType(Files.probeContentType(Paths.get(filename)));

			document.setSize((int) file.length());

			document.setStorageLocation(storeFile(data));
			
			dbContext.getDocumentDAO().insert( document);

			logger.info("[DOCS] stored file " + filename);
			logger.info("[DOCS] storage location is " + document.getStorageLocation() );
			
			return document;
		} catch (IOException x) {
			logger.error("[DOCS] Error storing file " + filename + " : " + x.getMessage());
			return null;
		}
		finally {
			try {
				data.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Store the file to the archive and also create its archive name
	 * 
	 * @param stream the file data from the uploaded file
	 * 
	 * @return relative path of the filename created in the archive (beneath imageFileFolder)
	 * 
	 * @throws IOException when an I/O Exception occured during copying
	 */
	private static String storeFile(InputStream stream) throws IOException {

		final Path relativeFilename = buildRelativeFilename();
		final Path absoluteFile = Paths.get(imageFolder, relativeFilename.toString());

		Files.copy(stream, absoluteFile);

		return relativeFilename.toString();
	}

	private static Path buildRelativeFilename() {

		final String filename = UUID.randomUUID().toString() + ".uploaded";
		final Integer subDirIndex = random.nextInt(maxSubIndices) + 1;

		return Paths.get(subDirIndex.toString(), filename);
	}
}
