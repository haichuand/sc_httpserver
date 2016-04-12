package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;


@XmlRootElement
@Entity
@Table(name = "event",schema="public")
public class Event {
	
	private String eventId;
	private String eventType;
	private String title;
	private String location;
	private long startTime;
	private long endTime;
	private int creatorId;
	private long createTime;
	private Set<User> attendees = new HashSet<>();
	private List<Integer> attendeesId;

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
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Column(name = "creator_id")
	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	@Column(name = "create_time")
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	@XmlElementWrapper (name="attendees")
    @XmlElement(name="attendee")
	@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)  
    @JoinTable(name="event_attendee", 
    		    joinColumns=@JoinColumn(name="event_id"), 
    			inverseJoinColumns=@JoinColumn(name="u_id"))  
	public Set<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<User> attendees) {
		this.attendees = attendees;
	}
	
	@Transient
	public List<Integer> getAttendeesId() {
		return attendeesId;
	}

	public void setAttendeesId(List<Integer> attendeesId) {
		this.attendeesId = attendeesId;
	}
}
