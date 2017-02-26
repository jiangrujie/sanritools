package temp.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * 创建时间:2016-12-18上午8:17:28<br/>
 * 创建者:sanri<br/>
 * 功能:主要实现爬虫功能,从百度知道上获取数据<br/>
 */
public class JSoupDemo {
	Log logger = LogFactory.getLog(Jsoup.class);
	public static void main(String[] args) {
		try {
			JSoupDemo jSoupDemo = new JSoupDemo();
			jSoupDemo.baiduzhidao();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	final static String ZHIDAO_BASE_URL = "https://zhidao.baidu.com/list?cid=110106";
	final static List<String> ZHIDAO_TAGS = new ArrayList<String>();
	static{
		ZHIDAO_TAGS.add("java");
		ZHIDAO_TAGS.add("javaweb");
		ZHIDAO_TAGS.add("jdk");
		ZHIDAO_TAGS.add("js");
		ZHIDAO_TAGS.add("jsp");
		ZHIDAO_TAGS.add("Jquery");
	}
	/**
	 * 
	 * 功能:从百度知道上获取问答数据<br/>
	 * 创建时间:2016-12-18上午8:18:36<br/>
	 * 作者：sanri<br/><br/>
	 * @throws IOException 
	 */
	public void baiduzhidao() throws IOException{
		for (String tag : ZHIDAO_TAGS) {
			int pn = 0;
			handlerTag(tag,pn);
		}
	}
	/**
	 * 
	 * 功能:处理单个标签<br/>
	 * 创建时间:2016-12-18上午8:52:15<br/>
	 * 作者：sanri<br/>
	 * @param requestUrl
	 * @throws IOException<br/>
	 */
	private void handlerTag(String tag,int pn) throws IOException {
		String requestUrl = ZHIDAO_BASE_URL+"&tag="+tag;
		//"&rn=30&pn="+pn+"&_pjax=%23j-question-list-pjax-container"
		Document document = Jsoup.connect(requestUrl).
				userAgent("Mozilla").
				timeout(3000).get();
		//获取当前页问题列表
		Elements questionLists = document.getElementsByClass("question-list-content");
		if(questionLists != null && questionLists.size() > 0){
			Element questionList = questionLists.get(0);
			Elements questionTitles = questionList.getElementsByClass("question-title");
			if(questionTitles != null && questionTitles.size() > 0){
				ListIterator<Element> listIterator = questionTitles.listIterator();
				while(listIterator.hasNext()){
					Element questionTitle = listIterator.next();
					Elements linkUrl = questionTitle.getElementsByTag("a");
					if(linkUrl != null && linkUrl.size() > 0){
						Element titleUrlEl = linkUrl.get(0);
//							System.out.println(titleUrl.text());
//							System.out.println(titleUrl.attr("href"));
//							System.out.println(titleUrl.baseUri());			//这个是当前的基础路径
						String href = titleUrlEl.attr("href");
						readQuestion(href);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 功能:读取当前问题和答案及提问者,回答者信息<br/>
	 * 创建时间:2016-12-18上午9:29:26<br/>
	 * 作者：sanri<br/>
	 * @param href
	 * @throws IOException<br/>
	 */
	private void readQuestion(String href) throws IOException {
		Document document = Jsoup.connect(href).
				userAgent("Mozilla").
				timeout(3000).get();
		parseAsk(document);
		parseAnswer(document);
	}
	/**
	 * 
	 * 功能:解析提问答案<br/>
	 * 创建时间:2016-12-18上午10:02:14<br/>
	 * 作者：sanri<br/>
	 * @param document<br/>
	 */
	private void parseAnswer(Document document) {
		Element answers = document.getElementById("wgt-answers");
		if(answers == null){
			return ;
		}
		Elements elementsByClass = answers.getElementsByClass("answer");
		Iterator<Element> iterator = elementsByClass.iterator();
		while(iterator.hasNext()){
			Element answerDiv = iterator.next();
			Elements answerContentEl = answerDiv.getElementsByAttributeValue("accuse", "aContent");
			String answerContent = answerContentEl.text();
			System.out.println("回答内容:"+answerContent);
		}
	}
	/**
	 * 
	 * 功能:解析提问信息<br/>
	 * 创建时间:2016-12-18上午9:38:28<br/>
	 * 作者：sanri<br/>
	 * @param document<br/>
	 */
	private void parseAsk(Document document) {
		Element ask = document.getElementById("wgt-ask");
		Elements titles = ask.getElementsByClass("ask-title");
		String questionTitle = titles.get(0).text();
		Elements questionContents = ask.getElementsByAttributeValue("accuse", "qContent");
		if(questionContents != null && questionContents.size() > 0){
			Element content = questionContents.get(0);
			String questionContent = content.html();
			System.out.println("提问标题:"+questionTitle);
			System.out.println("提问内容:"+questionContent);
		}
		//获取提问标签
		Elements itemTags = ask.getElementsByClass("question-list-item-tag");
		List<String> tagsList = new ArrayList<String>();
		if(itemTags != null && itemTags.size() > 0){
			Element itemTagsEl = itemTags.get(0);
			Elements tags = itemTagsEl.getElementsByTag("a");
			ListIterator<Element> listIterator = tags.listIterator();
			while(listIterator.hasNext()){
				Element tag = listIterator.next();
				tagsList.add(tag.text());
			}
		}
		System.out.println("问题标签:"+tagsList);
	}
}
