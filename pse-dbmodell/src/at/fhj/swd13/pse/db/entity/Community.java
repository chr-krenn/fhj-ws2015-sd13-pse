package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the community database table.
 * 
 */
@Entity
@Table(name="community")
@NamedQuery(name="Community.findAll", query="SELECT c FROM Community c")
public class Community implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="community_id", unique=true, nullable=false)
	private int communityId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="invitation_only", nullable=false)
	private boolean invitationOnly;

	@Column(nullable=false, length=64)
	private String name;

	@Column(name="system_internal", nullable=false)
	private boolean systemInternal;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="private_user")
	private Person person1;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="confirmed_by")
	private Person person2;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="created_by")
	private Person person3;

	//bi-directional many-to-one association to CommunityMember
	@OneToMany(mappedBy="community")
	private List<CommunityMember> communityMembers;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="community")
	private List<Message> messages;

	public Community() {
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

	public Person getPerson3() {
		return this.person3;
	}

	public void setPerson3(Person person3) {
		this.person3 = person3;
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

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setCommunity(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setCommunity(null);

		return message;
	}

}