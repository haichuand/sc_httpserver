package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "conversation",schema="public")
public class Conversation {
	private String cId;
	private String title;
	private int creatorId;
  private List attendees;
  private List messages;
	
	@Id
	@Column(name = "c_id")
	public String getcId() {
		return cId;
	}

  @XmlElementWrapper(name="users")
  @XmlElement(name="user")
  @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL) 
  @JoinTable(name="conversation_attendee",
      joinColumns=@JoinColumn(name="c_id"),
      inverseJoinColumns=@JoinColumn(name="u_id"))
  public List getAttendees() {
    System.out.println ("Get Conversation attendees.");
    return attendees;
  }

  public List setAttendees(List atts) {
    attendees = atts;
  }

  @XmlElementWrapper(name="messages")
  @XmlElement(name="message")
  @OneToMany
  @JoinColumn(name="c_id")
  public List getMessages() {
    return messages;
  }

  public List setMessages(List msgs) {
    messages = msgs;
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
	//TODO might be directly refer to a user
	@Column(name = "creator_id")
	public int getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	
}

