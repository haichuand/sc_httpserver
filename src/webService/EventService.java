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

import util.UniqueIdGenerator;
import dao.EventDao;
import dao.UserDao;
import dao.impl.EventDaoImpl;
import dao.impl.UserDaoImpl;
import model.Conversation;
import model.Event;
import model.User;


@Path("/event")
public class EventService {
	
	private static EventDao eventDao = new EventDaoImpl();
	
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
    @Path("{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Event getEventById(@PathParam("eventId")String eventId) {
    	Event event = eventDao.getEvent(eventId);
    	System.out.println("client is request the info of user: " + eventId);
    	Set<User> atts = event.getAttendees();
    	List attendeesId = new LinkedList<>();
    	for(User att: atts) {
    		att.setPassword(null);
    		attendeesId.add(att.getuId());
    		att.setConvsations(new HashSet<Conversation>());
    		att.setEvents(new HashSet<Event>());
    	}
    	event.setAttendeesId(attendeesId);
    	event.setAttendees(atts);
        return event;
    }
    
    @GET
    @Path("/eventAttendeesGcmId/{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEventAttendeesGcmId(@PathParam("eventId")String eventId) {
    	System.out.println("client is request the list of attendees in event: " + eventId);
    	List<User> attendees = new LinkedList<>(eventDao.getEventAttendees(eventId));
    	String result = "{\"attendeesGcmId\": [";
    	if(!attendees.isEmpty()) {
    		for(int i = 0; i < attendees.size(); i++) {
    			User att = attendees.get(i);
    			result = result + "{\"uId\" : "+ att.getuId()+", "+"\"gcmId\": \""+ att.getGcmId()+"\"}";
    			if(i < attendees.size()-1)
    				result += ", ";
    		}
    		result += "]}";
    	}
    	return result;
    }    
    
    // Event data from the client source to create a new Event object, returned in JSON format.  
	@POST
    @Path("/createEvent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postEvent(Event event) {
		UserDao userDao = new UserDaoImpl();
		String id = UniqueIdGenerator.generateId(Event.class.getSimpleName(),
				event.getCreatorId());
		event.setEventId(id);
		
		List<Integer> attendeesId = event.getAttendeesId();
		Set<User> attendeeSet = new HashSet<User>();
		
		if (attendeesId != null && !attendeesId.isEmpty()) {
			for (int i = 0; i < attendeesId.size(); i++) {
				int uId = attendeesId.get(i);
				User userTemp = userDao.getUser(uId);
				attendeeSet.add(userTemp);
			}
		}
		event.setAttendees(attendeeSet);
		eventDao.create(event);
    	return "{ \"eventId\": " +"\"" + event.getEventId() +"\"" +"}";
    }
    
    @POST
    @Path("/editEvent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editEvent(Event event) {
    	UserDao userDao = new UserDaoImpl();
    	
    	List<Integer> attendeesId = event.getAttendeesId();
		Set<User> attendeeSet = new HashSet<>();

		if (attendeesId != null && !attendeesId.isEmpty()) {
			for (int i = 0; i < attendeesId.size(); i++) {
				int uId = attendeesId.get(i);
				User userTemp = userDao.getUser(uId);
				attendeeSet.add(userTemp);
			}
		}
		event.setAttendees(attendeeSet);
		
    	eventDao.edit(event);
    	return "{ \"eventId\": " +"\"" + event.getEventId() +"\"" +"}";
    }
    
	@DELETE
	@Path("/deleteEvent/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteEvent(@PathParam("eventId")String eventId) {
		eventDao.delete(eventId);
		return "Delete Event Successfully";
	}
	
	
}
