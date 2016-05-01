package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConversationAttendee {
	private String cId;
	private int attendeeId;
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public int getAttendeeId() {
		return attendeeId;
	}
	public void setTargetUid(int attendeeId) {
		this.attendeeId = attendeeId;
	}
}
