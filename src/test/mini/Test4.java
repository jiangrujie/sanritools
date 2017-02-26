package test.mini;

import java.io.UnsupportedEncodingException;

public class Test4 {
	public static void main(String[] args) {
//		System.out.println("方法返回值为:"+abc());
		try {
			System.out.println("中java".getBytes("GBK").length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static int abc() {
		int a = 3;
		try {
			System.out.println("执行语句");
//			int a = 1/0;
			System.out.println("无用语句");
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			a = 5;
			System.out.println("释放资源");
		}
		
		return 10;
	}
	
	
}
