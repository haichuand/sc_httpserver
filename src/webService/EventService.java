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

import dao.EventDao;
import dao.impl.EventDaoImpl;
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

}
