package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;


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
		this.message = message;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}