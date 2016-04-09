package webService;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;

@Path("/user")
public class UserService {
	private static String U_ID = "uId";
	private static String MEDIA_ID = "mediaId";
	private static String GCM_ID= "gcmId";
	private static String EMAIL = "email";
	private static String PHONE_NUMBER = "phoneNumber";
	private static String FIRST_NAME = "firstName";
	private static String LAST_NAME = "lastName";
	private static String USER_NAME = "userName";
	private static Set events = new HashSet<>(0);
	
	private User user = new User();
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

    	System.out.println("client is request the info of user: " + userId);
        //User userNew = new User(2,"mediaID","googleId","fzudxj@gmail","123345","xuejing","dong","emma");
        //userDao.create(userNew);

    	user.setEvents(new HashSet());
    	return user;
    }
    
    @GET
    @Path("/userEventsInfo/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);

    	System.out.println("client is request the info of user: " + userId);
    	return user;
    }
    
    // Use data from the client source to create a new User object, returned in JSON format.  
    @POST
    @Path("/createuser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String postUser(User user) {
        int id = userDao.create (user);		         
        return String.valueOf(id);                 
    }
}
