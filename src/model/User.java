package model;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.*;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlRootElement
@Entity
@Table(name = "user",schema="public")
public class User {
	
	private int uId;
	private String fcmId;
	private String mediaId;
	private String email;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private Set<Event> events = new HashSet<>();
	private Set<Conversation> convsations = new HashSet<>();
	private Set<User> friends = new HashSet<>();

	public User() {}
    
	public User (int uId) {
		this.uId = uId;
	}
	
    public User(int uId, String mediaId, String fcmId, String email, String phoneNumber, String firstName, String lastName, String userName) {
    	this.uId = uId;
    	this.mediaId = mediaId;
    	this.fcmId = fcmId;
    	this.email = email;
    	this.phoneNumber = phoneNumber;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.userName = userName;
    }
    
    @Id @GeneratedValue
	@Column(name = "u_id")
    public int getuId() {
    	return this.uId;
    }
    
    public void setuId(int uId) {
    	this.uId = uId;
    }
    
	@Column(name = "fcm_id")
    public String getFcmId() {
    	return this.fcmId;
    }
    
    public void setFcmId(String fcmId) {
    	this.fcmId = fcmId;
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
    
    @Column(name = "password")
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    @JsonIgnore
    @XmlElementWrapper (name="events")
    @XmlElement(name="event")
    @ManyToMany(fetch = FetchType.LAZY,cascade={CascadeType.ALL}, mappedBy="attendees")
    public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	
	@JsonIgnore
	@XmlElementWrapper (name="conversations")
	@XmlElement(name="conversation")
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL, mappedBy="attendees")
	public Set<Conversation> getConvsations() {
		return convsations;
	}

	public void setConvsations(Set<Conversation> convsations) {
		this.convsations = convsations;
	}
	
	@JsonIgnore
	@XmlElementWrapper (name="friends")
	@XmlElement(name="friend")
	@ManyToMany (fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_friend",
            joinColumns ={ @JoinColumn(name = "u_id") },
            inverseJoinColumns ={ @JoinColumn(name = "friend_id") })
	public Set<User> getFriends() {
		return friends;
	}
	
	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}
	
	public String toJsonString() {
		String jsonString = "{\"uId\":" + this.uId +
							", \"fcmId\":" + "\"" +this.fcmId + "\"" +
 							", \"mediaId\":" + "\"" + this.mediaId + "\"" +
							", \"email\":" + "\"" + this.email + "\"" +
							", \"phoneNumber\":" + "\"" + this.phoneNumber + "\"" +
							", \"firstName\":" + "\"" + this.firstName + "\"" +
							", \"lastName\":" + "\"" + this.lastName + "\"" +
							", \"userName\":" + "\"" + this.userName + "\"" +
							"}";
		return jsonString;
	}
}
