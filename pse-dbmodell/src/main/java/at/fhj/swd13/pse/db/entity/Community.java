package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the community database table.
 * 
 */
@Entity
@Table(name = "community")
@NamedQueries({ @NamedQuery(name = "Community.findAll", query = "SELECT c FROM Community c"),
		@NamedQuery(name = "Community.findAllAccessible", query = "SELECT c FROM Community c WHERE c.systemInternal = false AND c.privateUser is NULL AND c.confirmedBy is not NULL"),
		@NamedQuery(name = "Community.findById", query = "SELECT c FROM Community c WHERE c.communityId = :id"),
		@NamedQuery(name = "Community.findByName", query = "SELECT c FROM Community c WHERE c.name = :name"),
		@NamedQuery(name = "Community.findMatching", query = "SELECT c FROM Community c WHERE c.name LIKE :needle"),
		@NamedQuery(name = "Community.findMatchingAccessible", query = "SELECT c FROM Community c WHERE c.name LIKE :needle AND c.systemInternal = false AND c.privateUser is NULL AND c.confirmedBy is not NULL"),
		@NamedQuery(name = "Community.findUnconfirmed", query = "SELECT c FROM Community c WHERE c.confirmedBy IS NULL"),
		@NamedQuery(name = "Community.findPrivateForUser", query = "SELECT c FROM Community c WHERE c.privateUser = :person"),
		@NamedQuery(name = "Community.deleteById", query = "DELETE FROM Community c WHERE c.communityId = :id"),
		@NamedQuery(name = "Community.findCommunitiesByMember", query = "SELECT c.communityId FROM Community c JOIN c.communityMembers m WHERE m.member = :person"),
		@NamedQuery(name = "Community.findCommunitiesByMemberButOwn", query = "SELECT c.communityId FROM Community c JOIN c.communityMembers m WHERE m.member = :person AND c.privateUser <> :person AND c.name LIKE :needle"),
		// @NamedQuery(name = "Community.findMatchingByMemberButOwn", query = "SELECT c FROM Community c WHERE c.name LIKE :needle AND ( c.privateUser.personId
		// IS NOT NULL OR ( c.privateUser IS NULL AND c.invitationOnly = false ))") })
		@NamedQuery(name = "Community.findMatchingByMemberButOwn", query =//
		"SELECT c FROM Community c " //
		+ " LEFT JOIN c.communityMembers m " //
				+ " LEFT JOIN m.member m2 " // 
 				+ " LEFT JOIN c.privateUser pU " //
				+ " WHERE c.name LIKE :needle AND " //
				+ "           ( ( pU IS NOT NULL AND pU.userName <> :uname) OR ( c.privateUser IS NULL AND c.invitationOnly = false ) " //
				+ "        OR ( m2.userName = :uname ))" //
		)//
})
public class Community implements Serializable {

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + communityId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof Community)) {
			return false;
		}
		Community other = (Community) obj;
		if (communityId != other.communityId) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = 1L;

	public static final String PRIVATE_PREFIX = "@";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "community_id", unique = true, nullable = false)
	private int communityId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, insertable = false)
	private Date createdAt;

	@Column(name = "invitation_only", nullable = false)
	private boolean invitationOnly;

	@Column(nullable = false, length = 64)
	private String name;

	@Column(name = "system_internal", nullable = false)
	private boolean systemInternal;

	/*
	 * If privateUser is not null, this is the private community of a person
	 * Private communities are not displayed on the Communities page and
	 * are only used for private messages to that person
	 */
	@OneToOne(optional = true)
	@JoinColumn(name = "private_user", nullable = true)
	private Person privateUser;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "confirmed_by")
	private Person confirmedBy;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private Person createdBy;

	// bi-directional many-to-one association to CommunityMember
	@OneToMany(mappedBy = "community")
	private List<CommunityMember> communityMembers;

	// bi-directional many-to-one association to Message
	@ManyToMany(mappedBy = "communities")
	private List<Message> messages;

	public Community() {
		invitationOnly = false;
		systemInternal = false;
	}

	public Community(final String communityName) {
		this();
		this.name = communityName;
	}

	public int getCommunityId() {
		return this.communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean getInvitationOnly() {
		return this.invitationOnly;
	}

	public void setInvitationOnly(boolean invitationOnly) {
		this.invitationOnly = invitationOnly;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getSystemInternal() {
		return this.systemInternal;
	}

	public void setSystemInternal(boolean systemInternal) {
		this.systemInternal = systemInternal;
	}

	public Person getPrivateUser() {
		return this.privateUser;
	}

	public void setPrivateUser(Person person) {
		this.privateUser = person;
		person.setPrivateCommunity(this);
	}

	public boolean isPrivateChannel() {

		return privateUser != null;
	}

	public boolean isConfirmed() {

		return confirmedBy != null;
	}

	public Person getConfirmedBy() {
		return this.confirmedBy;
	}

	public void setConfirmedBy(Person person2) {
		this.confirmedBy = person2;
	}

	public Person getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Person person3) {
		this.createdBy = person3;
	}

	public List<CommunityMember> getCommunityMembers() {
		return this.communityMembers;
	}

	public void setCommunityMembers(List<CommunityMember> communityMembers) {
		this.communityMembers = communityMembers;
	}

	public CommunityMember addCommunityMember(CommunityMember communityMember) {
		getCommunityMembers().add(communityMember);
		communityMember.setCommunity(this);

		return communityMember;
	}

	public CommunityMember removeCommunityMember(CommunityMember communityMember) {
		getCommunityMembers().remove(communityMember);
		communityMember.setCommunity(null);

		return communityMember;
	}

	/**
	 * Add a member to this community - if not present
	 * 
	 * @param newMember
	 *            the member to add
	 * @param isAdministrator
	 *            when true the new member is an admin
	 * 
	 * @return added CommunityMember or null when the user was already present
	 */
	public CommunityMember addMember(final Person newMember, final boolean isAdministrator) {

		if (!isMember(newMember)) {

			CommunityMember membership = new CommunityMember(this, newMember);
			membership.setIsAdministrator(isAdministrator);

			if (getCommunityMembers() != null) {
				getCommunityMembers().add(membership);
			}

			newMember.getMemberships().add(membership);

			return membership;
		}

		return null;
	}

	/**
	 * Check whether a person is a member of this group
	 * 
	 * @param potentialMember
	 *            the person to check
	 * 
	 * @return true if the person is a member
	 */
	public boolean isMember(final Person potentialMember) {

		if (potentialMember != null && getCommunityMembers() != null) {
			for (CommunityMember membership : getCommunityMembers()) {
				if (membership.getMember() == potentialMember) {
					return true;
				}
			}
		}

		return false;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.getCommunities().add(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.getCommunities().remove(this);

		return message;
	}

}
