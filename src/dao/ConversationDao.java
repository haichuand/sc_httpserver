package dao;

import model.Conversation;
import java.util.Set;
import java.util.List;

public interface ConversationDao {
  public void create (Conversation conv);
  public void edit (Conversation conv);
  public void delete (Conversation conv);
  public List getAttendees (String convId);
  public List getMessages (String convId);
  public List getMessages (String convId, long startTime, long endTime);
}
