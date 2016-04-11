package model;

import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement
@Entity
@Table(name = "conversation", schema = "public")
public class Conversation {
	private String cId;
	private String title;
	private int creatorId;
	private Set<User> attendees;
	private List<Message> messages;
	private List<Integer> attendeesId;

	@Id
	@Column(name = "c_id")
	public String getcId() {
		return cId;
	}
	
	public void setcId(String cId) {
		this.cId = cId;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "creator_id")
	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	
	@XmlElementWrapper(name = "attendees")
	@XmlElement(name = "attendee")
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "conversation_attendee", 
				joinColumns = @JoinColumn(name = "c_id"), 
				inverseJoinColumns = @JoinColumn(name = "u_id"))
	public Set<User> getAttendees() {
		System.out.println("Get Conversation attendees.");
		return attendees;
	}

	public void setAttendees(Set<User> atts) {
		attendees = atts;
	}

	@XmlElementWrapper(name = "messages")
	@XmlElement(name = "message")
	@OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL, mappedBy="conversation")
	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List msgs) {
		messages = msgs;
	}
	
	@Transient
	public List<Integer> getAttendeesId() {
		return attendeesId;
	}

	public void setAttendeesId(List<Integer> attendeesId) {
		this.attendeesId = attendeesId;
	}

}
