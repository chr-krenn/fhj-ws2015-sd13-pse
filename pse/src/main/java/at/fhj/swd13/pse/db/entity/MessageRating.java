package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;

import at.fhj.swd13.pse.plumbing.ArgumentChecker;

import java.util.Date;


/**
 * The persistent class for the mesasge_rating database table.
 * 
 */
@Entity
@Table(name="message_rating")
@NamedQueries({
	@NamedQuery(name="MessageRating.findAll", query="SELECT m FROM MessageRating m"),
	@NamedQuery(name="MessageRating.findAllRatersByMessage", query="SELECT m.person FROM MessageRating m WHERE m.message =:message"),
	@NamedQuery(name="MessageRating.findRatingByPersonAndMessage", query="SELECT m FROM MessageRating m WHERE m.message =:message AND m.person =:person"),
	@NamedQuery(name = "MessageRating.deleteById", query = "DELETE FROM MessageRating m WHERE m.messageRatingId = :id")})
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

	public MessageRating(Date createdAt, Message message, Person person) {
		
		ArgumentChecker.assertNotNull(createdAt, "createdAt");
		ArgumentChecker.assertNotNull(message, "message");
		ArgumentChecker.assertNotNull(person, "person");
		
		this.createdAt = createdAt;
		this.message = message;
		this.person = person;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + messageRatingId;
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
		if (!(obj instanceof MessageRating)) {
			return false;
		}
		MessageRating other = (MessageRating) obj;
		if (messageRatingId != other.messageRatingId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MessageRating [messageRatingId=" + messageRatingId + ", createdAt=" + createdAt + ", message=" + message + ", person=" + person + "]";
	}
	
}