package dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Conversation;
import java.util.Set;
import java.util.List;
import dao.UserDao;

public class ConversationDaoImp implements ConversationDao {
	private SessionFactory factory;
  
  public ConversationDaoImp () {
      try{
            factory = new Configuration().configure().buildSessionFactory();
         }catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
         }
	}

  @Override
  public void create (Conversation conv) {
      Session session = factory.openSession();
        Transaction tx = null;
        try{
        	tx = session.beginTransaction();
        	session.save(conv); 
        	tx.commit();
        } catch (HibernateException e) {
        	System.out.println("Error");
            if (tx!=null) tx.rollback();
            	e.printStackTrace(); 
        } finally {
            session.close(); 
        }
  }

  private Conversation getConversation (String convId) {
      Conversation conv = null;
      Session session = factory.openSession();
        Transaction tx = null;
        try{
        	tx = session.beginTransaction();
        	conv = (Conversation)session.get(Conversation.class, convId); 
        	tx.commit();
        } catch (HibernateException e) {
        	System.out.println("Error");
            if (tx!=null) tx.rollback();
            	e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return conv;
  }

  @Override
  public void edit (Conversation conv) {
      Session session = factory.openSession();
        Transaction tx = null;
        try{
        	tx = session.beginTransaction();
        	session.update(conv); 
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
  public void delete (Conversation conv) {
		Session session = factory.openSession();
        Transaction tx = null;
        
        try{
        	tx = session.beginTransaction();
        	session.delete(conv.getcId());
        	tx.commit();
        } catch (HibernateException e) {
        	System.out.println("Error");
            if (tx!=null) tx.rollback();
            	e.printStackTrace(); 
        } finally {
            session.close(); 
        }
  }

  @Overall
  public List getAttendees (String convId) {
        return getConversation(convId).getAttendees();
  }

  @Override
  public List getMessages (String convId) {
    return getConversation(convId).getMessages();
  }

  @Override
  public List getMessages (String convId, long startTime, long endTime) {
    List msgs = null;
		Session session = factory.openSession();
        Transaction tx = null;
        
        try{
        	tx = session.beginTransaction();

          String hql = "FROM message M WHERE M.timestamp >= :start_time AND M.timestamp <= :end_time";
          Query query = session.createQuery(hql);
          query.setParameter ("start_time", startTime);
          query.setParameter ("end_time", endTime);
          msgs = query.list();
        	tx.commit();
        } catch (HibernateException e) {
        	System.out.println("Error");
            if (tx!=null) tx.rollback();
            	e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return msgs;
  }
	
}
