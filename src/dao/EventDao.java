package dao;

import java.util.List;
import java.util.Set;

import model.Event;

public interface EventDao {
	public void create(Event event);
	public void edit(Event event);
	public void delete(String eventId);
	public Event getEvent(String eventId);
	public Set getEventAttendees(String eventId);
}
