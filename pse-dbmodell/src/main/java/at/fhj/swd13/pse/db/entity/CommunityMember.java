package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the community_member database table.
 * 
 */
@Entity
@Table(name = "community_member")
@NamedQueries({ @NamedQuery(name = "CommunityMember.findAll", query = "SELECT c FROM CommunityMember c"),
				@NamedQuery(name = "CommunityMember.findMembersByCommunity", query = "SELECT m FROM CommunityMember m WHERE m.community = :community"),
				@NamedQuery(name = "CommunityMember.findByCommunityAndPerson", query = "SELECT m FROM CommunityMember m WHERE m.community = :community AND m.member = :person")})
public class CommunityMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "community_member_id", unique = true, nullable = false)
	private int communityMemberId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(name = "is_administrator", nullable = false)
	private boolean isAdministrator;

	// bi-directional many-to-one association to Community
	@ManyToOne
	@JoinColumn(name = "community_id", nullable = false)
	private Community community;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + communityMemberId;
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
		if (!(obj instanceof CommunityMember)) {
			return false;
		}
		CommunityMember other = (CommunityMember) obj;
		if (communityMemberId != other.communityMemberId) {
			return false;
		}
		return true;
	}

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "confirmed_by")
	private Person confirmer;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	private Person member;

	/**
	 * protected ctor needed by jpa
	 */
	protected CommunityMember() {
	}

	/**
	 * Create a non-admin membership object
	 * 
	 * @param community
	 * @param member
	 * 
	 */
	public CommunityMember(final Community community, final Person member) {

		createdAt = new Date();

		isAdministrator = false;

		setCommunity(community);
		setMember(member);
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

	public Person getConfirmer() {
		return this.confirmer;
	}

	public void setConfirmer(Person confirmer) {
		this.confirmer = confirmer;
	}

	public Person getMember() {
		return this.member;
	}

	public void setMember(Person member) {
		this.member = member;
	}

}