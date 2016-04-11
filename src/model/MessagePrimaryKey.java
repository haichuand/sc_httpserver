package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class MessagePrimaryKey implements Serializable{
	
	private String cId;
	private int senderId;
	private long timestamp;
	
	public MessagePrimaryKey() {}
	
	@Column(name = "c_id")
	public String getcId() {
		return cId;
	}
	
	public void setcId(String cId) {
		this.cId = cId;
	}
	
	@Column(name = "sender_id")
	public int getSenderId() {
		return senderId;
	}
	
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	
	@Column(name = "timestamp")
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public boolean equals(Object other) {
		return cId == ((MessagePrimaryKey)other).cId && senderId == ((MessagePrimaryKey)other).senderId && timestamp == ((MessagePrimaryKey)other).timestamp;
	}
	
	@Override
    public int hashCode() {
        int hash = 5;
        hash = hash + (this.cId != null ? this.cId.hashCode() : 0);
        hash = hash + (int) (this.senderId ^ (this.senderId >>> 32));
        hash = hash + (int)this.timestamp;
        return hash;
    }
	
	
}
