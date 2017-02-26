package temp.demo.apachecomments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import test.bean.Student;

public class Coll {
	static List<String> strlist1 = new ArrayList<String>();
	static List<String> strlist2 = new ArrayList<String>();
	static List<String> strlistAll = new ArrayList<String>();
	
	static List<Student> students1 = new ArrayList<Student>();
	static List<Student> students2 = new ArrayList<Student>();
	
	public static void main(String[] args) {
		loadData();
		//这里的 集合运算最终取决于 hashmap 的 get 方法,hashmap 的 get 方法只取决于 对象的 hashcode ,所以当是同一对象是才会相等,实现 equals 都不管用
		System.out.println("并集");
		Collection union = CollectionUtils.union(strlist1, strlist2);
		System.out.println(union);
		Collection union2 = CollectionUtils.union(students1, students2);
		System.out.println(union2);
		
		System.out.println("交集");
		Collection intersection = CollectionUtils.intersection(strlist2, strlist1);
		System.out.println(intersection);
		Collection intersection2 = CollectionUtils.intersection(students1, students2);
		System.out.println(intersection2);
		
		System.out.println("差集,使用的 equals");
		Collection subtract = CollectionUtils.subtract(strlist1, strlist2);
		System.out.println(subtract);
		
		System.out.println("补集");
		Collection disjunction = CollectionUtils.disjunction(strlist1, strlistAll);
		System.out.println(disjunction);
	}
	
	static void loadData(){
		strlist1.add("1");
		strlist1.add("2");
		strlist1.add("3");
		strlist1.add("4");
		
		strlist2.add("2");
		strlist2.add("3");
		strlist2.add("4");
		strlist2.add("5");
		
		strlistAll.add("1");
		strlistAll.add("2");
		strlistAll.add("3");
		strlistAll.add("4");
		strlistAll.add("5");
		strlistAll.add("6");
		strlistAll.add("7");
		strlistAll.add("8");
		strlistAll.add("9");
		
		
		Student student = new Student();
		student.setAge(5);
		students1.add(student);
		
		student = new Student();
		student.setAge(6);
		students1.add(student);
		students2.add(student);
		
		Student student2 = new Student();
		student2.setAge(5);
		students2.add(student2);
	}
}
