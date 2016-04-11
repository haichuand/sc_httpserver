package webService;

import java.util.HashSet;
import java.util.LinkedList;
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

import model.Conversation;
import model.Event;
import model.User;
import dao.ConversationDao;
import dao.UserDao;
import dao.impl.ConversationDaoImpl;
import dao.impl.UserDaoImpl;


@Path("/conversation")
public class ConversationService {
	private static ConversationDao conversationDao = new ConversationDaoImpl();
	
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
        return "Demo service for event is ready!";
    }
    
    @GET
    @Path("{conversationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Conversation getConversationById(@PathParam("conversationId")String conversationId) {
    	Conversation conversation = conversationDao.getConversation(conversationId);
    	System.out.println("client is request the info of user: " + conversationId);
    	//conversation.setAttendees(new HashSet<User>());
        return conversation;
    }
    
    @GET
    @Path("/conversationAttendees/{convId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getConversationAttendees(@PathParam("convId")String convId) {
    	Set<User> attendees = conversationDao.getAttendees(convId);
    	System.out.println("client is request the list of attendees in conversation: " + convId);
    	//conversation.setAttendees(new HashSet<User>());
        return new LinkedList<>(attendees);
    }
    
    // Event data from the client source to create a new Event object, returned in JSON format.  
   	@POST
    @Path("/createConversation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createConversation(Conversation conv) {
   		UserDao userDao = new UserDaoImpl();
		
		List<Integer> attendeesId = conv.getAttendeesId();
		Set<User> attendees = new HashSet();
		Set<Conversation> convs = new HashSet<>();

		if (attendeesId != null && !attendeesId.isEmpty()) {
			for (int i = 0; i < attendeesId.size(); i++) {
				int uId = attendeesId.get(i);
				User userTemp = userDao.getUser(uId);
				attendees.add(userTemp);
			}
		}
		conv.setAttendees(attendees);
   		conversationDao.create(conv);
   		String convId = conv.getcId();
   		
   		return "conversation is successfully saved on server: "+convId;
    }
}
