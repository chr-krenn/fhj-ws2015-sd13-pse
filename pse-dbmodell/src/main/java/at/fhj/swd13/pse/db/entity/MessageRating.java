package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the mesasge_rating database table.
 * 
 */
@Entity
@Table(name="message_rating")
@NamedQuery(name="MessageRating.findAll", query="SELECT m FROM MessageRating m")
public class MessageRating implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="message_rating_id", unique=true, nullable=false)
	private int messageRatingId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="message_id")
	private Message message;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="rating_person_id")
	private Person person;

	public MessageRating() {
	}

	public int getMessageRatingId() {
		return this.messageRatingId;
	}

	public void setMessageRatingId(int messageRatingId) {
		this.messageRatingId = messageRatingId;
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

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}