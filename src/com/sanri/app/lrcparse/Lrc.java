package com.sanri.app.lrcparse;

public class Lrc  implements Comparable<Lrc>{
	private int time;
	private String content;
	
	public Lrc(){}
	public Lrc(int time,String content){
		this.time = time;
		this.content = content;
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public int compareTo(Lrc o) {
		return this.time - o.time;
	}
	@Override
	public String toString() {
		return "time:"+this.time+" | content:"+this.content;
	}
}
