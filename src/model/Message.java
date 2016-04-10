package model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="message",schema="public")
public class Message {
	
    private MessagePrimaryKey messageKey;
	private String mediaId;
	private String textContent;
	
	@EmbeddedId
	public MessagePrimaryKey getMessageKey() {
		return messageKey;
	}
	
	public void setMessageKey(MessagePrimaryKey messageKey) {
		this.messageKey = messageKey;
	}
	
	@Column(name = "media_id")
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@Column(name = "text_content")
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
}
