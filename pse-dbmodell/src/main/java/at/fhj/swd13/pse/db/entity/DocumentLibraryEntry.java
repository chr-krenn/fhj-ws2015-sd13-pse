package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: DocumentLibraryEntry
 *
 */
@Entity
@Table(name="document_library_entry")
@NamedQueries({ @NamedQuery(name = "DocumentLibraryEntry.findById", query = "SELECT e FROM DocumentLibraryEntry e WHERE e.documentLibraryEntryId = :id"),
				@NamedQuery(name = "DocumentLibraryEntry.getAllForCommunity", query = "SELECT e FROM DocumentLibraryEntry e WHERE e.community = :community")})
public class DocumentLibraryEntry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_library_entry_id", unique = true, nullable = false)
	private int documentLibraryEntryId;
	
	private static final long serialVersionUID = 1L;

	public DocumentLibraryEntry() {
		super();
	}
	
	@ManyToOne
	@JoinColumn(name="community_id")
	private Community community;
	
	
	@ManyToOne
	@JoinColumn(name="document_id")
	private Document document;
	
	
	
	public int getDocumentLibraryEntryId() {
		return documentLibraryEntryId;
	}

	public void setDocumentLibraryEntryId(int documentLibraryEntryId) {
		this.documentLibraryEntryId = documentLibraryEntryId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}
   
	
	
	
}
