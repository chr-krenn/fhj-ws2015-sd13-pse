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
	
	public DocumentLibraryEntry(Community community, Document document) {
		this.community = community;
		this.document = document;
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
   
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentLibraryEntryId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DocumentLibraryEntry)) {
			return false;
		}
		DocumentLibraryEntry other = (DocumentLibraryEntry) obj;
		if (documentLibraryEntryId != other.documentLibraryEntryId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DocumentLibraryEntry [documentLibraryEntryId=" + documentLibraryEntryId + ", document=" + document + "]";
	}

}
