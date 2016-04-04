package webService;

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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;

@Path("/user")
public class UserService {
	private static String U_ID = "u_id";
	private static String MEDIA_ID = "mediaId";
	private static String GCM_ID= "gcmId";
	private static String EMAIL = "email";
	private static String PHONE_NUMBER = "phoneNumber";
	private static String FIRST_NAME = "firstName";
	private static String LAST_NAME = "lastName";
	private static String USER_NAME = "userName";
	
	private User user = new User();
	private static UserDAO userDao = new UserDAOImpl();
	
	// The @Context annotation allows us to have certain contextual objects
    // injected into this class.
    // UriInfo object allows us to get URI information (no kidding).
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
        return "Demo service is ready!";
    }
    
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("userId")int userId) {
    	User user = userDao.getUser(userId);
    	System.out.println("client is request the info of user: " + userId);
        //User userNew = new User(2,"mediaID","googleId","fzudxj@gmail","123345","xuejing","dong","emma");
        //userDao.create(userNew);
        return user;
    }
    
    // Use data from the client source to create a new User object, returned in JSON format.  
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User postUser(MultivaluedMap<String, String> userParams) {
    	String mediaId = userParams.getFirst(MEDIA_ID);
    	String gcmId = userParams.getFirst(GCM_ID);
    	String email = userParams.getFirst(EMAIL);
    	String phoneNumber = userParams.getFirst(PHONE_NUMBER);
        String firstName = userParams.getFirst(FIRST_NAME);
        String lastName = userParams.getFirst(LAST_NAME);
        String userName = userParams.getFirst(USER_NAME);
        //TODO:store the information to database
        System.out.println("Storing posted " + firstName + " " + lastName + "  " + email);
         
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
         				         
        return user;                 
    }
}
