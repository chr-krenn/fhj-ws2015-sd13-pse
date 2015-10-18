package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the mesasge_rating database table.
 * 
 */
@Entity
@Table(name="mesasge_rating")
@NamedQuery(name="MesasgeRating.findAll", query="SELECT m FROM MesasgeRating m")
public class MesasgeRating implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="mesasge_rating_id", unique=true, nullable=false)
	private int mesasgeRatingId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="mesasge_id")
	private Message message;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="rating_person_id")
	private Person person;

	public MesasgeRating() {
	}

	public int getMesasgeRatingId() {
		return this.mesasgeRatingId;
	}

	public void setMesasgeRatingId(int mesasgeRatingId) {
		this.mesasgeRatingId = mesasgeRatingId;
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