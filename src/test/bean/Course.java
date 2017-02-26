package test.bean;

import java.util.List;

public class Course {
	private String sno;
	private String name;
	private List<Course> prevs;
	private People teacherPeople;
	
	public Course(){}
	public Course(String sno,String name){
		this.sno = sno;
		this.name = name;
	}
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Course> getPrevs() {
		return prevs;
	}
	public void setPrevs(List<Course> prevs) {
		this.prevs = prevs;
	}
	public People getTeacherPeople() {
		return teacherPeople;
	}
	public void setTeacherPeople(People teacherPeople) {
		this.teacherPeople = teacherPeople;
	}
	
}
