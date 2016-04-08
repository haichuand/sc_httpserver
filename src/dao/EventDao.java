package dao;

import java.util.Set;

import model.Event;

public interface EventDao {
	public String create(Event event, int creatorId);
	public void edit(Event event);
	public void delete(String eventId);
	public Event getEvent(String eventId);
	public Set getEventAttendees(String eventId);
	public Set getUserEvents(String userId);
}
