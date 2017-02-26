package temp.learn.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author sanri
 * 功能说明:过滤<br/>
 * 
 * 创建时间:2017-1-5下午4:55:16
 */
public class LuceneFilter {
	
	LuceneQuery luceneQuery = null;
	private Version v = LuceneQuery.V;
	@Before
	public void init(){
		luceneQuery = new LuceneQuery();
	}
	
	public TopDocs filter(IndexSearcher searcher,Filter filter,Sort sort){
		try {
			QueryParser queryParser = new QueryParser(v, "content", new StandardAnalyzer(v));
			Query query = queryParser.parse("like");
			TopDocs search = searcher.search(query, filter, 10);
			return search;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 创建时间:2017-1-5下午5:00:04<br/>
	 * 功能:范围内的过滤<br/>
	 * 作者: sanri<br/>
	 */
	@Test
	public void termRangeFilter(){
		IndexSearcher searcher = luceneQuery.getSearcher();
		TermRangeFilter termRangeFilter = new TermRangeFilter("name", "a", "s", true, true);
		TopDocs topDocs = filter(searcher,termRangeFilter,null);
		luceneQuery.printDocs(topDocs, searcher);
		
		NumericRangeFilter<Integer> numericRangeFilter = NumericRangeFilter.newIntRange("attachs", 2, 10, true, true);
		TopDocs filter = filter(searcher, numericRangeFilter, null);
		luceneQuery.printDocs(filter, searcher);
	}
	
	/**
	 * 
	 * 创建时间:2017-1-5下午5:48:40<br/>
	 * 功能:通过 query 来过滤<br/>
	 * 作者: sanri<br/>
	 */
	@Test
	public void testqueryWrapFilter(){
		try {
			IndexSearcher searcher = luceneQuery.getSearcher();
			QueryParser queryParser = new QueryParser(v, "content", new StandardAnalyzer(v));
			Query query = queryParser.parse("name:zhangsan");
			QueryWrapperFilter queryWrapperFilter = new QueryWrapperFilter(query);
			TopDocs filter = filter(searcher, queryWrapperFilter, null);
			luceneQuery.printDocs(filter, searcher);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
