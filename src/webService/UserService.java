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

    	System.out.println("client is request the info of user: " + userId);
    	return user;
    }
    
    @GET
    @Path("/userEvents/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Event> getEvensByUserId(@PathParam("userId")int userId) {
    	Set<Event> events = userDao.getUserEvents(userId);
    	System.out.println("client is request the info of user: " + userId);
    	return events;
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
    
    @POST
    @Path("/edituser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateUser(User user) {
        userDao.edit(user);		         
        return "Successfully updated!";                 
    }
}
