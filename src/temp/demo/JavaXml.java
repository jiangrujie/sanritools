package temp.demo;

import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.impl.PopulatorBuilder;

import java.util.ArrayList;
import java.util.List;

import test.bean.Course;
import test.bean.Student;

import com.thoughtworks.xstream.XStream;

public class JavaXml {
	public static void main(String[] args) {
		Populator populator = new PopulatorBuilder().build();
		List<Student> populateBeans = populator.populateBeans(Student.class, 10);
//		System.out.println(JSONObject.toJSONString(populateBeans));
		
		XStream xStream = new XStream();
		//别名
		xStream.alias("students", Student.class);
		//把子元素变成父元素的属性
//		xStream.("sno", Student.class);
		xStream.addImplicitCollection(String.class, "favorites");

		Student student = populateBeans.get(0);
		List<String> favorite = student.getFavorite();favorite = new ArrayList<String>();
		List<Course> courses = student.getCourses(); courses = new ArrayList<Course>();
		favorite.add("爱好1");
		favorite.add("爱好2");
		favorite.add("爱好3");
		courses.add(new Course("22323","sadfsadf"));
		String xml = xStream.toXML(student);
		System.out.println(xml);
		
//		XStream xStream =  new XStream(new DomDriver());
//		xStream.alias("Student", Student.class);
//		try {
//			String pkgPath = PathUtil.pkgPath("temp");
//			Object fromXML = xStream.fromXML(new FileReader(pkgPath+"/test.xml"));
//			System.out.println(fromXML);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}
}
