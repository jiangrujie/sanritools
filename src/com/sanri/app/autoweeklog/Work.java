package com.sanri.app.autoweeklog;

/**
 * 创建时间:2016-10-4下午2:47:28<br/>
 * 创建者:sanri<br/>
 * 功能:周报内容实体<br/>
 */
public class Work {
	private int no;
	private String time;
	private String content;
	private String status;
	private String remark;
	
	public Work(){}
	public Work(int no,String time,String content){
		this.no = no;
		this.time = time;
		this.content = content;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
}
