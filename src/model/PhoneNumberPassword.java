package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhoneNumberPassword {
	private String phoneNumber;
	private String password;
	
	public PhoneNumberPassword() {}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
