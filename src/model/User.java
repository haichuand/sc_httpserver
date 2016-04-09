package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.*;
import javax.persistence.*;

@XmlRootElement
@Entity
@Table(name = "user",schema="public")
public class User {
	
	private int uId;
	private String gcmId;
	private String mediaId;
	private String email;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String userName;
	private Set<Event> events = new HashSet<>();

	public User() {}
    
	public User (int uId) {
		this.uId = uId;
	}
	
    public User(int uId, String mediaId, String gcmId, String email, String phoneNumber, String firstName, String lastName, String userName) {
    	this.uId = uId;
    	this.mediaId = mediaId;
    	this.gcmId = gcmId;
    	this.email = email;
    	this.phoneNumber = phoneNumber;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.userName = userName;
    }
    
    @Id @GeneratedValue
	@Column(name = "u_id")
    public int getId() {
    	return this.uId;
    }
    
    public void setId(int uId) {
    	this.uId = uId;
    }
    
	@Column(name = "gcm_id")
    public String getGcmId() {
    	return this.gcmId;
    }
    
    public void setGcmId(String gcmId) {
    	this.gcmId = gcmId;
    }
    
    @Column(name = "media_id")
    public String getMediaId() {
    	return this.mediaId;
    }

    public void setMediaId(String mediaId) {
    	this.mediaId = mediaId;
    }
    
    @Column(name = "email")
    public String getEmail() {
    	return this.email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    @Column(name = "phone_number")
    public String getPhoneNumber() {
    	return this.phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
    	this.phoneNumber = phoneNumber;
    }
    
    @Column(name = "first_name")
    public String getFirstName() {
    	return this.firstName;
    }
    
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    @Column(name = "last_name")
    public String getLastName() {
    	return this.lastName;
    }
    
    public void setLastName(String lastName) {
    	this.lastName = lastName;
    }
    
    @Column(name = "user_name")
    public String getUserName() {
    	return this.userName;
    }
    
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    @XmlElementWrapper (name="events")
    @XmlElement(name="event")
    @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL, mappedBy="attendees")
    public Set<Event> getEvents() {
    	//System.out.println("getEvent !!!!!!!!!!!!!!!!!!!!!!1\n\n");
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
