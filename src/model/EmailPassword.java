package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailPassword {
	private String email;
	private String password;
	
	public EmailPassword() {}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
 	
}
