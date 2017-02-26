package temp.demo.apachecomments;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import test.bean.Student;

public class BeanUtil {
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Student student = new Student();
		student.setAge(15);student.setIdCard("43242");
		
		Map describe = BeanUtils.describe(student); //常用,描述一个 bean
		System.out.println(describe);
	}
}
