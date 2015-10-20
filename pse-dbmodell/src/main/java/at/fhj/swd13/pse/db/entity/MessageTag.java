package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;

	public MessageTag() {
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

}