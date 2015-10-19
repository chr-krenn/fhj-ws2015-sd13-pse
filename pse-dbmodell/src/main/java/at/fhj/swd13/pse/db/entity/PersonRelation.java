package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;

import javax.persistence.*;


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
			throw new ParameterException("fromPerson must not be null");
		}

		if ( toPerson == null ) {
			throw new ParameterException("toPerson must not be null");
		}
		
		if ( fromPerson == toPerson ) {
			throw new ParameterException("from and to person may not be identical");			
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
		this.sourcePerson = person;
	}
	
	public Person getTargetPerson() {
		return this.targetPerson;
	}

	public void setTargetPerson( Person person ) {
		this.targetPerson = person;
	}
}