package at.fhj.swd13.pse.dto;

import java.util.Date;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Message;

public class MessageDTO {

	private int id;
	
	private String author;
	
	private Date date;
	
	private String headline;
	
	private String text;
	
	private boolean privateMessage;
	
	private String community;
	
	private Document icon;

	private MessageDTO() {}
	
	public MessageDTO(Message m) {
		this();
		this.id = m.getMessageId();
		this.author = m.getPerson().getFirstName() + m.getPerson().getLastName();
		this.date = m.getCreatedAt();
		this.headline = m.getHeadline();
		this.text = m.getMessage();
		this.privateMessage = isPrivateMessage(m);
		this.community = getCommunity(m);
		this.icon = m.getIcon();
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
	
	/*
	 * FIXME: If the message has been posted in a community and privately at the same time,
	 *  it is considered private. Possible use case? 
	 */
	/**
	 * Checks whether the message has been posted in a private community
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
			if(community != "") {
				community += ", ";
			}
			community += c.getName();
		}
		return community;
	}
	
	public void setCommunity(String community) {
		this.community = community;
	}

	
	public Document getIcon() {
		return icon;
	}

	
	public void setIcon(Document icon) {
		this.icon = icon;
	}
}
