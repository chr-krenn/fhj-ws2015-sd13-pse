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

	public PersonTag(Tag tag) {
		this.tag = tag;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + personTagId;
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
		if (!(obj instanceof PersonTag)) {
			return false;
		}
		PersonTag other = (PersonTag) obj;
		if (personTagId != other.personTagId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PersonTag [personTagId=" + personTagId + ", person=" + person + ", tag=" + tag + "]";
	}

}