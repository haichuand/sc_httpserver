package webService;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;

import dao.EventDao;
import dao.impl.EventDaoImpl;
import model.Event;
import model.User;


@Path("/event")
public class EventService {
	private static String EVENT_ID = "event_id";
	private static String EVENT_TYPE = "event_type";
	private static String TITLE = "title";
	private static String LOCATION = "location";
	private static String START_TIME = "start_time";
	private static String END_TIME = "end_time";
	private static String CREATOR_ID = "creator_id";
	private static String CREATE_TIME = "create_time";
	private static String ATTENDEES = "attendees";
	
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
        return event;
    }
    
    // Use data from the client source to create a new User object, returned in JSON format.  
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postEvent(MultivaluedMap<String, Object> eventParams) {
    	Event event = new Event();
    	event.setEventId((String)eventParams.getFirst(EVENT_ID));
    	event.setEventType((String)eventParams.getFirst(EVENT_TYPE));
    	event.setTitle((String)eventParams.getFirst(TITLE));
    	event.setLocation((String)eventParams.getFirst(LOCATION));
    	event.setStartTime((long)eventParams.getFirst(START_TIME));
    	event.setEndTime((long)eventParams.getFirst(END_TIME));
    	event.setCreatorId((int)eventParams.getFirst(CREATOR_ID));
    	event.setCreateTime((long)eventParams.getFirst(CREATE_TIME));
    	
    	List attendeeIds = (List) eventParams.get(ATTENDEES);
    	
    	String eventId = eventDao.create(event, attendeeIds);
    	
    	return eventId;
    }

}
