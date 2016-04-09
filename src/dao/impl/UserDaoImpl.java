package dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.User;
import java.util.Set;
import dao.UserDao;


public class UserDaoImpl implements UserDao {
	private SessionFactory factory;
	
	public UserDaoImpl () {
		try{
            factory = new Configuration().configure().buildSessionFactory();
         }catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
         }
	}
	
	@Override
	public String create(User user) {
		Session session = factory.openSession();
        Transaction tx = null;
        String id = null;
        
        try{
        	tx = session.beginTransaction();
        	id = String.valueOf((Integer)session.save(user)); 
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
	public void edit(User user) {
		factory.getCurrentSession().update(user);
	}

	@Override
	public void delete(String uId) {
		factory.getCurrentSession().delete(uId);
	}

	@Override
	public User getUser(int uId) {
		User user = null;
		Session session = factory.openSession();
        Transaction tx = null;
        
        try{
        	tx = session.beginTransaction();
        	user = (User)session.get(User.class, uId);
        	tx.commit();
        } catch (HibernateException e) {
        	System.out.println("Error");
            if (tx!=null) tx.rollback();
            	e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return user;
	}

	@Override
	public Set getUserEvents(int uId) {
		return getUser(uId).getEvents();
	}
}
