package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.fhj.swd13.pse.plumbing.ArgumentChecker;

/**
 * The persistent class for the message_tag database table.
 * 
 */
@Entity
@Table(name = "message_tag")
@NamedQueries({
		@NamedQuery(name = "MessageTag.deleteById", query = "DELETE FROM MessageTag m WHERE m.messageTagId = :id"),
		@NamedQuery(name = "MessageTag.findAll", query = "SELECT m FROM MessageTag m"),
		@NamedQuery(name = "MessageTag.findById", query = "SELECT m FROM MessageTag m WHERE m.messageTagId = :id") })
public class MessageTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_tag_id", unique = true, nullable = false)
	private int messageTagId;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	// bi-directional many-to-one association to Message
	@ManyToMany
	@JoinColumn(name = "message_id", nullable = false)
	private List<Message> messages = new ArrayList<Message>();

	// bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;

	public MessageTag() {
	}
	
	public MessageTag(Tag tag) {
		
		ArgumentChecker.assertNotNull( tag, "tag");
		
		this.tag = tag;
		this.createdAt = new Date();
	}

	public int getMessageTagId() {
		return this.messageTagId;
	}

	public void setMessageTagId(int messageTagId) {
		this.messageTagId = messageTagId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
		tag.getMessageTags().add(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + messageTagId;
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
		if (!(obj instanceof MessageTag)) {
			return false;
		}
		MessageTag other = (MessageTag) obj;
		if (messageTagId != other.messageTagId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MessageTag [messageTagId=" + messageTagId + ", createdAt=" + createdAt + ", messages=" + messages + ", tag=" + tag + "]";
	}

}