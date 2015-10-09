package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the message_tag database table.
 * 
 */
@Entity
@Table(name="message_tag")
@NamedQuery(name="MessageTag.findAll", query="SELECT m FROM MessageTag m")
public class MessageTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="message_tag_id", unique=true, nullable=false)
	private int messageTagId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="message_id", nullable=false)
	private Message message;

	//bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumn(name="tag_id", nullable=false)
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

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}