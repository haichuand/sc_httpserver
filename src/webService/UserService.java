package webService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;

import util.StatusCode;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.Conversation;
import model.EmailPassword;
import model.Event;
import model.PhoneNumberPassword;
import model.User;
import model.UserBasic;
import model.UserFriends;

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
    public String getUserBasicInfoById(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);
    	if(user != null)
    		return user.toJsonString();
    	else 
    		return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
    }
    
    @GET
    @Path("/getUserByEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserByEmail(@PathParam("email")String email) {
    	User user = userDao.getUserByEmail(email);
    	if(user != null)
    		return user.toJsonString();
    	else 
    		return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
    }
    
    @GET
    @Path("/getUserByPhoneNumber/{phoneNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserByPhoneNumber(@PathParam("phoneNumber")String phoneNumber) {
    	User user = userDao.getUserByPhoneNumber(phoneNumber);
    	if(user != null)
    		return user.toJsonString();
    	else 
    		return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
    }
    
    @GET
    @Path("/getUserFcmId/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserFcmId(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);
    	String fcmId = null;
    	if(user != null) {
    		fcmId = user.getFcmId();
    		return "{\"fcmId\": \"" +fcmId + "\"}" ; 
    	}
    	
    	return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
    }
    
    @GET
    @Path("/getAllUserIdAndFcmId")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUserFcmId() {
    	List<User> users = userDao.getAllUser();
    	String result = "{\"usersFcmId\": [";
    	if(!users.isEmpty()) {
    		for(int i = 0; i < users.size(); i++) {
    			User user = users.get(i);
    			result = result + "{\"uId\" : "+ user.getuId()+", "+"\"fcmId\": \""+ user.getFcmId()+"\"}";
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
    	User user = userDao.getUser(userId);
    	if(user == null)
    		return new HashSet<>();
    	
    	Set<Event> events = userDao.getUserEvents(userId);
    	for(Event event: events) {
    		event.setConversation(null);
    		List<Integer> attendeesIds = new ArrayList<>();
    		Set<User> attendeesList = event.getAttendees();
    		for(User attendee: attendeesList) {
    			attendee.setPassword(null);
    			attendeesIds.add(attendee.getuId());
    		}
    		event.setAttendeesId(attendeesIds);
    	}
    	return events;
    }
    
    @GET
    @Path("/userConversations/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Conversation> getConversationsByUserId(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);
    	
    	if(user == null)
    		return new HashSet<>();
    	
    	Set<Conversation> conversations = userDao.getUserConversations(userId);
    	for(Conversation conv: conversations) {
    		List<Integer> attendeesIds = new ArrayList<>();
    		Set<User> attendeesList = conv.getAttendees();
    		for(User attendee: attendeesList) {
    			attendee.setPassword(null);
    			attendeesIds.add(attendee.getuId());
    		}
    		conv.setAttendeesId(attendeesIds);
    	}
    	return conversations;
    }
    
    @GET
    @Path("/userFriendList/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<User> getFriendsByUserId(@PathParam("userId")int userId) {
    	if(userDao.getUser(userId) == null)
    		return new HashSet<>();
    	
    	Set<User> friends = userDao.getUser(userId).getFriends();
    	for(User friend: friends) 
    		friend.setPassword(null);
    	return friends;
    }
    
    @POST
    @Path("/verifyUserByEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String verifyUserByEmail(EmailPassword emailPassword) {
    	if(emailPassword != null) {
    		if(emailPassword.getEmail() != null) {
    			User user = userDao.getUserByEmail(emailPassword.getEmail());
    			if(user == null)
    	    		return "{\"status\": "+ StatusCode.STATUS_NO_USER + "}";
    			else if (!user.getPassword().equals(emailPassword.getPassword()))
    	    		return "{\"status\": "+ StatusCode.STATUS_WRONG_PASSWORD + "}";
    	    	else
    	    		return user.toJsonString();
    		}
    	}
    	return "{\"status\": "+ StatusCode.STATUS_ERROR + "}";
	
    }
    
    @POST
    @Path("/verifyUserByPhoneNumber")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String verifyUserByPhoneNumber(PhoneNumberPassword phonePassword) {
    	if(phonePassword != null) {
    		if(phonePassword.getPhoneNumber() != null) {
    			User user = userDao.getUserByPhoneNumber(phonePassword.getPhoneNumber());
    			if(user == null)
    	    		return "{\"status\": "+ StatusCode.STATUS_NO_USER + "}";
    			else if (!user.getPassword().equals(phonePassword.getPassword()))
    	    		return "{\"status\": "+ StatusCode.STATUS_WRONG_PASSWORD + "}";
    	    	else
    	    		return user.toJsonString();
    		}
    	}
    	return "{\"status\": "+ StatusCode.STATUS_ERROR + "}";
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
    			return "{ \"status\": " + StatusCode.STATUS_ERROR +"}";
    		}
    	}
    	
    	if(phoneNumber != null && !phoneNumber.isEmpty()) {
    		User existringUserWithPhoneNumber = userDao.getUserByPhoneNumber(phoneNumber);
    		if( existringUserWithPhoneNumber != null) {
    			return "{ \"status\": " + StatusCode.STATUS_ERROR +"}";
    		}
    	}
    	
        int id = userDao.create (user);	
        return "{ \"uId\": " + id +"}";                 
    }
    
    @POST
    @Path("/editUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUser(UserBasic userBasic) {
    	User user = userDao.getUser(userBasic.getuId());
    	if(user != null) {
    		user.setEmail(userBasic.getEmail());
    		user.setFcmId(userBasic.getFcmId());
    		user.setMediaId(userBasic.getFcmId());
    		user.setPhoneNumber(userBasic.getPhoneNumber());
    		user.setFirstName(userBasic.getFirstName());
    		user.setLastName(userBasic.getLastName());
    		user.setUserName(userBasic.getUserName());
    		user.setPassword(userBasic.getPassword());
    		userDao.edit(user);	
    		return "{ \"status\": " + StatusCode.STATUS_OK +"}"; 
    	}
    	else 
    		return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}"; 
                        
    }
    
    @POST
    @Path("/addFriends")
    @Produces(MediaType.APPLICATION_JSON)
    public String addFriend(UserFriends ufs) {
        User user = userDao.getUser(ufs.getuId());
        if(user == null)
        	return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
        
        List<Integer> friendsId = ufs.getFriendsId();
        Set<User> userFriends = user.getFriends();
        
        for(int id: friendsId) {
        	User friend = userDao.getUser(id);
        	if(friend == null)
            	return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
        	userFriends.add(friend);
        }
        
        user.setFriends(userFriends);
        userDao.edit(user);
        return "{ \"status\": " + StatusCode.STATUS_OK +"}";                 
    }
    
    @POST
    @Path("/updateUserFcmId/{userId}/{newFcmId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateFcmId(@PathParam("userId")int userId, @PathParam("newFcmId")String newFcmId) {
    	User user = userDao.getUser(userId);
    	if(user == null) {
    		return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
    	}
    	
    	user.setFcmId(newFcmId);
    	userDao.edit(user);
    	return "{ \"status\": " + StatusCode.STATUS_OK +"}";
    }
    
    
}
