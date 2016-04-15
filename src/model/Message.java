package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlRootElement
@Entity
@Table(name="message",schema="public")
public class Message {
	
    private MessagePrimaryKey messageKey;
	private String mediaId;
	private String textContent;
	private Conversation conversation;

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
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name="c_id",insertable=false, updatable=false)
	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
}
