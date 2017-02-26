package test.bean;

import java.util.List;

public class Student extends People {
	private String sno;
	private List<Course> courses;
	private Single [] singles;
	private Integer [] integers;
	private Course mainCourse;
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	public Single[] getSingles() {
		return singles;
	}
	public void setSingles(Single[] singles) {
		this.singles = singles;
	}
	public Integer[] getIntegers() {
		return integers;
	}
	public void setIntegers(Integer[] integers) {
		this.integers = integers;
	}
	public Course getMainCourse() {
		return mainCourse;
	}
	public void setMainCourse(Course mainCourse) {
		this.mainCourse = mainCourse;
	}
}
