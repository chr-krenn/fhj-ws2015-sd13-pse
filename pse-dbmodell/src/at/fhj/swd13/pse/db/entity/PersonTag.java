package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the person_tag database table.
 * 
 */
@Entity
@Table(name="person_tag")
@NamedQuery(name="PersonTag.findAll", query="SELECT p FROM PersonTag p")
public class PersonTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="person_tag_id", unique=true, nullable=false)
	private int personTagId;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="person_id", nullable=false)
	private Person person;

	//bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumn(name="tag_id", nullable=false)
	private Tag tag;

	public PersonTag() {
	}

	public int getPersonTagId() {
		return this.personTagId;
	}

	public void setPersonTagId(int personTagId) {
		this.personTagId = personTagId;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}