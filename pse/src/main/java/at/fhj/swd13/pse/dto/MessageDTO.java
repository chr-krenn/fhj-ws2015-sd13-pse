package at.fhj.swd13.pse.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;
import at.fhj.swd13.pse.db.entity.MessageRating;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.db.entity.Person;

public class MessageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String author;
	
	private Date date;
	
	private String headline;
	
	private String text;
	
	private boolean privateMessage;
	
	//Comma-separated string listing all communities
	private String community;
	
	private List<Community> communities;
	
	private Document image;
	
	private String imageRef;
	
	private boolean like;
	
	private List<MessageRating> ratingList;
	
	private int quantityRatings;
	
	private List<Person> ratingPersonsList;
	
	private Integer numberOfComments;
	
	private List<MessageDTO> comments;
	
	private boolean isComment;
	
	private Date validFrom;
	
	private Date validUntil;
	
	private List<String> tags;
	
	private Document attachement;
	
	private MessageDTO() {}
	
	public MessageDTO(Message m) {
		this();
		this.id = m.getMessageId();
		this.author = m.getPerson().getFirstName() +" " + m.getPerson().getLastName();
		this.date = m.getCreatedAt();
		this.headline = m.getHeadline();
		this.text = m.getMessage();
		this.privateMessage = isPrivateMessage(m);
		this.community = getCommunity(m);
		this.communities = m.getCommunities();
		this.image = m.getIcon();
		this.attachement = m.getAttachment();
		this.ratingList = m.getMessageRatings();
		this.like = false;
		this.setIsComment(m.getMessageBean() != null);
		this.validFrom = m.getValidFrom();
		this.validUntil = m.getExpiresOn();
		
		tags = new ArrayList<String>();
		for(MessageTag tag : m.getMessageTags()){
			tags.add(tag.getTag().getToken());
		}
	}
	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public boolean isPrivateMessage() {
		return privateMessage;
	}

	/**
	 * Checks whether the message has been posted in a private community
	 * If the message has been posted in a community and privately at the same time,
	 *  it is considered private.
	 *  
	 * @param m - the Message
	 * @return true if message has been posted to at least one person privately
	 */
	public boolean isPrivateMessage(Message m) {
		privateMessage = false;
		for(Community c : m.getCommunities()) {
			if(c.getPrivateUser() != null) {
				privateMessage = true;
			}
		}
		return privateMessage;
	}
	
	public void setPrivateMessage(boolean privateMessage) {
		this.privateMessage = privateMessage;
	}
	
	public String getCommunity() {
		return community;
	}

	public String getCommunity(Message m) {
		community = "";
		for(Community c : m.getCommunities()) {
			if(! community.equals("")) {
				community += ", ";
			}
			community += c.getName();
		}
		return community;
	}
	
	public void setCommunity(String community) {
		this.community = community;
	}
	
	public List<Community> getCommunities() {
		return communities;
	}
	
	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}

	public Document getImage() {
		return image;
	}

	public void setImage(Document image) {
		this.image = image;
	}

	public String getImageRef() {
		return imageRef;
	}

	
	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	
	public boolean isLike() {
		return like;
	}

	
	public void setLike(boolean like) {
		this.like = like;
	}

	
	public List<MessageRating> getRatingList() {
		return ratingList;
	}

	
	public void setRatingList(List<MessageRating> ratingList) {
		this.ratingList = ratingList;
	}
	
	
	public int getQuantityRatings() {
		return quantityRatings;
	}


	public void setQuantityRatings(int quantityRatings) {
		this.quantityRatings = quantityRatings;
	}


	public List<Person> getRatingPersonsList() {
		return ratingPersonsList;
	}


	public void setRatingPersonsList(List<Person> ratingPersonsList) {
		this.ratingPersonsList = ratingPersonsList;
	}

	
	public Integer getNumberOfComments() {
		return numberOfComments;
	}

	
	public void setNumberOfComments(Integer numberOfComments) {
		this.numberOfComments = numberOfComments;
	}

	
	public List<MessageDTO> getComments() {
		return comments;
	}

	
	public void setComments(List<MessageDTO> comments) {
		this.comments = comments;
		setNumberOfComments(comments.size());
	}

	public boolean getIsComment() {
		return isComment;
	}

	public void setIsComment(boolean isComment) {
		this.isComment = isComment;
	}

	
	/**
	 * @return the validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}

	
	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	
	/**
	 * @return the validUntil
	 */
	public Date getValidUntil() {
		return validUntil;
	}

	
	/**
	 * @param validUntil the validUntil to set
	 */
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	
	public boolean isShowable() {
		final Date now = new Date();
		
		return ( null == validFrom || validFrom.before( now ) )
				&& ( null == validUntil || validUntil.after( now ) );
	}
	
	/**
	 * @param isComment the isComment to set
	 */
	public void setComment(boolean isComment) {
		this.isComment = isComment;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public Document getAttachement() {
		return attachement;
	}

	public void setAttachement(Document attachement) {
		this.attachement = attachement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageDTO other = (MessageDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageDTO [id=" + id + ", author=" + author + ", date=" + date + ", headline=" + headline + ", text=" + text + "]";
	}
	
}
