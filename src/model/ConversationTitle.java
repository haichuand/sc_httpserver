package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConversationTitle {
	private String cId;
	private String title;
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
