package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mindrot.jbcrypt.BCrypt;

import at.fhj.swd13.pse.domain.user.WeakPasswordException;

/**
 * The persistent class for the person database table.
 * 
 */
@Entity
@Table(name = "person")
@NamedQueries({ @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p ORDER BY p.lastName, p.firstName"),
		@NamedQuery(name = "Person.findAllWithDepartment", query = "SELECT p FROM Person p WHERE p.department = :department ORDER BY p.lastName, p.firstName"), 
		@NamedQuery(name = "Person.findAllNullPasswords", query = "SELECT p FROM Person p WHERE p.hashedPassword IS NULL OR p.hashedPassword = '--' ORDER BY p.lastName, p.firstName"),
		@NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.personId = :id"),
		@NamedQuery(name = "Person.findByUserName", query = "SELECT p FROM Person p WHERE p.userName = :uname"),
		@NamedQuery(name = "Person.findNameLike", query = "SELECT p FROM Person p WHERE p.userName LIKE :name OR p.lastName LIKE :name OR p.emailAddress LIKE :name ORDER BY p.lastName, p.firstName"),
		@NamedQuery(name = "Person.deleteById", query = "DELETE FROM Person p WHERE p.personId = :id") })
public class Person implements Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + personId;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (!(obj instanceof Person)) {
			return false;
		}
		Person other = (Person) obj;
		if (personId != other.personId) {
			return false;
		}
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!userName.equals(other.userName)) {
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "person_id", unique = true, nullable = false)
	private int personId;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_entry")
	private Date dateOfEntry;

	@Column(length = 64)
	private String department;

	@Column(name = "email_address", length = 64)
	private String emailAddress;

	@Column(name = "first_name", length = 64)
	private String firstName;

	@Column(name = "hashed_password", nullable = false, length = 64)
	private String hashedPassword;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	@Column(name = "is_login_allowed", nullable = false)
	private boolean isLoginAllowed;

	@Column(name = "is_online", nullable = false)
	private boolean isOnline;

	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin;

	@Column(name = "job_position", length = 64)
	private String jobPosition;

	@Column(name = "last_name", nullable = false, length = 128)
	private String lastName;

	@Column(name = "location_building", length = 64)
	private String locationBuilding;

	@Column(name = "location_floor")
	private Integer locationFloor;

	@Column(name = "location_room_number", length = 16)
	private String locationRoomNumber;

	@Column(name = "phone_number_mobile", length = 64)
	private String phoneNumberMobile;

	@Column(name = "user_name", nullable = false, length = 64)
	private String userName;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Community privateCommunity;

	// bi-directional many-to-one association to Community
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "confirmedBy")
	private List<Community> confirmedCommunities = new ArrayList<Community>();

	// bi-directional many-to-one association to Community
	@OneToMany(mappedBy = "createdBy")
	private List<Community> createdCommunities;

	// bi-directional many-to-one association to CommunityMember
	@OneToMany(mappedBy = "confirmer")
	private List<CommunityMember> confirmedMemberships;

	// bi-directional many-to-one association to CommunityMember
	@OneToMany(mappedBy = "member")
	private List<CommunityMember> memberships;

	// bi-directional many-to-one association to MesasgeRating
	@OneToMany(mappedBy = "person")
	private List<MessageRating> mesasgeRatings;

	// bi-directional many-to-one association to Message
	@OneToMany(mappedBy = "person")
	private List<Message> messages;

	// bi-directional many-to-one association to Document
	@ManyToOne
	@JoinColumn(name = "document_image_id")
	private Document document;

	// bi-directional many-to-one association to PersonMessage
	@OneToMany(mappedBy = "person")
	private List<PersonMessage> personMessages;

	// bi-directional many-to-one association to PersonRelation
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sourcePerson")
	private List<PersonRelation> personSourceRelations;

	// bi-directional many-to-one association to PersonRelation
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "targetPerson")
	private List<PersonRelation> personTargetRelations;

	// bi-directional many-to-one association to PersonTag
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = { CascadeType.PERSIST, CascadeType.DETACH } )	
	private List<PersonTag> personTags;

	@Column(name = "current_session_id", nullable = true, length = 64)
	private String currentSessionId;

	public Person() {

		isActive = true;
		isAdmin = false;
		isLoginAllowed = true;
		isOnline = false;
	}

	public Person(final String userName, final String lastName, final String firstName, final String plainPassword) {

		this();

		this.userName = userName;
		this.lastName = lastName;
		this.firstName = firstName;
		setPassword(plainPassword);
	}

	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfEntry() {
		return this.dateOfEntry;
	}

	public void setDateOfEntry(Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getHashedPassword() {
		return this.hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	/**
	 * Takes a plaintext password, hashes and stores it
	 * 
	 * @param plainPassword
	 *            the user's password in plain text
	 * 
	 * @throws WeakPasswordException
	 *             when the password is not strong enough
	 */
	public void setPassword(String plainPassword) {

		hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	/**
	 * Check whether the given plaintext password matches the user's password
	 * 
	 * @param plainPassword
	 *            the entered user's password in plain text
	 * 
	 * @return true if passwords match, false in any other case
	 */
	public boolean isMatchingPassword(String plainPassword) {

		return plainPassword != null && plainPassword.length() > 3 && BCrypt.checkpw(plainPassword, hashedPassword);
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isLoginAllowed() {
		return this.isLoginAllowed;
	}

	public void setIsLoginAllowed(boolean isLoginAllowed) {
		this.isLoginAllowed = isLoginAllowed;
	}

	public boolean isOnline() {
		return this.isOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getJobPosition() {
		return this.jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocationBuilding() {
		return this.locationBuilding;
	}

	public void setLocationBuilding(String locationBuilding) {
		this.locationBuilding = locationBuilding;
	}

	public Integer getLocationFloor() {
		return this.locationFloor;
	}

	public void setLocationFloor(Integer locationFloor) {
		this.locationFloor = locationFloor;
	}

	public String getLocationRoomNumber() {
		return this.locationRoomNumber;
	}

	public void setLocationRoomNumber(String locationRoomNumber) {
		this.locationRoomNumber = locationRoomNumber;
	}

	public String getPhoneNumberMobile() {
		return this.phoneNumberMobile;
	}

	public void setPhoneNumberMobile(String phoneNumberMobile) {
		this.phoneNumberMobile = phoneNumberMobile;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the currentSessionId
	 */
	public String getCurrentSessionId() {
		return currentSessionId;
	}

	/**
	 * @param currentSessionId
	 *            the currentSessionId to set
	 */
	public void setCurrentSessionId(String currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	public Community getPrivateCommunity() {
		return this.privateCommunity;
	}

	public void setPrivateCommunity(Community privateCommunity) {
		this.privateCommunity = privateCommunity;
	}

	public List<Community> getConfirmedCommunities() {
		return this.confirmedCommunities;
	}

	public Community addConfirmedCommunities(Community communities2) {
		getConfirmedCommunities().add(communities2);
		communities2.setConfirmedBy(this);

		return communities2;
	}

	public Community removeConfirmedCommunities(Community communities2) {
		getConfirmedCommunities().remove(communities2);
		communities2.setConfirmedBy(null);

		return communities2;
	}

	public List<Community> getCreatedCommunities() {
		return this.createdCommunities;
	}

	public Community addCreatedCommunities(Community communities3) {
		getCreatedCommunities().add(communities3);
		communities3.setCreatedBy(this);

		return communities3;
	}

	public Community removeCreatedCommunities(Community communities3) {
		getCreatedCommunities().remove(communities3);
		communities3.setCreatedBy(null);

		return communities3;
	}

	public List<CommunityMember> getConfirmedMemberships() {
		return this.confirmedMemberships;
	}

	public CommunityMember addConfirmedMemberships(CommunityMember confirmedMembership) {
		getConfirmedMemberships().add(confirmedMembership);

		return confirmedMembership;
	}

	public CommunityMember removeConfirmedMemberships(CommunityMember confirmedMembership) {
		getConfirmedMemberships().remove(confirmedMembership);

		return confirmedMembership;
	}

	public List<CommunityMember> getMemberships() {
		return this.memberships;
	}

	public CommunityMember addMembership(CommunityMember membership) {
		getMemberships().add(membership);

		return membership;
	}

	public CommunityMember removeMembership(CommunityMember membership) {
		getMemberships().remove(membership);

		return membership;
	}

	public List<MessageRating> getMesasgeRatings() {
		return this.mesasgeRatings;
	}

	public void setMesasgeRatings(List<MessageRating> mesasgeRatings) {
		this.mesasgeRatings = mesasgeRatings;
	}

	public MessageRating addMesasgeRating(MessageRating mesasgeRating) {
		getMesasgeRatings().add(mesasgeRating);
		mesasgeRating.setPerson(this);

		return mesasgeRating;
	}

	public MessageRating removeMesasgeRating(MessageRating mesasgeRating) {
		getMesasgeRatings().remove(mesasgeRating);
		mesasgeRating.setPerson(null);

		return mesasgeRating;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setPerson(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setPerson(null);

		return message;
	}

	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public List<PersonMessage> getPersonMessages() {
		return this.personMessages;
	}

	public void setPersonMessages(List<PersonMessage> personMessages) {
		this.personMessages = personMessages;
	}

	public PersonMessage addPersonMessage(PersonMessage personMessage) {
		getPersonMessages().add(personMessage);
		personMessage.setPerson(this);

		return personMessage;
	}

	public PersonMessage removePersonMessage(PersonMessage personMessage) {
		getPersonMessages().remove(personMessage);
		personMessage.setPerson(null);

		return personMessage;
	}

	public List<PersonRelation> getPersonSourceRelations() {
		return this.personSourceRelations;
	}

	public PersonRelation addPersonSourceRelation(PersonRelation personRelation) {
		getPersonSourceRelations().add(personRelation);
		personRelation.setSourcePerson(this);

		return personRelation;
	}

	public PersonRelation removePersonSourceRelation(PersonRelation personRelations) {
		getPersonSourceRelations().remove(personRelations);
		personRelations.setSourcePerson(null);

		return personRelations;
	}

	/**
	 * Remove (if exists) the relation to the given person that I started The
	 * caller is responsible for the relation object
	 * 
	 * @param personTo
	 *            the person to which to remove the relation
	 */
	public void removeRelationTo(Person personTo) {

		for (PersonRelation relation : getPersonSourceRelations()) {
			System.out.println(personTo.equals(relation.getTargetPerson()));
			if (relation.getTargetPerson() == personTo) {

				getPersonSourceRelations().remove(relation);
				break;
			}
		}
	}

	public List<PersonRelation> getPersonTargetRelations() {
		return this.personTargetRelations;
	}

	public PersonRelation addPersonTargetRelation(PersonRelation personRelations) {
		getPersonTargetRelations().add(personRelations);
		personRelations.setTargetPerson(this);

		return personRelations;
	}

	public PersonRelation removePersonTragetRelation(PersonRelation personRelations) {
		getPersonTargetRelations().remove(personRelations);
		personRelations.setTargetPerson(null);

		return personRelations;
	}

	/**
	 * Checks whether a person is generally related to an other person (either
	 * as source or target)
	 * 
	 * @param other
	 *            the person with which to check the relation
	 * 
	 * @return true if persons are related either as source or as target, false
	 *         in any other case
	 */
	public boolean isRelatedTo(Person other) {

		for (PersonRelation relation : getPersonSourceRelations()) {
			if (relation.getTargetPerson() == other) {
				return true;
			}
		}

		for (PersonRelation relation : getPersonTargetRelations()) {
			if (relation.getSourcePerson() == other) {
				return true;
			}
		}

		return false;
	}

	public List<PersonTag> getPersonTags() {
		return this.personTags;
	}

	public void setPersonTags(List<PersonTag> personTags) {
		this.personTags = personTags;
	}

	public PersonTag addPersonTag(PersonTag personTag) {
		getPersonTags().add(personTag);
		personTag.setPerson(this);

		return personTag;
	}

	public void removePersonTag(PersonTag personTag) {
		getPersonTags().remove(personTag);
		personTag.getTag().removePersonTag(personTag);
	}

	/**
	 * Is the person tagged with this tag?
	 * 
	 * @param t
	 *            the tag to check
	 * 
	 * @return true if the person is tagged with this tag, false in any other
	 *         case
	 */
	public boolean hasTag(Tag t) {

		for (PersonTag myTags : getPersonTags()) {
			if (myTags.getTag() == t) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Add a tag to this person (if not already tagged)
	 * 
	 * @param t
	 *            the tag to add
	 */
	public void addTag(Tag t) {
		if (!hasTag(t)) {

			PersonTag myTag = new PersonTag();
			myTag.setPerson(this);
			myTag.setTag(t);

			addPersonTag(myTag);
			t.addPersonTag(myTag);
		}
	}

	/**
	 * Remove a tag from this person does nothing if the tag is not set
	 * 
	 * @param t
	 *            the tag to remove
	 */
	public void removeTag(Tag t) {

		for (PersonTag myTag : getPersonTags()) {

			if (myTag.getTag() == t) {

				removePersonTag(myTag);
				break;
			}
		}
	}

	/**
	 * Creates a set containing all contacts of the user
	 * 
	 * @return a set of all contacts
	 */
	public Set<Person> getContacts() {
		Set<Person> contacts = new HashSet<Person>();

		if (getPersonTargetRelations() != null) {
			for (PersonRelation pr : getPersonTargetRelations()) {
				contacts.add(pr.getSourcePerson());

			}
		}

		if (getPersonSourceRelations() != null) {
			for (PersonRelation pr : getPersonSourceRelations()) {
				contacts.add(pr.getTargetPerson());
			}
		}
		return contacts;
	}
}