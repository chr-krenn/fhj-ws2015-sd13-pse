package at.fhj.swd13.pse.domain.document;

import java.io.Serializable;

public class DocumentLibraryEntry implements Serializable {

	private final int id;
	
	private final String name;
	
	private final String description;
	
	private final String createdAt;
	
	private final int documentId;
	
	private final String serverPath;
	
	private final String contentType;
	
	public DocumentLibraryEntry(int id, String name, String description, String createdAt, int documentId, String serverPath, 
								String contentType) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.documentId = documentId;
		this.serverPath = serverPath;
		this.contentType = contentType;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public int getDocumentId() {
		return documentId;
	}

	public String getServerPath() {
		return serverPath;
	}

	public String getContentType() {
		return contentType;
	}
}
