package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the community_member database table.
 * 
 */
@Entity
@Table(name="community_member")
@NamedQuery(name="CommunityMember.findAll", query="SELECT c FROM CommunityMember c")
public class CommunityMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="community_member_id", unique=true, nullable=false)
	private int communityMemberId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="is_administrator", nullable=false)
	private boolean isAdministrator;

	//bi-directional many-to-one association to Community
	@ManyToOne
	@JoinColumn(name="community_id", nullable=false)
	private Community community;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="confirmed_by")
	private Person person1;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="person_id", nullable=false)
	private Person person2;

	public CommunityMember() {
	}

	public int getCommunityMemberId() {
		return this.communityMemberId;
	}

	public void setCommunityMemberId(int communityMemberId) {
		this.communityMemberId = communityMemberId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean getIsAdministrator() {
		return this.isAdministrator;
	}

	public void setIsAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	public Community getCommunity() {
		return this.community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Person getPerson1() {
		return this.person1;
	}

	public void setPerson1(Person person1) {
		this.person1 = person1;
	}

	public Person getPerson2() {
		return this.person2;
	}

	public void setPerson2(Person person2) {
		this.person2 = person2;
	}

}