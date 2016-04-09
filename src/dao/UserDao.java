package dao;

import model.User;
import java.util.Set;

public interface UserDao {
	public String create(User user);
	public void edit(User user);
	public void delete(String uId);
	public User getUser(int uId);
	public Set getUserEvents(int uId);
}
