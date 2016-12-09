package dao;

import model.EmailPhoneNumber;
import model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
	public int create(User user);
	public void edit(User user);
	public void delete(int uId);
	public User getUser(int uId);
	public Set getUserEvents(int uId);
	public Set getUserConversations(int uId);
	public void addFriend (User user);
	public Set getUserFriends (int u_id);
	public User getUserByEmail(String email);
	public User getUserByPhoneNumber(String phoneNumber);
	public List<User> getAllUser();
	public List<User> getContactSuggestion(EmailPhoneNumber contact);
}
