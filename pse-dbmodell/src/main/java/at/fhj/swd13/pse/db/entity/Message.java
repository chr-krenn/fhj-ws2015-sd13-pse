package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jsoup.Jsoup;

/**
 * The persistent class for the message database table.
 * 
 */
@Entity
@Table(name = "message")
@NamedQueries({
		@NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
		@NamedQuery(name = "Message.findAllOrderedByNewest", query = "SELECT m FROM Message m ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.messageId = :id"),
		@NamedQuery(name = "Message.findNews", query = "SELECT m FROM Message m JOIN m.communities c WHERE " +
			    "(m.expiresOn IS NULL OR m.expiresOn > CURRENT_TIMESTAMP) " +
			    "AND (m.validFrom < CURRENT_TIMESTAMP) AND c.communityId = :id ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.findForUserWithCommunitiesParam", query = "SELECT m FROM Message m LEFT JOIN m.communities c " +
				"WHERE m.person <> :person AND m.messageBean IS NULL AND (m.expiresOn IS NULL OR m.expiresOn > CURRENT_TIMESTAMP) " +
				"AND (m.validFrom IS NULL OR m.validFrom <= CURRENT_TIMESTAMP) " +
				"AND c.systemInternal = false AND (c.communityId IS NULL OR c.communityId IN (:communities)) " +
				"ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.findForUser", query = "SELECT m FROM Message m LEFT JOIN m.communities c LEFT JOIN c.communityMembers cm " +
				"WHERE m.person <> :person AND m.messageBean IS NULL AND (m.expiresOn IS NULL OR m.expiresOn > CURRENT_TIMESTAMP) " +
				"AND (m.validFrom IS NULL OR m.validFrom <= CURRENT_TIMESTAMP) " +
				"AND (c.communityId IS NULL OR (c.systemInternal = false AND :person = cm.member)) ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.findByTags", query = "SELECT m FROM Message m LEFT JOIN m.messageTags mt LEFT JOIN m.communities c " +
				"WHERE m.person <> :person AND m.messageBean IS NULL AND (m.expiresOn IS NULL OR m.expiresOn > CURRENT_TIMESTAMP) " +
				"AND (m.validFrom IS NULL OR m.validFrom <= CURRENT_TIMESTAMP) " +
				"AND mt.tag IN (:tags) AND c.systemInternal = false AND c.invitationOnly = false ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.findByContacts", query = "SELECT m FROM Message m LEFT JOIN m.communities c " +
				"WHERE m.person <> :person AND m.messageBean IS NULL AND (m.expiresOn IS NULL OR m.expiresOn > CURRENT_TIMESTAMP) " +
				"AND (m.validFrom IS NULL OR m.validFrom <= CURRENT_TIMESTAMP) " +
				"AND m.person in (select p from Person p LEFT JOIN p.personSourceRelations sr LEFT JOIN p.personTargetRelations tr " +
					"where sr.targetPerson = :person or tr.sourcePerson = :person) AND c.systemInternal = false AND c.invitationOnly = false " +
				"ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.findForUserAndTagsAndContacts", query = "SELECT m FROM Message m LEFT JOIN m.messageTags mt " +
				"LEFT JOIN m.communities c LEFT JOIN c.communityMembers cm " +
				"WHERE m.person <> :person AND m.messageBean IS NULL AND (m.expiresOn IS NULL OR m.expiresOn > CURRENT_TIMESTAMP) " +
				"AND (m.validFrom IS NULL OR m.validFrom <= CURRENT_TIMESTAMP) " +
				"AND (c.communityId IS NULL OR :person = cm.member " +
					"OR ((mt.tag IN (SELECT t FROM Tag t LEFT JOIN t.personTags p where p.person = :person) " +
					"OR m.person in (select p from Person p LEFT JOIN p.personSourceRelations sr LEFT JOIN " +
						"p.personTargetRelations tr WHERE sr.targetPerson = :person or tr.sourcePerson = :person)) " +
						"AND c.systemInternal = false AND c.invitationOnly = false)) " +
				"ORDER BY m.createdAt DESC"),
		@NamedQuery(name = "Message.deleteById", query = "DELETE FROM Message m WHERE m.messageId = :id"),
		@NamedQuery(name="Message.findComments", query = "SELECT m FROM Message m where m.messageBean = :message")})
