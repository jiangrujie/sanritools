package temp.learn.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author sanri
 * 功能说明:自定义搜索排序<br/>
 * 创建时间:2017-1-5下午4:15:50
 */
public class LuceneSort {
	LuceneQuery luceneQuery;
	private Version v = LuceneQuery.V;
	
	@Before
	public void init(){
		luceneQuery = new LuceneQuery();
	}
	
	@Test
	public void testSearch(){
		try {
			IndexSearcher searcher = luceneQuery.getSearcher();
			QueryParser queryParser = new QueryParser(v, "content", new StandardAnalyzer(v));
			//Sort.INDEXORDER 是以 id 进行排序,如果没有 id 选项呢 TODO ,设置排序之后就没有评分了
			Query query = queryParser.parse("like");
			System.out.println("-----------------不加 sort-----------------------");
			TopDocs topDocs = searcher.search(query, 10);
			luceneQuery.printDocs(topDocs, searcher);
			System.out.println("-------------------通过索引顺序排序---------------------");
			topDocs = searcher.search(query, 10,Sort.INDEXORDER);
			luceneQuery.printDocs(topDocs, searcher);
			System.out.println("--------------通过评分排序-------------------");
			topDocs = searcher.search(query, 10,Sort.RELEVANCE);
			luceneQuery.printDocs(topDocs, searcher);
			System.out.println("-------------------排序字段,根据附件数升序排序 ----------------------");
			topDocs = searcher.search(query, 10,new Sort(new SortField("attachs", SortField.INT)));
			luceneQuery.printDocs(topDocs, searcher);
			System.out.println("----------------------排序字段,根据附件数降序排序-----------------------------");
			topDocs = searcher.search(query, 10,new Sort(new SortField("attachs", SortField.INT,true)));
			luceneQuery.printDocs(topDocs, searcher);
			System.out.println("-----------以评分降序------------");
			topDocs = searcher.search(query, 10,new Sort(SortField.FIELD_SCORE));
			luceneQuery.printDocs(topDocs, searcher);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
