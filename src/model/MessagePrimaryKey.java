package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MessagePrimaryKey {
	
	private String cId;
	private int uId;
	private long timestamp;
	
	@Column(name = "c_id")
	public String getcId() {
		return cId;
	}
	
	public void setcId(String cId) {
		this.cId = cId;
	}
	
	@Column(name = "sender_id")
	public int getuId() {
		return uId;
	}
	
	public void setuId(int uId) {
		this.uId = uId;
	}
	
	@Column(name = "timestamp")
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
