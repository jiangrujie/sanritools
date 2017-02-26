package temp.demo.ikanalyzer.n;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jsqlparser.statement.select.Top;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * 
 * @author sanri
 * 功能说明:lucene 的搜索<br/>
 * 包含所有的查询,以及分页
 * MatchAllDocsQuery
 * TermQuery
 * TermRangeQuery
 * 	NumericRangeQuery
 * WildcardQuery
 * PrefixQuery
 * FuzzyQuery
 * PhraseQuery
 * BooleanQuery
 * 
 * QueryParser
 * 创建时间:2017-1-3上午9:15:49
 */
public class LuceneQuery {
	/**
	 * indexReader 必须是单例的,因为读取索引很耗费时间
	 */
	private static IndexReader indexReader;						
	private static Version V = Version.LUCENE_35;				//lucene 版本
	private static File indexDir;								//存放索引的目录
	private static Directory directory;
	static{
		try {
			indexDir  = new File("D:\\test\\index");
			directory = FSDirectory.open(indexDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public LuceneQuery(){}
	
	/**
	 * 
	 * 创建时间:2017-1-3上午9:40:57<br/>
	 * 功能:创建索引<br/>
	 * 作者: sanri<br/>
	 * @param create 是否重新创建
	 */
	public void createIndex(boolean create){
		loadData();
		try {
			IndexWriterConfig writerConfig = new IndexWriterConfig(V, new StandardAnalyzer(V));
			IndexWriter indexWriter = new IndexWriter(directory, writerConfig);
			if(create){
				//如果重新创建,则先删除所有索引,如果没有目录会不会报 FileNotFoundException ???
				indexWriter.deleteAll();
			}
			List<IndexData> loadData = loadData();
			for (IndexData indexData : loadData) {
				String id = indexData.getId();
				int attachs = indexData.getAttachs();
				String content = indexData.getContent();
				String email = indexData.getEmail();
				String name = indexData.getName();
				Date date = indexData.getDate();
				
				Document document = new Document();
				document.add(new Field("id", id, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("name",name,Field.Store.YES,Field.Index.NOT_ANALYZED));
				document.add(new Field("email",email,Field.Store.YES,Field.Index.NOT_ANALYZED));
				document.add(new Field("content",content,Field.Store.NO,Field.Index.ANALYZED));
				
				//设置为 true 是被索引的意思,不是被分词
				document.add(new NumericField("date",Field.Store.YES,true).setLongValue(date.getTime()));
				document.add(new NumericField("attachs", Field.Store.YES, true).setIntValue(attachs));
				
				indexWriter.addDocument(document);
			}
			indexWriter.commit();
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<IndexData> loadData() {
		List<IndexData> indexDatas = new ArrayList<IndexData>();
		try {
			String [] patterns  = new String []{"yyyy-MM-dd"};
			IndexData indexData = new IndexData();
			indexData.setId("1");
			indexData.setContent("welcome to visited the space,I like book");
			indexData.setAttachs(2);
			indexData.setDate(DateUtils.parseDate("2010-02-19", patterns));
			indexData.setEmail("aa@itat.org");
			indexData.setName("zhangsan");
			indexDatas.add(indexData);
			
			indexData = new IndexData();
			indexData.setId("2");
			indexData.setContent("hello boy, I like pingpeng ball");
			indexData.setAttachs(3);
			indexData.setDate(DateUtils.parseDate("2012-01-11", patterns));
			indexData.setEmail("bb@itat.org");
			indexData.setName("lisi");
			indexDatas.add(indexData);
			
			indexData = new IndexData();
			indexData.setId("3");
			indexData.setContent("my name is cc I like game");
			indexData.setAttachs(1);
			indexData.setDate(DateUtils.parseDate("2011-09-19", patterns));
			indexData.setEmail("cc@cc.org");
			indexData.setName("john");
			indexDatas.add(indexData);
			
			indexData = new IndexData();
			indexData.setId("4");
			indexData.setContent("I like football");
			indexData.setAttachs(4);
			indexData.setDate(DateUtils.parseDate("2010-12-22", patterns));
			indexData.setEmail("dd@sina.org");
			indexData.setName("jetty");
			indexDatas.add(indexData);
			
			indexData = new IndexData();
			indexData.setId("5");
			indexData.setContent("I like football and I like basketball too");
			indexData.setAttachs(5);
			indexData.setDate(DateUtils.parseDate("2012-01-01", patterns));
			indexData.setEmail("ee@zttc.edu");
			indexData.setName("mike");
			indexDatas.add(indexData);
			
			indexData = new IndexData();
			indexData.setId("6");
			indexData.setContent("I like movie and swim");
			indexData.setAttachs(5);
			indexData.setDate(DateUtils.parseDate("2011-05-19", patterns));
			indexData.setEmail("ff@itat.org");
			indexData.setName("jake");
			indexDatas.add(indexData);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return indexDatas;
	}

	/**
	 * 
	 * 创建时间:2017-1-3上午9:37:32<br/>
	 * 功能:获取 IndexReader<br/>
	 * 作者: sanri<br/>
	 * @return
	 */
	public synchronized IndexReader getReader(){
		try {
			if(indexReader == null){
				indexReader = IndexReader.open(directory);
				return indexReader;
			}
			//如果已经创建好了,但有可能索引已经被改变
			IndexReader openIfChanged = IndexReader.openIfChanged(indexReader);
			if(openIfChanged != null){
				indexReader.close();		//关闭原来的 indexReader
				indexReader = openIfChanged;
			}
			return indexReader;
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3上午9:38:40<br/>
	 * 功能:获取搜索器,用完需要关闭<br/>
	 * 作者: sanri<br/>
	 * @return
	 */
	public IndexSearcher getSearcher(){
		IndexSearcher indexSearcher = new IndexSearcher(getReader());
		return indexSearcher;
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午2:29:17<br/>
	 * 功能:精确查找<br/>
	 * 作者: sanri<br/>
	 */
	public void termQuery(){
		try {
			//对应查询字符串为 name:lishi
			IndexSearcher searcher = getSearcher();
			TermQuery termQuery = new TermQuery(new Term("name", "lisi"));
			TopDocs topDocs = searcher.search(termQuery, 10);
			printDocs(topDocs,searcher);
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void termRangeQuery(){
		TermRangeQuery termRangeQuery = new TermRangeQuery("name", "a", "s", true, true);
		searchAndShowResult(termRangeQuery);
		
		System.out.println("-----------------------------------------------");
		//数字域范围查询
		NumericRangeQuery<Integer> newIntRange = NumericRangeQuery.newIntRange("attachs", 2, 10, true, true);
		searchAndShowResult(newIntRange);
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午3:02:00<br/>
	 * 功能:通配符查询<br/>
	 * ? 代表一个
	 * *　代表多个
	 * 作者: sanri<br/>
	 */
	public void wildcardQuery(){
		try {
			//对应查询字符串为 email:*itat.org
			IndexSearcher searcher = getSearcher();
			WildcardQuery wildcardQuery = new WildcardQuery(new Term("email", "*itat.org"));
			TopDocs topDocs = searcher.search(wildcardQuery, 10);
			printDocs(topDocs, searcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午3:24:23<br/>
	 * 功能:查询所有<br/>
	 * 相当于查询 *.*
	 * 作者: sanri<br/>
	 */
	public void matchAllQuery(){
		try {
			// 对应的查询字符串为：*:*
			IndexSearcher searcher = getSearcher();
			MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
			TopDocs topDocs = searcher.search(matchAllDocsQuery, 10);
			printDocs(topDocs, searcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午4:32:24<br/>
	 * 功能:组合查询<br/>
	 * 作者: sanri<br/>
	 */
	public void booleanQuery(){
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(new TermQuery(new Term("name","zhangsan")), Occur.MUST);
		searchAndShowResult(booleanQuery);
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午3:28:32<br/>
	 * 功能:模糊查询<br/>
	 * 作者: sanri<br/>
	 */
	public void fuzzyQuery(){
		//默认相似度是 0.5f
		//相当于查询字符串name:make~0.5
		FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("name", "make"));
		searchAndShowResult(fuzzyQuery);
		System.out.println("--------------------------------------");
		// 第二个参数是最小相似度，表示有多少正确的就显示出来，比如0.9表示有90%正确的字符就会显示出来。
		FuzzyQuery fuzzyQuery2 = new FuzzyQuery(new Term("name","make"),0.9f);
		searchAndShowResult(fuzzyQuery2);
	}
	
	
	/**
	 * 
	 * 创建时间:2017-1-3下午3:56:32<br/>
	 * 功能:前缀查询<br/>
	 * 作者: sanri<br/>
	 * 搜索名称以 j 开头的人,需要不对这个字段进行分词
	 */
	public void prefixQuery(){
		PrefixQuery prefixQuery = new PrefixQuery(new Term("name", "j"));
		searchAndShowResult(prefixQuery);
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午4:12:17<br/>
	 * 功能:短语查询,可以间隔几个单词,这里是有顺序的<br/>
	 * 作者: sanri<br/>
	 */
	public void phraseQuery(){
		PhraseQuery phraseQuery = new PhraseQuery();
//		phraseQuery.add(new Term("content","pingpeng")); i like football
		//第一个Term
		phraseQuery.add(new Term("content","i"));
		//产生距离之后的第二个Term
		phraseQuery.add(new Term("content","football"));
		phraseQuery.setSlop(1);		//设置可以间隔 1 个单词
		
		searchAndShowResult(phraseQuery);
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午4:39:03<br/>
	 * 功能:通过解析来查询<br/>
	 * 作者: sanri<br/>
	 */
	public void parserQuery(){
		try {
			//创建QueryParser对象,默认搜索域为content
			QueryParser queryParser = new QueryParser(V, "content", new StandardAnalyzer(V));
			Query query = queryParser.parse("football"); //TermQuery
			searchAndShowResult(query);
			System.out.println("------------------1--------------");
			//更改空格的默认操作
//			queryParser.setDefaultOperator(Operator.AND); 改为同时成立才成立,但一般不会去改
			//开启第一个字符的通配符匹配操作
//			queryParser.setAllowLeadingWildcard(true);
			//有basketball或者football的，空格默认就是OR content:swim OR content:football
			query = queryParser.parse("swim football");
			searchAndShowResult(query);
			System.out.println("-----------------2-----------------");
			//boolean 查询,查询名字是 zhangsan 或者 content 中有 football 的 ,OR 要大写
			query = queryParser.parse("name:zhangsan OR football");
			searchAndShowResult(query);
			System.out.println("------------------3----------------");
			//更改搜索域
			query = queryParser.parse("name:john");
			searchAndShowResult(query);
			System.out.println("-------------------4--------------");
			//通配符查询
			query = queryParser.parse("name:j*");
			searchAndShowResult(query);
			System.out.println("-------------------5-------------");
			//匹配name中没有mike但是content中必须有like的，+和-要放置到域说明前面,要用空格分开
			query = queryParser.parse("- name:mike + like");
			searchAndShowResult(query);
			System.out.println("-----------------6----------------");
			//查询一个区间,查询 id 从 1 到 3 的,闭区间
			query = queryParser.parse("id:[1 TO 3]");
			searchAndShowResult(query);
			System.out.println("------------------7-----------------");
			//查询一个区间,开区间
			query = queryParser.parse("id:{1 TO 3}");
			searchAndShowResult(query);
			System.out.println("-----------------8------------------");
			//不分词匹配
			//完全匹配I Like Football的
			query = queryParser.parse("\"I like football\"");
			searchAndShowResult(query);
			System.out.println("------------------9-----------------");
			//短语查询
			query = queryParser.parse("\"I football\"~1");
			searchAndShowResult(query);
			System.out.println("-------------------10--------------------");
			//模糊查询
			query = queryParser.parse("name:make~");
			searchAndShowResult(query);
		} catch (org.apache.lucene.queryParser.ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 创建时间:2017-1-3下午3:35:30<br/>
	 * 功能:搜索  Query 并打印结果<br/>
	 * 作者: sanri<br/>
	 * @param query
	 */
	private void searchAndShowResult(Query query){
		try {
			IndexSearcher searcher = getSearcher();
			TopDocs topDocs = searcher.search(query, 10);
			printDocs(topDocs, searcher);
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 创建时间:2017-1-3下午2:29:27<br/>
	 * 功能:打印文档<br/>
	 * 作者: sanri<br/>
	 * @param topDocs
	 * @param searcher
	 */
	private void printDocs(TopDocs topDocs, IndexSearcher searcher) {
		System.out.println("总共查询出:"+topDocs.totalHits+" 条数据");
		try {
			for (ScoreDoc scoreDoc:topDocs.scoreDocs) {
				int docId = scoreDoc.doc;
				Document doc = searcher.doc(docId);
				System.out.println("id:"+doc.get("id")+",name:"+doc.get("name")+"" +
						",email:"+doc.get("email")+",attachs:"+doc.get("attachs")+"" +
								",date:"+DateFormatUtils.format(Long.parseLong(doc.get("date")),"yyyy-MM-dd"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 功能:再查询方式的分页<br/>
	 * 创建时间:2017-1-4下午7:56:06<br/>
	 * 作者：sanri<br/>
	 * @param page
	 * @param size<br/>
	 */
	public void searchPage(int page,int size){
		try {
			IndexSearcher searcher = getSearcher();
			MatchAllDocsQuery query = new MatchAllDocsQuery();
			TopDocs topDocs = searcher.search(query, 10);
			int start = (page-1) * size;
			int end = start + size;
			for(int i=start;i<end;i++){
				ScoreDoc scoreDoc = topDocs.scoreDocs[i];
				Document doc = searcher.doc(scoreDoc.doc);
				System.out.println("id:"+doc.get("id")+",name:"+doc.get(("name")));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 功能:取出最后一个元素<br/>
	 * 创建时间:2017-1-4下午8:18:15<br/>
	 * 作者：sanri<br/>
	 * @param pageIndex
	 * @param pageSize
	 * @param query
	 * @param searcher
	 * @return<br/>
	 */
	private ScoreDoc getLastScoreDoc(int pageIndex,int pageSize,Query query,IndexSearcher searcher ){
		try {
			if(pageIndex == 1){
				return null;
			}
			int count = (pageIndex - 1) * pageSize ;
			TopDocs topDocs = searcher.search(query, count);
			return topDocs.scoreDocs[count - 1];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 功能:通过 searchAfter() 来实现分页<br/>
	 * 创建时间:2017-1-4下午8:07:42<br/>
	 * 作者：sanri<br/><br/>
	 */
	public void searchByAfter(int page,int size){
		try {
			IndexSearcher searcher = getSearcher();
			MatchAllDocsQuery query = new MatchAllDocsQuery();
			ScoreDoc lastScoreDoc = getLastScoreDoc(page, size, query, searcher);
			TopDocs searchAfter = searcher.searchAfter(lastScoreDoc, query, size);
			for(int i=0;i<searchAfter.scoreDocs.length;i++){
				ScoreDoc scoreDoc = searchAfter.scoreDocs[i];
				Document doc = searcher.doc(scoreDoc.doc);
				System.out.println("id:"+doc.get("id")+",name:"+doc.get(("name")));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
