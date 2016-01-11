package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import at.fhj.swd13.pse.plumbing.ArgumentChecker;


/**
 * The persistent class for the person_relation database table.
 * 
 */
@Entity
@Table(name="person_relation")
@NamedQueries({
	@NamedQuery(name="PersonRelation.findAll", query="SELECT p FROM PersonRelation p"),
	@NamedQuery(name = "PersonRelation.deleteByPersonIds", query = "DELETE FROM PersonRelation p WHERE p.sourcePerson = :sourcePerson AND p.targetPerson = :targetPerson") })
public class PersonRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="person_relation_id", unique=true, nullable=false)
	private int personRelationId;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="source_person_id", nullable=false)
	private Person sourcePerson;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="target_person_id", nullable=false)
	private Person targetPerson;

	public PersonRelation() {
	}

	/**
	 * Create a relation from a specific person to another
	 * 
	 * @param fromPerson person that initiated the relation
	 * @param toPerson person that is the target of the relation
	 * 
	 * @throws ParameterException when either is null or both are the same
	 * 
	 */
	public PersonRelation( Person fromPerson, Person toPerson ) {
		
		if ( fromPerson == null ) {
			throw new IllegalArgumentException("fromPerson must not be null");
		}

		if ( toPerson == null ) {
			throw new IllegalArgumentException("toPerson must not be null");
		}
		
		if ( fromPerson == toPerson ) {
			throw new IllegalArgumentException("from and to person may not be identical");			
		}
		
		this.sourcePerson = fromPerson;
		sourcePerson.addPersonSourceRelation( this );
		
		this.targetPerson = toPerson;
		targetPerson.addPersonTargetRelation( this );
	}
	
	public int getPersonRelationId() {
		return this.personRelationId;
	}

	public void setPersonRelationId(int personRelationId) {
		this.personRelationId = personRelationId;
	}

	public Person getSourcePerson() {
		return this.sourcePerson;
	}

	public void setSourcePerson( Person person ) {
		
		ArgumentChecker.assertNotNull(person, "person");
		
		this.sourcePerson = person;
	}
	
	public Person getTargetPerson() {
		return this.targetPerson;
	}

	public void setTargetPerson( Person person ) {

		ArgumentChecker.assertNotNull(person, "person");		
		
		this.targetPerson = person;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + personRelationId;
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
		if (!(obj instanceof PersonRelation)) {
			return false;
		}
		PersonRelation other = (PersonRelation) obj;
		if (personRelationId != other.personRelationId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PersonRelation [personRelationId=" + personRelationId + ", sourcePerson=" + sourcePerson + ", targetPerson=" + targetPerson + "]";
	}
	
}