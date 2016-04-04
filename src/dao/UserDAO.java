package dao;

import model.User;

public interface UserDAO {
	public String create(User user);
	public void edit(User user);
	public void delete(String uId);
	public User getUser(int uId);
}
