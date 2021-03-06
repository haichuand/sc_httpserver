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
import org.hibernate.Query;

import util.UniqueIdGenerator;
import model.Event;
import model.User;
import dao.EventDao;
import dao.UserDao;

public class EventDaoImpl implements EventDao {

	private SessionFactory factory;

	public EventDaoImpl() {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	@Override
	public void create(Event event) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(event);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println("Error");
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void edit(Event event) {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(event);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println("Error");
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(String eventId) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Event event = session.load(Event.class, eventId);
			if(event != null)
				session.delete(event);	
			tx.commit();
		} catch (HibernateException e) {
			System.out.println("Error");
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public Event getEvent(String eventId) {
		Session session = factory.openSession();
		Event event = null;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			event = (Event)session.get(Event.class, eventId);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println("Error");
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return event;
	}
        
        @Override
        public List<Event> getEventbyConvId(String convId)
        {
                Session session = factory.openSession();
		List<Event> event = null;
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
                        String hql = "FROM Event E WHERE E.conversation.cId = :convId";
			Query query = session.createQuery(hql);
                        query.setParameter("convId", convId);
                        event = query.list(); 
			tx.commit();
		} catch (HibernateException e) {
			System.out.println("Error");
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return event; 
        }

	@Override
	public Set getEventAttendees(String eventId) {
		return getEvent(eventId).getAttendees();
	}

}
