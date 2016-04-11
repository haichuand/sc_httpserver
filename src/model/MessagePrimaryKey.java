package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class MessagePrimaryKey implements Serializable{
	
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
	
	public boolean equals(MessagePrimaryKey other) {
		return cId == other.cId && uId == other.uId && timestamp == other.timestamp;
	}
	
	@Override
    public int hashCode() {
        int hash = 5;
        hash = hash + (this.cId != null ? this.cId.hashCode() : 0);
        hash = hash + (int) (this.uId ^ (this.uId >>> 32));
        hash = hash + (int)this.timestamp;
        return hash;
    }
	
	
}
