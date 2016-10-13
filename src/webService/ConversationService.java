package webService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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
import model.Conversation;
import model.ConversationAttendee;
import model.ConversationTitle;
import model.Event;
import model.Message;
import model.User;
import dao.ConversationDao;
import dao.UserDao;
import dao.impl.ConversationDaoImpl;
import dao.impl.UserDaoImpl;


@Path("/conversation")
public class ConversationService {
	
	private static ConversationDao conversationDao = new ConversationDaoImpl();
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
        return "Demo service for event is ready!";
    }
    
    @GET
    @Path("/getConversation/{conversationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Conversation getConversationById(@PathParam("conversationId")String convId) {
    	Conversation conversation = conversationDao.getConversation(convId);
    	
    	if(conversation == null) {
    		return null;
    	}
    	
    	Set<User> atts = conversation.getAttendees();
    	List attendeesId = new LinkedList<>();
    	
    	for(User att: atts) {
    		att.setPassword(null);
    		attendeesId.add(att.getuId());
    		att.setConvsations(new HashSet<Conversation>());
    		att.setEvents(new HashSet<Event>());
    	}
    	
    	conversation.setAttendeesId(attendeesId);
    	conversation.setAttendees(atts);
    	conversation.setMessages(null);
        return conversation;
    }
    
    @GET
    @Path("/conversationAttendeesId/{convId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getConversationAttendees(@PathParam("convId")String convId) {
    	Conversation conv = conversationDao.getConversation(convId);
    	
    	if(conv == null) 
    		return "{ \"status\": " + StatusCode.STATUS_NO_CONVERSATION +"}";
    	
    	List<User> attendees = new LinkedList<>(conversationDao.getAttendees(convId));
    	
    	if(attendees != null && !attendees.isEmpty()) {
    		String result = "{\"attendeesId\": [";
    		for(int i = 0; i < attendees.size(); i++) {
    			result += attendees.get(i).getuId();
    			if(i < attendees.size()-1)
    				result += ", ";
    		}
    		result += "]}";
    		return result;
    	}
    	
    	return "{ \"status\": " + StatusCode.STATUS_ERROR +"}";
    }
    
    @GET
    @Path("/conversationAttendeesGcmId/{convId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getConversationAttendeesGcmId(@PathParam("convId")String convId) {
    	Conversation conv = conversationDao.getConversation(convId);
    	
    	if(conv == null) 
    		return "{ \"status\": " + StatusCode.STATUS_NO_CONVERSATION +"}";
    	
    	List<User> attendees = new LinkedList<>(conversationDao.getAttendees(convId));
    	String result = "{\"attendeesGcmId\": [";
    	
    	if(!attendees.isEmpty()) {
    		for(int i = 0; i < attendees.size(); i++) {
    			User att = attendees.get(i);
    			result = result + "{\"uId\" : "+ att.getuId()+", "+"\"fcmId\": \""+ att.getFcmId()+"\"}";
    			if(i < attendees.size()-1)
    				result += ", ";
    		}
    		result += "]}";
    	}
    	return result;
    }    
    
    @GET
    @Path("/conversationMessages/{convId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getConversationMessages(@PathParam("convId")String convId) {
    	List<Message> messages = conversationDao.getMessages(convId);
    	return messages;
    }
    
    
    //Conversation data from the client source to create a new Conversation object, returned in JSON format.  
   	@POST
    @Path("/createConversation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createConversation(Conversation conv) {
   		
   		if(conversationDao.getConversation(conv.getcId()) != null)
   			return "{ \"status\": " + StatusCode.STATUS_ERROR +"}";
		
		List<Integer> attendeesId = conv.getAttendeesId();
		Set<User> attendees = new HashSet<>();
		Set<Conversation> convs = new HashSet<>();

		if (attendeesId != null && !attendeesId.isEmpty()) {
			for (int i = 0; i < attendeesId.size(); i++) {
				int uId = attendeesId.get(i);
				User userTemp = userDao.getUser(uId);
				if(userTemp == null)
					return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
				attendees.add(userTemp);
			}
		}
		conv.setAttendees(attendees);
   		conversationDao.create(conv);
   		String convId = conv.getcId();
   		
   		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
    }
   	
   	@POST
    @Path("/addMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addMessagetoConv(Message msg) {
   		conversationDao.addMessage(msg);
   		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
   	}
   	
   	@POST
    @Path("/backupMessages")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String syncConvMessages(List<Message> msgs) {
   		if(msgs != null) {
   			for(Message msg: msgs)
   				conversationDao.addMessage(msg);
   		}
   		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
   	}
   	
	@POST
    @Path("/editConversation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String updateConversation(Conversation conv) {
		
		List<Integer> attendeesId = conv.getAttendeesId();
		Set<User> attendees = new HashSet();

		if (attendeesId != null && !attendeesId.isEmpty()) {
			for (int i = 0; i < attendeesId.size(); i++) {
				int uId = attendeesId.get(i);
				User userTemp = userDao.getUser(uId);
				if(userTemp == null) {
					return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
				}
				attendees.add(userTemp);
			}
		}
		
		conv.setAttendees(attendees);
		conversationDao.edit(conv);
		
		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
	}
	
	@POST
	@Path("/updateTitle")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String updateConversationTitle(ConversationTitle convTitle) {
		String convId = convTitle.getcId();
		String title = convTitle.getTitle();
		
		Conversation conv = conversationDao.getConversation(convId);
		if(conv == null) 
			return "{ \"status\": " + StatusCode.STATUS_NO_CONVERSATION +"}";
		conv.setTitle(title);
		conversationDao.edit(conv);
		
		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
	}
	
	@POST
	@Path("/addAttendees")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String addConversationAttendee(ConversationAttendee convAttendee) {
		String convId = convAttendee.getcId();
		List<Integer> attendeesId = convAttendee.getAttendeesId();
		
		Conversation conv = conversationDao.getConversation(convId);
		if(conv == null) 
			return "{ \"status\": " + StatusCode.STATUS_NO_CONVERSATION +"}";
		
		List<User> attendeesAdd = new ArrayList();
		for(int attendeeId: attendeesId) {
			User attendee = userDao.getUser(attendeeId);
			if(attendee == null)
				return "{ \"status\": " + StatusCode.STATUS_NO_USER +"}";
			attendeesAdd.add(attendee);
		}
		
		Set<User> curAttendees = conv.getAttendees();
		for(User attendee: attendeesAdd)
			curAttendees.add(attendee);

		conv.setAttendees(curAttendees);

		conversationDao.edit(conv);
		
		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
	}
	
	@POST
	@Path("/dropAttendees")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String dropConversationAttendee(ConversationAttendee convAttendee) {
		String convId = convAttendee.getcId();
		List<Integer> attendeesId = convAttendee.getAttendeesId();
		
		Conversation conv = conversationDao.getConversation(convId);
		if(conv == null) 
			return "{ \"status\": " + StatusCode.STATUS_NO_CONVERSATION +"}";
		
		Set<User> curAttendees = conv.getAttendees();
		List<User> attendeesDrop = new ArrayList();
		boolean findAttendee = false;
		for(User attendee: curAttendees) {
			for(int attendeeId: attendeesId) {
				if(attendee.getuId() == attendeeId) {
					attendeesDrop.add(attendee);
					findAttendee = true;
				}
			}
		}
		if(!findAttendee)
			return "{ \"status\": " + StatusCode.STATUS_NO_USER + "}";
		for(User attendeeDrop: attendeesDrop)
			curAttendees.remove(attendeeDrop);
		
		conv.setAttendees(curAttendees);
		conversationDao.edit(conv);
		return "{ \"status\": " + StatusCode.STATUS_OK +"}";
	}
	
}
