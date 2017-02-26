package temp.demo.ikanalyzer.n;

import java.util.Date;

public class IndexData {
	private String id;
	private String name;
	private String email;
	private int attachs;
	private Date date;
	private String content;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAttachs() {
		return attachs;
	}
	public void setAttachs(int attachs) {
		this.attachs = attachs;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
