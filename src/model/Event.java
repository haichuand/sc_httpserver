package model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;


@XmlRootElement
@Entity
@Table(name = "event",schema="public")
public class Event {
	
	private String eventId;
	private String eventType;
	private String title;
	private String location;
	private String startTime;
	private String endTime;
	private String creatorId;
	private String createTime;
	private Set<User> attendees;
	
	public Event() {}
    
	public Event (String eventId) {
		this.eventId = eventId;
	}
	
	@Id
	@Column(name = "event_id")
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	@Column(name = "event_Type")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "start_time")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "creator_id")
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	@Column(name = "create_time")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@XmlElementWrapper (name="attendees")
    @XmlElement(name="attendee")
	@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)  
    @JoinTable(name="event_attendees", 
    		    joinColumns=@JoinColumn(name="event_id"), 
    			inverseJoinColumns=@JoinColumn(name="u_id"))  
	public Set<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<User> attendees) {
		this.attendees = attendees;
	}

	
}
