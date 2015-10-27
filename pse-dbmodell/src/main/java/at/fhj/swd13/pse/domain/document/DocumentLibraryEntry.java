package at.fhj.swd13.pse.domain.document;

public class DocumentLibraryEntry {

	private final int id;
	
	private final String name;
	
	private final String description;
	
	private final String createdAt;
	
	private final int documentId;

	public DocumentLibraryEntry(int id, String name, String description, String createdAt, int documentId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.documentId = documentId;
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
}
