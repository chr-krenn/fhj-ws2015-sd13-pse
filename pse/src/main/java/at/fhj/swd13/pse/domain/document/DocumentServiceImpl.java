package at.fhj.swd13.pse.domain.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.hibernate.service.spi.ServiceException;
import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.plumbing.ConfigurationHelper;
import at.fhj.swd13.pse.service.ServiceBase;

@Stateless
public class DocumentServiceImpl extends ServiceBase implements DocumentService {

	@Inject
	private Logger logger;

	private static Random random = new Random();

	private static String serviceUrl = "/store/media/";
	private static String imageFolderUrl = "/protected/img/";
	private static String imageFolder = "/tmp/pse/documents";

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

		loadSettings();
	}

	@PostConstruct
	protected void onPostConstruct() {
		loadSettings();
	}

	/**
	 * load the properties from the current system property file.
	 * Properties loaded are
	 * 
	 * at.fhj.swd13.pse.mediaUrl
	 * at.fhj.swd13.pse.imageFolderUrl
	 * 
	 * at.fhj.swd13.pse.imageFolder
	 * at.fhj.swd13.pse.maxSubIndices
	 * 
	 */
	protected void loadSettings() {

		if (logger != null) {
			logger.info("[DOCS] loading properties");
		}

		serviceUrl = ConfigurationHelper.saveSetProperty("at.fhj.swd13.pse.mediaUrl", serviceUrl);
		imageFolderUrl = ConfigurationHelper.saveSetProperty("at.fhj.swd13.pse.imageFolderUrl", imageFolderUrl);

		imageFolder = ConfigurationHelper.saveSetProperty("at.fhj.swd13.pse.imageFolder", imageFolder);
		maxSubIndices = ConfigurationHelper.saveSetPropertyInt("at.fhj.swd13.pse.maxSubIndices", "9");
	}

	@Override
	public Document store(String filename, InputStream data, String description) {

		try {
			File file = new File(filename);

			String mimeType = GetMimeType(filename);

			Document document = new Document(description, mimeType, file.getName(), (int) file.length(), storeFile(data));

			dbContext.getDocumentDAO().insert(document);

			logger.info("[DOCS] stored file " + filename);
			logger.info("[DOCS] storage location is " + document.getStorageLocation());

			return document;
		} catch (Throwable x) {
			logger.error("[DOCS] Error storing file " + filename + " : " + x.getMessage());
			throw new ServiceException("[DOCS] cannot store " + filename, x);
		}
		finally {
			try {
				data.close();
			} catch (IOException e) {
				logger.error("[DOCS] Error closing file input stream for " + filename + " : " + e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.domain.document.DocumentService#store(java.lang.String,
	 * java.io.InputStream)
	 */
	@Override
	public Document store(final String filename, InputStream data) {
		return store(filename, data, null);
	}

	@Override
	@SuppressWarnings("squid:S1166")
	public Document store(String filename, String filepath) {
		try (InputStream in = new FileInputStream(filepath)) {
			return store(filename, in);
		} catch (IOException e) {
			logger.error("[DOCS] Error creating file input stream for " + filename + " with filepath " + filepath + ": " + e.getMessage());

			throw new ServiceException("[DOCS] cannot store " + filename, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.document.DocumentService#get(int)
	 */
	public Document get(final int documentId) {

		try {
			return dbContext.getDocumentDAO().getById(documentId);
		} catch (Throwable e) {
			logger.error("[DOCS] Error getting " + documentId, e);

			throw new ServiceException("[DOCS] error getting " + documentId, e);
		}
	}

	public void remove(final Document document) {
		try {
			dbContext.getDocumentDAO().remove(document);
		} catch (Throwable e) {
			logger.error("[DOCS] Error removing document " + document, e);
		}
	}

	@Override
	public void removeDocument(int documentId) {
		try {
			dbContext.getDocumentDAO().remove(documentId);
		} catch (Throwable e) {
			logger.error("[DOCS] Error removing document with id " + documentId, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.domain.document.DocumentService#getServerPath(at.fhj.
	 * swd13.pse.db.entity.Document)
	 */
	public String getServerPath(final Document d) {

		Path fullPath = Paths.get(imageFolder, d.getStorageLocation());

		return fullPath.toString();
	}

	/**
	 * Store the file to the archive and also create its archive name
	 * 
	 * @param stream
	 *            the file data from the uploaded file
	 * 
	 * @return relative path of the filename created in the archive (beneath
	 *         imageFileFolder)
	 * 
	 * @throws IOException
	 *             when an I/O Exception occured during copying
	 */
	private static String storeFile(InputStream stream) {
		try {
			final Path relativeFilename = buildRelativeFilename();
			final Path absoluteFile = Paths.get(imageFolder, relativeFilename.toString());

			Files.copy(stream, absoluteFile);

			return relativeFilename.toString();
		} catch (Throwable e) {

			throw new ServiceException("[DOCS] error copying", e);
		}
	}

	private static Path buildRelativeFilename() {

		final String filename = UUID.randomUUID().toString() + ".uploaded";
		final Integer subDirIndex = random.nextInt(maxSubIndices) + 1;

		return Paths.get(subDirIndex.toString(), filename);
	}

	private String GetMimeType(String filePath) {
		try {
			String contentType = Files.probeContentType(Paths.get(filePath));
			return contentType == null ? "application/unknown" : contentType;

		} catch (IOException e) {
			logger.warn("[DOCS] Error while detecting contentType of file: %s", filePath, e);
			return "application/unknown";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.document.DocumentService#buildImageUrl(java.lang.String)
	 */
	public String buildImageUrl(final String filename) {
		return imageFolderUrl + filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.document.DocumentService#buildServiceUrl(int)
	 */
	public String buildServiceUrl(final int documentId) {
		return serviceUrl + Integer.toString(documentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.document.DocumentService#getDefaultDocumentRef(at.fhj.swd13.pse.domain.document.DocumentService.DocumentCategory)
	 */
	public String getDefaultDocumentRef(DocumentCategory documentCategory) {

		switch (documentCategory) {
		case MESSAGE_ICON:
			return buildImageUrl("default_message_icon.jpg");
		case USER_IMAGE:
			return buildImageUrl("default_user_image.jpg");
		default:
			return buildImageUrl("no_img.jpg");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.document.DocumentService#assertDocumentFolders()
	 */
	public void assertDocumentFolders() {
		try {
			Path documentPath = Paths.get(imageFolder);

			Files.createDirectories(documentPath);
			logger.info("[DOCS] checked document folder " + documentPath);

			for (int i = 1; i <= maxSubIndices; ++i) {

				Path documentSubPath = Paths.get(imageFolder + "/" + i);

				Files.createDirectories(documentSubPath);
				logger.info("[DOCS] checked document folder " + documentSubPath);
			}
		} catch (Throwable e) {
			logger.error("[DOCS] creating document folders ", e);

			throw new ServiceException("[DOCS] error creating document folders", e);
		}
	}

	@Override
	public InputStream getStreamForDocument(int documentId) {
		try {
			Document document = get(documentId);
			return new FileInputStream(getServerPath(document));
		} catch (FileNotFoundException e) {
			logger.error("[DOCS] file not found for " + documentId, e);
			throw new ServiceException("[DOCS] file not found " + documentId, e);
		} catch ( Throwable t ) {
			logger.error("[DOCS] error getting stream for " + documentId +": " + t.getMessage(), t);
			throw new ServiceException("[DOCS] error getting stream for " + documentId + ":" + t.getMessage(), t);			
		}
	}
}
