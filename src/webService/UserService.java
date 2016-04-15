package webService;

import java.util.HashSet;
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
    	if(user != null)
    		gcmId = user.getGcmId();
    	return "{\"gcmId\": \"" +gcmId + "\"}" ; 
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
    	System.out.println("client is request the friend list of user: " + userId);
    	return friends;
    }
    
    // Use data from the client source to create a new User object, returned in JSON format.  
    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String postUser(User user) {
        int id = userDao.create (user);		         
        return "{\"uId\": "+ String.valueOf(id) + "}" ;                 
    }
    
    @POST
    @Path("/editUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateUser(User user) {
        userDao.edit(user);		         
        return "{\"uId\": "+ String.valueOf(user.getuId()) + "}" ;                 
    }
    
    @POST
    @Path("/addFriend/{userId}/{friendId}")
    public String addFriend(@PathParam("userId")int userId, @PathParam("friendId")int friendId) {
        User user = userDao.getUser(userId);
        User friend = userDao.getUser(friendId);
        
        Set<User> userFriends = user.getFriends();
        Set<User> friendFriends = friend.getFriends();
        
        userFriends.add(friend);
        friendFriends.add(user);
        user.setFriends(userFriends);
        friend.setFriends(friendFriends);
        
        userDao.edit(user);
        userDao.edit(friend);
        return "{\"friendId\": "+ friendId + "}" ;                 
    }
    
    @POST
    @Path("/verifyUserByEmail/{email}/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public String verifyUserByEmail(@PathParam("email")String email, @PathParam("password")String password) {
    	String noUserFound =  "{\"verify result\": \"No user found!\"}" ;
    	return noUserFound;
    }
    
}
