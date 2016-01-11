package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;

import at.fhj.swd13.pse.plumbing.ArgumentChecker;


/**
 * The persistent class for the person_message database table.
 * 
 */
@Entity
@Table(name="person_message")
@NamedQuery(name="PersonMessage.findAll", query="SELECT p FROM PersonMessage p")
public class PersonMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="person_message_id", unique=true, nullable=false)
	private int personMessageId;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="message_id")
	private Message message;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="target_person_id")
	private Person person;

	public PersonMessage() {
	}

	public int getPersonMessageId() {
		return this.personMessageId;
	}

	public void setPersonMessageId(int personMessageId) {
		this.personMessageId = personMessageId;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		
		ArgumentChecker.assertNotNull(message, "message");
		
		this.message = message;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		
		ArgumentChecker.assertNotNull(person, "person");
		
		this.person = person;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + personMessageId;
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
		if (!(obj instanceof PersonMessage)) {
			return false;
		}
		PersonMessage other = (PersonMessage) obj;
		if (personMessageId != other.personMessageId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PersonMessage [personMessageId=" + personMessageId + ", message=" + message + ", person=" + person + "]";
	}

}