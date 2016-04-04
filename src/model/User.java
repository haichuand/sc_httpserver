package model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

@XmlRootElement
@Entity
@Table(name = "user",schema="public")
public class User {
	@Id @GeneratedValue
	@Column(name = "u_id")
	private int uId;
	
	@Column(name = "gcm_id")
	private String gcmId;
	
	@Column(name = "media_id")
	private String mediaId;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "user_name")
	private String userName;
	
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
    
    public int getId() {
    	return this.uId;
    }
    
    public void setId(int uId) {
    	this.uId = uId;
    }
    
    public String getGcmId() {
    	return this.gcmId;
    }
    
    public void setGcmId(String gcmId) {
    	this.gcmId = gcmId;
    }
    
    public String getMediaId() {
    	return this.mediaId;
    }
    
    public void setMediaId(String mediaId) {
    	this.mediaId = mediaId;
    }
    
    public String getEmail() {
    	return this.email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    public String getPhoneNumber() {
    	return this.phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
    	this.phoneNumber = phoneNumber;
    }
    
    public String getFirstName() {
    	return this.firstName;
    }
    
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    public String getLastName() {
    	return this.lastName;
    }
    
    public void setLastName(String lastName) {
    	this.lastName = lastName;
    }
    
    public String getUserName() {
    	return this.userName;
    }
    
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    
}
