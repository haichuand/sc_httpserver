/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anu
 * To get messages in jsonObject format
 */
@XmlRootElement
public class MessagesList {
     public List<Message> messages;
    
     public MessagesList(List<Message> messages)
     {
         this.messages = messages;
     }
     

}