public class Message implements Serializable {
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + messageId;
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
		if (!(obj instanceof Message)) {
			return false;
		}
		Message other = (Message) obj;
		if (messageId != other.messageId) {
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id", unique = true, nullable = false)
	private int messageId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expires_on")
	private Date expiresOn;

	@Column(length = 45)
	private String headline;

	@Column(nullable = false, length = 2048)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_from", nullable = false)
	private Date validFrom;

	// bi-directional many-to-one association to MesasgeRating
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "message")
	private List<MessageRating> messageRatings;

	// bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name = "commented_on_message_id")
	private Message messageBean;

	// bi-directional many-to-one association to Message
	@OneToMany(mappedBy = "messageBean", cascade=CascadeType.PERSIST)
	private List<Message> messages;

	// bi-directional many-to-one association to DeliverySystem
	@ManyToOne
	@JoinColumn(name = "delivered_by", nullable = false)
	private DeliverySystem deliverySystem;

	// bi-directional many-to-one association to Document
	@ManyToOne
	@JoinColumn(name = "document_attachment_id")
	private Document attachment;

	// bi-directional many-to-one association to Document
	@ManyToOne
	@JoinColumn(name = "document_icon_id")
	private Document icon;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private Person person;

	// bi-directional many-to-one association to Community
	@ManyToMany (fetch = FetchType.EAGER)
	private List<Community> communities;

	// bi-directional many-to-one association to MessageTag
	@ManyToMany(mappedBy = "messages", cascade=CascadeType.PERSIST)
	private List<MessageTag> messageTags = new ArrayList<MessageTag>();

	// bi-directional many-to-one association to PersonMessage
	@OneToMany(mappedBy = "message")
	private List<PersonMessage> personMessages;

	/**
	 * before the update set the updatedOn property
	 */
	@PreUpdate
	private void onPreUpdate() {
		updatedOn = new Date();
	}
	
	public Message() {
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getExpiresOn() {
		return this.expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	public String getHeadline() {
		return this.headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getMessage() {
		return this.message;
	}

	public String getPlainMessage() {
		return Jsoup.parse(message).text();
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getValidFrom() {
		return this.validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public List<MessageRating> getMessageRatings() {
		return this.messageRatings;
	}

	public void setMessageRatings(List<MessageRating> messageRatings) {
		this.messageRatings = messageRatings;
	}

	public MessageRating addMesasgeRating(MessageRating messageRating) {
		getMessageRatings().add(messageRating);
		messageRating.setMessage(this);

		return messageRating;
	}

	public MessageRating removeMesasgeRating(MessageRating messageRating) {
		getMessageRatings().remove(messageRating);
		messageRating.setMessage(null);

		return messageRating;
	}

	public Message getMessageBean() {
		return this.messageBean;
	}

	public void setMessageBean(Message messageBean) {
		this.messageBean = messageBean;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setMessageBean(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setMessageBean(null);

		return message;
	}

	public DeliverySystem getDeliverySystem() {
		return this.deliverySystem;
	}

	public void setDeliverySystem(DeliverySystem deliverySystem) {
		this.deliverySystem = deliverySystem;
	}

	public Document getAttachment() {
		return this.attachment;
	}

	public void setAttachment(Document document) {
		this.attachment = document;
	}

	public Document getIcon() {
		return this.icon;
	}

	public void setIcon(Document document) {
		this.icon = document;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Community> getCommunities() {
		return this.communities;
	}

	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}

	public List<MessageTag> getMessageTags() {
		return this.messageTags;
	}

	public void setMessageTags(List<MessageTag> messageTags) {
		for(MessageTag messageTag : messageTags){
			this.addMessageTag(messageTag);
		}
	}

	public MessageTag addMessageTag(MessageTag messageTag) {
		messageTags.add(messageTag);
		messageTag.getMessages().add(this);

		return messageTag;
	}

	public MessageTag removeMessageTag(MessageTag messageTag) {
		messageTags.remove(messageTag);
		messageTag.getMessages().remove(messageTag);

		return messageTag;
	}

	public List<PersonMessage> getPersonMessages() {
		return this.personMessages;
	}

	public void setPersonMessages(List<PersonMessage> personMessages) {
		this.personMessages = personMessages;
	}

	public PersonMessage addPersonMessage(PersonMessage personMessage) {
		getPersonMessages().add(personMessage);
		personMessage.setMessage(this);

		return personMessage;
	}

	public PersonMessage removePersonMessage(PersonMessage personMessage) {
		getPersonMessages().remove(personMessage);
		personMessage.setMessage(null);

		return personMessage;
	}

}