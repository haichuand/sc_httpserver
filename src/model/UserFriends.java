package model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserFriends {
	private int uId;
	private List<Integer> friendsId;
	
	public UserFriends() {}
	
	public int getuId() {
		return uId;
	}
	
	public void setuId(int uId) {
		this.uId = uId;
	}
	
	public List<Integer> getFriendsId() {
		return friendsId;
	}
	
	public void setFirendsId(List<Integer> friendsId) {
		this.friendsId = friendsId;
	}
}
