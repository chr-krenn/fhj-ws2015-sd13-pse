package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.fhj.swd13.pse.plumbing.JpaHelper;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "document")
@NamedQueries({ @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
		@NamedQuery(name = "Document.findById", query = "SELECT d FROM Document d WHERE d.documentId = :id"),
		@NamedQuery(name = "Document.deleteById", query = "DELETE FROM Document d WHERE d.documentId = :id") })
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int nameLength = JpaHelper.getColumneLength(Document.class, "name");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id", unique = true, nullable = false)
	private int documentId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(length = 128)
	private String description;

	@Column(name = "mime_type", nullable = false, length = 64)
	private String mimeType;

	@Column(nullable = false, length = 64)
	private String name;

	@Column(nullable = false)
	private int size;

	@Column(name = "storage_location", nullable = false, length = 128)
	private String storageLocation;

	// bi-directional many-to-one association to Message
	@OneToMany(mappedBy = "icon")
	private List<Message> messagesAsIcons;

	// bi-directional many-to-one association to Message
	@OneToMany(mappedBy = "attachment")
	private List<Message> messagesAsAttachments;

	// bi-directional many-to-one association to Person
	@OneToMany(mappedBy = "document")
	private List<Person> persons;

	public Document() {}
	
	public Document(String description, String mimeType, String name, int size, String storageLocation) {
		this.description = description;
		this.mimeType = mimeType;
		this.name = name;
		this.size = size;
		this.storageLocation = storageLocation;
	}

	@PrePersist
	protected void prePersist() {
		setCreatedAt(new Date());
	}

	public int getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {

		if (nameLength == 0 || name.length() <= nameLength ) {

			this.name = name;
		} else {
			//name is too long to fit into the db column --> truncate
			
			final int charsTooMuch = name.length() - nameLength;
			final int lastDot = name.lastIndexOf( '.');
			
			if ( lastDot > charsTooMuch ) {
				
				this.name = name.substring( 0, lastDot - charsTooMuch);
				this.name = this.name + name.substring( lastDot );
			} else {
				this.name = name.substring(0, nameLength);
			}
		}
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getStorageLocation() {
		return this.storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public List<Message> getMessageAsIcons() {
		return this.messagesAsIcons;
	}

	public Message addUseAsIcon(Message messages) {
		getMessageAsIcons().add(messages);
		messages.setIcon(this);

		return messages;
	}

	public Message removeUseAsIcon(Message messages1) {
		getMessageAsIcons().remove(messages1);
		messages1.setIcon(null);

		return messages1;
	}

	public List<Message> getMessageAsAttachment() {
		return this.messagesAsAttachments;
	}

	public Message addUseAsAttachment(Message messages) {
		getMessageAsAttachment().add(messages);
		messages.setAttachment(this);

		return messages;
	}

	public Message removeMessages2(Message messages) {
		getMessageAsAttachment().remove(messages);
		messages.setAttachment(null);

		return messages;
	}

	public List<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public Person addPerson(Person person) {
		getPersons().add(person);
		person.setDocument(this);

		return person;
	}

	public Person removePerson(Person person) {
		getPersons().remove(person);
		person.setDocument(null);

		return person;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentId;
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
		if (!(obj instanceof Document)) {
			return false;
		}
		Document other = (Document) obj;
		if (documentId != other.documentId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Document [documentId=" + documentId + ", name=" + name + ", storageLocation=" + storageLocation + "]";
	}

}