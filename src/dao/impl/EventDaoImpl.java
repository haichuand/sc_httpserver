package dao.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import util.UniqueIdGenerator;
import model.Event;
import model.User;
import dao.EventDao;
import dao.UserDao;



public class EventDaoImpl implements EventDao {
	
private SessionFactory factory;
	
	public EventDaoImpl () {
		try{
            factory = new Configuration().configure().buildSessionFactory();
         }catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
         }
	}

	@Override
	public String create(Event event, List attendeeIds) {
		UserDao userDao= new UserDaoImpl();
		Session session = factory.openSession();
        Transaction tx = null;
        String id = UniqueIdGenerator.generateId(Event.class.getSimpleName(), event.getCreatorId());
        event.setEventId(id);
        
        List<User> attendees = new LinkedList();
        Set<Event> eventSet = new HashSet<Event>();
        Set<User> attendeeSet;

        try{
        	tx = session.beginTransaction();
        	eventSet.add(event);
        	
        	if(attendeeIds != null && !attendeeIds.isEmpty()) {
        		for(int i = 0; i< attendeeIds.size(); i++) {
        			int uId = (Integer) attendeeIds.get(i);
        			User userTemp = userDao.getUser(uId);
        			userTemp.setEvents(eventSet);
        			attendees.add(userTemp);
        		}
        	}
        	attendeeSet = new HashSet(attendees);
        	event.setAttendees(attendeeSet);
        	
        	id = String.valueOf((Integer)session.save(event)); 
        	tx.commit();
        } catch (HibernateException e) {
        	System.out.println("Error");
            if (tx!=null) tx.rollback();
            	e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        
        return id;
	}

	@Override
	public void edit(Event event) {
		Session session = factory.openSession();
    Transaction tx = null;
    try{
      tx = session.beginTransaction();
      String hql = "UPDATE event set" + 
            "even_type = : ev_type" + 
            "title = : ev_title" + 
            "location = : ev_location" + 
            "start_time = : ev_start" + 
            "end_time = : ev_end" + 
            "creator_id = : ev_creator" + 
            "create_time = : ev_ctime" + 
            "WHERE event_id = : ev_id";

      Query query = session.createQuery(hql);
      query.setParameter("ev_id", event.getEventId());
      query.setParameter("ev_type", event.getEventType());
      query.setParameter("ev_title", event.getEventType());
      query.setParameter("ev_location", event.getTitle());
      query.setParameter("ev_start", event.getStartTime());
      query.setParameter("ev_end", event.getEndTime());
      query.setParameter("ev_creator", event.getCreatorId());
      query.setParameter("ev_ctime", event.getCreateTime());

      int result = query.executeUpdate();
      System.out.println("Update finished. Rows affected: " + result);

      tx.commit();
    } catch (HibernateException e) {
      System.out.println("Error");
        if (tx!=null) tx.rollback();
          e.printStackTrace(); 
    } finally {
        session.close(); 
    }
	}

	@Override
	public void delete(String eventId) {
		Session session = factory.openSession();
    Transaction tx = null;
    try{
      tx = session.beginTransaction();
      String hql = "DELETE FROM event " + 
            "WHERE event_id = : ev_id";

      Query query = session.createQuery(hql);
      query.setParameter("ev_id", eventId);

      int result = query.executeUpdate();
      System.out.println("Delete finished. Rows affected: " + result);

      tx.commit();
    } catch (HibernateException e) {
      System.out.println("Error");
        if (tx!=null) tx.rollback();
          e.printStackTrace(); 
    } finally {
        session.close(); 
    }
	}

	@Override
	public Event getEvent(String eventId) {
		Session session = factory.openSession();
    List result = null;
    Transaction tx = null;
    try{
      tx = session.beginTransaction();
      String hql = "FROM event E " + 
            "WHERE E.event_id = : ev_id";

      Query query = session.createQuery(hql);
      query.setParameter("ev_id", eventId);

      result = query.list();
      System.out.println("GetEvent for " + eventId);

      tx.commit();
    } catch (HibernateException e) {
      System.out.println("Error");
        if (tx!=null) tx.rollback();
          e.printStackTrace(); 
    } finally {
        session.close(); 
    }
		return result.get(0);
	}

	@Override
	public Set getEventAttendees(String eventId) {
    getEvent (eventId).getEventAttendees();
	}

}
