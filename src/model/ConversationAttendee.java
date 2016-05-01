package model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConversationAttendee {
	private String cId;
	private List<Integer> attendeesId;
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public List<Integer> getAttendeesId() {
		return attendeesId;
	}
	public void setTargetUid(List<Integer> attendeesId) {
		this.attendeesId = attendeesId;
	}
}
