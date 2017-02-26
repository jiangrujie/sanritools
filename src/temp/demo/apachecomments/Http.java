package temp.demo.apachecomments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class Http {
	public static void get(){
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("127.0.0.1", 8080, "http");
		GetMethod getMethod = new GetMethod("/Wifi/interface.do?getAllLabelPrisoner");
		
		try {
			int statusCode = client.executeMethod(getMethod);
			System.out.println(statusCode);
			System.out.println(statusCode == HttpStatus.SC_OK);
			Header[] responseHeaders = getMethod.getResponseHeaders();
			byte[] responseBody = getMethod.getResponseBody();
			if(responseHeaders != null && responseHeaders.length > 0){
				for (Header head : responseHeaders) {
					System.out.println(head.getName() + ":"+head.getValue());
				}
			}
			System.out.println();
			System.out.println("--------------------------------");
			System.out.println(new String(responseBody));
		} catch ( IOException e) {
			e.printStackTrace();
		}finally{
			getMethod.releaseConnection(); //释放连接 ??
		}
	}
	
	public static void queryPhone(){
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("shouji.supfree.net", 80, "http");
		
		PostMethod postMethod = new PostMethod("/fish.asp");
		NameValuePair nameValuePair = new NameValuePair("cat", "1511109");
		postMethod.setRequestBody(new NameValuePair [] {nameValuePair});
		
		//设置浏览器特征
		postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		
		try {
			int statusCode = client.executeMethod(postMethod);
			System.out.println(statusCode);
			System.out.println(postMethod.getStatusText());
			System.out.println(postMethod.getStatusLine());

			byte[] responseBody = postMethod.getResponseBody();
			//get 请求乱码解决
			System.out.println(new String(responseBody,Charset.forName("gbk")));
		} catch ( IOException e) {
			e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
		
	}
	
	/**
	 * 
	 * 作者:sanri<br/>
	 * 时间:2016-9-13下午3:22:07<br/>
	 * 功能: 使用 httpClient 做文件上传</br/>
	 * @throws FileNotFoundException 
	 */
	public static void fileUpload() throws FileNotFoundException{
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("127.0.0.1", 8080, "http");
		
		//登录
		PostMethod postMethod2 =  new PostMethod("/Wifi/info/login.do?login");
		NameValuePair userName = new NameValuePair("userAccount", "admin");
		NameValuePair passwd = new NameValuePair("userPassword", "123456");
		postMethod2.setRequestBody(new NameValuePair []{userName,passwd});
		
		int status;
		try {
			status = client.executeMethod(postMethod2);
			System.out.println(status);
			System.out.println(postMethod2.getStatusLine());
			System.out.println(postMethod2.getResponseBodyAsString());
		} catch (HttpException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			postMethod2.releaseConnection();
		}
		
		//设置最大连接超时时间 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		//上传文件
		PostMethod postMethod = new PostMethod("/Wifi/info/police.do?batchimport");
		Part part = new FilePart("filename", new File("C:\\Users\\sanri\\Documents\\Downloads\\police.xlsx"));
		MultipartRequestEntity requestEntity = new MultipartRequestEntity(new Part[]{part}, postMethod.getParams());
		postMethod.setRequestEntity(requestEntity);
		try {
			int executeMethod = client.executeMethod(postMethod);
			System.out.println(executeMethod);
			System.out.println(postMethod.getStatusLine());
			byte[] responseBody = postMethod.getResponseBody();
			System.out.println(new String(responseBody,"utf-8"));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
		
	}
	
	
	public static void main(String[] args) {
		try {
			fileUpload();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
