package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;

import javax.persistence.*;

import at.fhj.swd13.pse.plumbing.ArgumentChecker;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the tag database table.
 * 
 */
@Entity
@Table(name="tag")
@NamedQueries( { 
	@NamedQuery(name="Tag.findAll", query="SELECT t FROM Tag t"),
	@NamedQuery(name="Tag.findById", query="SELECT t FROM Tag t WHERE t.tagId = :id" ),
	@NamedQuery(name="Tag.findByToken", query="SELECT t FROM Tag t WHERE t.token = :token" ),
	@NamedQuery(name="Tag.findByTokenLike", query="SELECT t FROM Tag t WHERE t.token LIKE :token" ),
	@NamedQuery(name="Tag.deleteById", query="DELETE FROM Tag t WHERE t.tagId = :id"),
	@NamedQuery(name="Tag.findByPerson", query="SELECT t FROM Tag t LEFT JOIN t.personTags p where p.person = :person")
} )
public class Tag implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tag_id", unique=true, nullable=false)
	private int tagId;

	@Column(length=128)
	private String description;

	@Column(length=32)
	private String token;

	//bi-directional many-to-one association to MessageTag
	@OneToMany(mappedBy="tag",fetch=FetchType.EAGER)
	private List<MessageTag> messageTags = new ArrayList<MessageTag>();

	//bi-directional many-to-one association to PersonTag
	@OneToMany(mappedBy="tag", cascade={CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	private List<PersonTag> personTags;

	public Tag() {
	}

	/**
	 * Create a tag with the given token and description
	 *  
	 * @param token token of this tag (short name)
	 * @param description full text description of this tag
	 */
	public Tag( String token, String description ) {
		
		ArgumentChecker.assertContent(token, "token");
		
		this.token = token;
		this.description = description;
		
	}
	
	public int getTagId() {
		return this.tagId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		ArgumentChecker.assertContent(token, "token");
		
		
		this.token = token;
	}

	public List<MessageTag> getMessageTags() {
		return this.messageTags;
	}

	public void setMessageTags(List<MessageTag> messageTags) {
		this.messageTags = messageTags;
	}

	public MessageTag addMessageTag(MessageTag messageTag) {
		getMessageTags().add(messageTag);
		messageTag.setTag(this);

		return messageTag;
	}

	public MessageTag removeMessageTag(MessageTag messageTag) {
		getMessageTags().remove(messageTag);

		return messageTag;
	}

	public List<PersonTag> getPersonTags() {
		return this.personTags;
	}

	public PersonTag addPersonTag(PersonTag personTag) {
		getPersonTags().add(personTag);
		personTag.setTag(this);

		return personTag;
	}

	public PersonTag removePersonTag(PersonTag personTag) {
		getPersonTags().remove(personTag);

		return personTag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tagId;
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
		if (!(obj instanceof Tag)) {
			return false;
		}
		Tag other = (Tag) obj;
		if (tagId != other.tagId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Tag [tagId=" + tagId + ", description=" + description + ", token=" + token + "]";
	}

}