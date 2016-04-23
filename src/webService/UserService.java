package webService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.Conversation;
import model.Event;
import model.User;

@Path("/user")
public class UserService {
	private final int STATUS_OK = 0;
	private final int STATUS_NO_USER = 1;
	private final int STATUS_WRONG_PASSWORD = 2;
	private final int STATUS_ERROR = 3;
	
	private static UserDao userDao = new UserDaoImpl();
	
	// The @Context annotation allows us to have certain contextual objects
    // injected into this class.
    // UriInfo object allows us to get URI information.
    @Context
    UriInfo uriInfo;
 
    // Another "injected" object. This allows us to use the information that's
    // part of any incoming request.
    // We could, for example, get header information, or the requestor's address.
    @Context
    Request request;
    
 // Basic "is the service running" test
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Demo service for user is ready!";
    }
    
    @GET
    @Path("/basicInfo/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserBasicInfoById(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);
    	if(user != null)
    		user.setPassword(null);
    	System.out.println("client is request the info of user: " + userId);
    	return user;
    }
    
    @GET
    @Path("/getUserByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByEmail(@PathParam("email")String email) {
    	User user = userDao.getUserByEmail(email);
    	if(user != null)
    		user.setPassword(null);
    	System.out.println("client is request the info of user: " + email);
    	return user;
    }
    
    @GET
    @Path("/getUserByPhoneNumber/{phoneNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByPhoneNumber(@PathParam("phoneNumber")String phoneNumber) {
    	User user = userDao.getUserByPhoneNumber(phoneNumber);
    	if(user != null)
    		user.setPassword(null);
    	System.out.println("client is request the info of user: " + phoneNumber);
    	return user;
    }
    
    @GET
    @Path("/getUserGcmId/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserGcmId(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);
    	String gcmId = null;
    	if(user != null) {
    		gcmId = user.getGcmId();
    		return "{\"gcmId\": \"" +gcmId + "\"}" ; 
    	}
    	
    	return "{\"gcmId\": " +gcmId + "}" ;
    }
    
    @GET
    @Path("/getAllUserIdAndGcmId")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUserGcmId() {
    	List<User> users = userDao.getAllUser();
    	String result = "{\"usersGcmId\": [";
    	if(!users.isEmpty()) {
    		for(int i = 0; i < users.size(); i++) {
    			User user = users.get(i);
    			result = result + "{\"uId\" : "+ user.getuId()+", "+"\"gcmId\": \""+ user.getGcmId()+"\"}";
    			if(i < users.size()-1)
    				result += ", ";
    		}
    		result += "]}";
    	}
    	return result;
    }
    
    @GET
    @Path("/getAllUserId")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUserId() {
    	List<User> users = userDao.getAllUser();
    	String result = "{\"allUserId\": [";
    	if(!users.isEmpty()) {
    		for(int i = 0; i < users.size(); i++) {
    			User user = users.get(i);
    			result += user.getuId() ;
    			if(i < users.size()-1)
    				result += ", ";
    		}
    		result += "]}";
    	}
    	return result;
    }
    
    @GET
    @Path("/userEvents/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Event> getEvensByUserId(@PathParam("userId")int userId) {
    	Set<Event> events = userDao.getUserEvents(userId);
    	System.out.println("client is request the info of user: " + userId);
    	return events;
    }
    
    @GET
    @Path("/userConversations/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Conversation> getConversationsByUserId(@PathParam("userId")int userId) {
    	Set<Conversation> conversations = userDao.getUserConversations(userId);
    	System.out.println("client is request the info of user: " + userId);
    	return conversations;
    }
    
    @GET
    @Path("/userFriendList/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<User> getFriendsByUserId(@PathParam("userId")int userId) {
    	Set<User> friends = userDao.getUser(userId).getFriends();
    	for(User friend: friends) 
    		friend.setPassword(null);
    	System.out.println("client is request the friend list of user: " + userId);
    	return friends;
    }
    
    @POST
    @Path("/verifyUserByEmail/{email}/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public String verifyUserByEmail(@PathParam("email")String email, @PathParam("password")String password) {
    	User user = userDao.getUserByEmail(email);
    	if(user == null)
    		return "{\"status\": "+ this.STATUS_NO_USER + "}";
    	else if (!user.getPassword().equals(password)){
    		return "{\"status\": "+ this.STATUS_WRONG_PASSWORD + "}";
    		
    	}else
    		return "{\"status\": "+ this.STATUS_OK + "}";
    }
    
    @POST
    @Path("/verifyUserByPhoneNumber/{phoneNumber}/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public String verifyUserByPhoneNumber(@PathParam("phoneNumber")String phoneNumber, @PathParam("password")String password) {
    	User user = userDao.getUserByPhoneNumber(phoneNumber);
    	if(user == null)
    		return "{\"status\": "+ this.STATUS_NO_USER + "}";
    	else if (!user.getPassword().equals(password)){
    		return "{\"status\": "+ this.STATUS_WRONG_PASSWORD + "}";
    		
    	}else
    		return "{\"status\": "+ this.STATUS_OK + "}";
    }
    
    // Use data from the client source to create a new User object, returned in JSON format.  
    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String postUser(User user) {
    	String email = user.getEmail();
    	String phoneNumber = user.getPhoneNumber();
    	
    	if(email != null && !email.isEmpty()) {
    		User existringUserWithEmail = userDao.getUserByEmail(email);
    		if( existringUserWithEmail != null) {
    			return "{ \"status\": " + this.STATUS_ERROR +"}";
    		}
    	}
    	
    	if(phoneNumber != null && !phoneNumber.isEmpty()) {
    		User existringUserWithPhoneNumber = userDao.getUserByPhoneNumber(phoneNumber);
    		if( existringUserWithPhoneNumber != null) {
    			return "{ \"status\": " + this.STATUS_ERROR +"}";
    		}
    	}
    	
        int id = userDao.create (user);	
        return "{ \"uId\": " + id +"}";                 
    }
    
    @POST
    @Path("/editUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateUser(User user) {
        userDao.edit(user);		         
        return "{ \"status\": " + this.STATUS_OK +"}";                 
    }
    
    @POST
    @Path("/addFriend/{userId}/{friendId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addFriend(@PathParam("userId")int userId, @PathParam("friendId")int friendId) {
        User user = userDao.getUser(userId);
        User friend = userDao.getUser(friendId);
        
        if(friend == null || user == null)
        	return "{ \"status\": " + this.STATUS_NO_USER +"}";
        
        Set<User> userFriends = user.getFriends();
        
        userFriends.add(friend);
        user.setFriends(userFriends);
        
        userDao.edit(user);
        return "{ \"status\": " + this.STATUS_OK +"}";                 
    }
    
    @POST
    @Path("/updateUserGcmId/{userId}/{newGcmId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateGcmId(@PathParam("userId")int userId, @PathParam("newGcmId")String newGcmId) {
    	User user = userDao.getUser(userId);
    	if(user == null) {
    		return "{ \"status\": " + this.STATUS_NO_USER +"}";
    	}
    	
    	user.setGcmId(newGcmId);
    	userDao.edit(user);
    	return "{ \"status\": " + this.STATUS_OK +"}";
    }
    
    
}
