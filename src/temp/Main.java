package temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 从网上扒新闻信息
 * 主要想取里面的正则
 */
public class Main {

	//显示新闻列表的地址
	private static final String http_url = "http://www.sina.cn";
	//找到需要扒的信息模块的ID
	private static final String summaryBlock = "id=\"blist\"";
	//显示的信息以什么HTML标签结束
	private static final String endSummaryBlock = "</table>";
	//存储网页中的链接标签
	public static List<String> list = new LinkedList<String>();

	public static void main(String[] args) {
		//想要抓取信息的页面
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//通过字符串得到URL对象
			URL url = new URL(http_url);
			//远程连接，得到URLConnection对象(它代表应用程序和 URL 之间的通信链接)
			URLConnection conn = url.openConnection();
			int find_flag = 0;		//表示没有找到需要的内容
			//从连接中读取数据流，
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = reader.readLine()) != null){
				//找到了需要下载链接模块
				if(line.indexOf(summaryBlock)!= -1){
					find_flag = 1;//表示找到了需要的内容
				}
				//需要新闻模块的结束标记
				if(line.indexOf(endSummaryBlock) != -1){
					find_flag = 2;//表示需要找的内容结束了
				}
				//将找到的信息放入stringBuffer中
				if(1 == find_flag){
					stringBuffer.append(line);
				}
				//需要找的信息已经结束
				if(2 == find_flag){
					System.out.println("over");
					find_flag = 0;
				}
			}
			
//			System.out.println(stringBuffer);
			//使用正则表达式获取想要的字符串
			Pattern pattern = Pattern.compile("[0-9]{5}\\.htm");
			Matcher matcher = pattern.matcher(stringBuffer);
			System.out.println(matcher.find());
			while(matcher.find()) {
				//将连接的地址存储到list容器中
				list.add("显示网页内容的网址" + matcher.group());
				//下面显示匹配的内容
//				System.out.println(matcher.group());
			}
			//读取具体链接信息内容
			readNews(list);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/*
	 * 读显示新闻的网页
	 */
	public static void readNews(List<String> list){
		String flagName = "news";
		for(int i = 0; i < list.size(); i++){
			//得到的是每篇文章的链接地址 具体网页的地址
			String temp = list.get(i);
			String filename = "";
			filename = flagName + i+".txt";
			//将下载的网页信息保存到文件中
			getNewsContent(temp,filename);
		}
	}
	
	/*
	 * 将显示新闻的网页的内容存放在本地文件中
	 */
	public static void getNewsContent(String httpLink,String fileName){
		try {
			System.out.println("getNewsContent : " + httpLink);
			//通过URL产生链接到具体的网页，然后读取数据
			URL url = new URL(httpLink);
			URLConnection conn = url.openConnection();
			//这里读取的网页内容一定要注意后面的编码，跟网页的报纸一致，否则在后面存储在文件中的也为乱码
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String tempStr;
			//根据显示具体网页个格式，找到对应的模块，然后读取出来存储在文件中
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			String class_name = "class=\"content2";
			String end_content = "</div>";
			int readContentFlag = 0;
			StringBuffer strbuf = new StringBuffer();
			while((tempStr = reader.readLine()) != null){
				if(tempStr.indexOf(class_name)!= -1){
					readContentFlag = 1;
				}
				if(tempStr.indexOf(end_content)!= -1){
					readContentFlag = 2;
				}
				if(1 == readContentFlag){
					strbuf.append(tempStr);
//					System.out.println(line);
				}
				if(2 == readContentFlag){
					System.out.println("over");
					readContentFlag = 0;
				}
				tempStr = strbuf.toString();
				System.out.println("tempStr.indexOf(class_name)2: "+ tempStr.indexOf(class_name));
				tempStr = delHTMLTag(tempStr);
				tempStr = stripHtml(tempStr);
				fos.write(tempStr.getBytes("utf-8"));
			}
			//一定不要忘记了关闭数据流，否则出现异常情况
			fos.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public static String delHTMLTag(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    } 

	public static String stripHtml(String content) { 
		// <p>段落替换为换行 
		content = content.replaceAll("<p .*?>", "\r\n"); 
		// <br><br/>替换为换行 
		content = content.replaceAll("<br\\s*/?>", "\r\n"); 
		// 去掉其它的<>之间的东西 
		content = content.replaceAll("\\<.*?>", ""); 
		// 还原HTML 
		// content = HTMLDecoder.decode(content); 
		content = content.replaceAll("&nbsp;", "");
		return content; 
	} 


}