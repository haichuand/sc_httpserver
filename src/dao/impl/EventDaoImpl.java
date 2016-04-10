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
	public String create(Event event) {
		UserDao userDao = new UserDaoImpl();
		Session session = factory.openSession();
		Transaction tx = null;
		String id = UniqueIdGenerator.generateId(Event.class.getSimpleName(),
				event.getCreatorId());
		event.setEventId(id);
		
		List<Integer> attendeesId = event.getAttendeesId();
		List<User> attendees = new LinkedList();
		Set<Event> eventSet = new HashSet<Event>();
		Set<User> attendeeSet;

		try {
			tx = session.beginTransaction();
			eventSet.add(event);

			if (attendeesId != null && !attendeesId.isEmpty()) {
				for (int i = 0; i < attendeesId.size(); i++) {
					int uId = attendeesId.get(i);
					User userTemp = userDao.getUser(uId);
					userTemp.setEvents(eventSet);
					attendees.add(userTemp);
				}
			}
			attendeeSet = new HashSet(attendees);
			event.setAttendees(attendeeSet);

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

		return id;
	}

	@Override
	public void edit(Event event) {
		UserDao userDao = new UserDaoImpl();
		Session session = factory.openSession();
		Transaction tx = null;
		
		List<Integer> attendeesId = event.getAttendeesId();
		List<User> attendees = new LinkedList();
		Set<Event> eventSet = new HashSet<Event>();
		Set<User> attendeeSet;

		try {
			tx = session.beginTransaction();
			eventSet.add(event);

			if (attendeesId != null && !attendeesId.isEmpty()) {
				for (int i = 0; i < attendeesId.size(); i++) {
					int uId = attendeesId.get(i);
					User userTemp = userDao.getUser(uId);
					userTemp.setEvents(eventSet);
					attendees.add(userTemp);
				}
			}
			attendeeSet = new HashSet(attendees);
			event.setAttendees(attendeeSet);
			
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
		List<Integer> attendeesId = new LinkedList<>();
		Set<User> attendees = null;
		try {
			tx = session.beginTransaction();
			event = (Event)session.get(Event.class, eventId);
			System.out.println("GetEvent for " + eventId);
			attendees = event.getAttendees();
			

			for(User user: attendees) {
				attendeesId.add(user.getuId());
			}
			event.setAttendeesId(attendeesId);
			
			
			tx.commit();
			System.out.println("after commit");
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
