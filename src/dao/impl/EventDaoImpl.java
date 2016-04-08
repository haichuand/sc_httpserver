package dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import util.UniqueIdGenerator;
import model.Event;
import dao.EventDao;


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
	public String create(Event event, int creatorId) {
		Session session = factory.openSession();
        Transaction tx = null;
        String id = UniqueIdGenerator.generateId(Event.class.getSimpleName(), creatorId);
        event.setEventId(id);
        
        try{
        	tx = session.beginTransaction();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Event getEvent(String eventId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getEventAttendees(String eventId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getUserEvents(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
