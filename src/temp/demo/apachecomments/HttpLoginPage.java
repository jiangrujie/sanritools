package temp.demo.apachecomments;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpLoginPage {
	
	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("127.0.0.1", 8080, "http");
		
		PostMethod postMethod =  new PostMethod("/Wifi/info/login.do?login");
		NameValuePair userName = new NameValuePair("userAccount", "admin");
		NameValuePair passwd = new NameValuePair("userPassword", "123456");
		postMethod.setRequestBody(new NameValuePair []{userName,passwd});
		
		String jSessionId = "";
		
		try {
			int status = client.executeMethod(postMethod);
			System.out.println(status);
			System.out.println(postMethod.getStatusLine());
			System.out.println(postMethod.getResponseBodyAsString());
			
			//查看 cookie 信息
//			CookieSpec cookieSpec = CookiePolicy.getDefaultSpec();
//			Cookie[] cookies = cookieSpec.match("127.0.0.1", 8080, "/Wifi/", false, client.getState().getCookies());
//			if(cookies.length == 0){
//				System.out.println("none");
//			}else{
//				for (Cookie cookie : cookies) {
//					System.out.println(BeanUtils.describe(cookie));
//					if("JSESSIONID".equalsIgnoreCase(cookie.getName())){
//						System.out.println(cookie.getValue());
//						jSessionId = cookie.getValue();
//					}
//				}
//			}
//			Cookie[] cookies2 = client.getState().getCookies();
//			for (Cookie cookie : cookies2) {
//				System.out.println(BeanUtils.describe(cookie));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
		
		GetMethod getMethod = new GetMethod("/Wifi/info/prisoner.do?list");
		HttpMethodParams params = getMethod.getParams();
		params.setParameter("prisonerName", "");
		params.setParameter("pageSize", "10");
		params.setParameter("currentPage", "1");
		params.setParameter("totalRows", "162");
		params.setParameter("totalPages", "17");
		
		//client 可能已经记住登录 sessionId 所以不需要带上 sessionId 了
		
//		getMethod.setRequestHeader("Cookie", "JSESSIONID="+jSessionId+"; testcookie=yes; username=admin; checked=1");
//		getMethod.setRequestHeader("JSESSIONID", jSessionId);
		try {
			int statusCode = client.executeMethod(getMethod);
			System.out.println(statusCode);
			System.out.println(getMethod.getStatusLine());
			System.out.println(getMethod.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			getMethod.releaseConnection();
		}
		
	}
}
