package at.fhj.swd13.pse.db.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the message database table.
 * 
 */
@Entity
@Table(name="message")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="message_id", unique=true, nullable=false)
	private int messageId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_on", nullable=false)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expires_on")
	private Date expiresOn;

	@Column(length=45)
	private String headline;

	@Column(nullable=false, length=2048)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_on")
	private Date updatedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="valid_from", nullable=false)
	private Date validFrom;

	//bi-directional many-to-one association to MesasgeRating
	@OneToMany(mappedBy="message")
	private List<MesasgeRating> mesasgeRatings;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="commented_on_message_id")
	private Message messageBean;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="messageBean")
	private List<Message> messages;

	//bi-directional many-to-one association to DeliverySystem
	@ManyToOne
	@JoinColumn(name="delivered_by", nullable=false)
	private DeliverySystem deliverySystem;

	//bi-directional many-to-one association to Document
	@ManyToOne
	@JoinColumn(name="document_attachment_id")
	private Document attachment;

	//bi-directional many-to-one association to Document
	@ManyToOne
	@JoinColumn(name="document_icon_id")
	private Document icon;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Person person;

	//bi-directional many-to-one association to Community
	@ManyToOne
	@JoinColumn(name="posted_in")
	private Community community;

	//bi-directional many-to-one association to MessageTag
	@OneToMany(mappedBy="message")
	private List<MessageTag> messageTags;

	//bi-directional many-to-one association to PersonMessage
	@OneToMany(mappedBy="message")
	private List<PersonMessage> personMessages;

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

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
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

	public List<MesasgeRating> getMesasgeRatings() {
		return this.mesasgeRatings;
	}

	public void setMesasgeRatings(List<MesasgeRating> mesasgeRatings) {
		this.mesasgeRatings = mesasgeRatings;
	}

	public MesasgeRating addMesasgeRating(MesasgeRating mesasgeRating) {
		getMesasgeRatings().add(mesasgeRating);
		mesasgeRating.setMessage(this);

		return mesasgeRating;
	}

	public MesasgeRating removeMesasgeRating(MesasgeRating mesasgeRating) {
		getMesasgeRatings().remove(mesasgeRating);
		mesasgeRating.setMessage(null);

		return mesasgeRating;
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

	public Community getCommunity() {
		return this.community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public List<MessageTag> getMessageTags() {
		return this.messageTags;
	}

	public void setMessageTags(List<MessageTag> messageTags) {
		this.messageTags = messageTags;
	}

	public MessageTag addMessageTag(MessageTag messageTag) {
		getMessageTags().add(messageTag);
		messageTag.setMessage(this);

		return messageTag;
	}

	public MessageTag removeMessageTag(MessageTag messageTag) {
		getMessageTags().remove(messageTag);
		messageTag.setMessage(null);

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