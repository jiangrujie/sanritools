package temp.learn.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery;
import org.apache.lucene.search.function.ValueSourceQuery;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import sanri.utils.StringUtil;

/**
 * 
 * @author sanri
 * 功能说明:自定义评分<br/>
 * 1.重写 CustomScoreQuery 中的 getCustomScoreProvider 方法
 * 2.重写 CustomScoreProvider 中的 customScore 方法,确定新的评分
 * 创建时间:2017-1-6上午9:24:17
 */
public class LuceneScore {
	private LuceneQuery luceneQuery;
	private Version v = LuceneQuery.V;
	
	@Before
	public void init(){
		luceneQuery = new LuceneQuery();
	}
	
	@Test
	public void testDelete(){
		try {
			IndexWriter indexWriter = new IndexWriter(LuceneQuery.directory, new IndexWriterConfig(v, new StandardAnalyzer(v)));
			indexWriter.deleteDocuments(new Term("id", "3"));
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void searchByScore(){
		try {
			IndexSearcher searcher = luceneQuery.getSearcher();
			//查询字符串
			MatchAllDocsQuery query = new MatchAllDocsQuery();
			//创建一个评分域
			FieldScoreQuery scoreQuery = new FieldScoreQuery("score", FieldScoreQuery.Type.FLOAT);
			//根据评分域和原有的 query 创建自定义 query 对象
			MyCustomScoreQuery myCustomScoreQuery = new MyCustomScoreQuery(query, scoreQuery);
			TopDocs topDocs = searcher.search(myCustomScoreQuery, 10);
			luceneQuery.printDocs(topDocs, searcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author sanri
	 * 功能说明:自定义评分查询<br/>
	 * 创建时间:2017-1-6上午9:28:04
	 */
	private class MyCustomScoreQuery extends CustomScoreQuery{

		public MyCustomScoreQuery(Query subQuery, ValueSourceQuery valSrcQuery) {
			super(subQuery, valSrcQuery);
		}
		
		@Override
		protected CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
			return new MyCustomScoreProvider(reader);
		}
		
	}
	
	/**
	 * 
	 * @author sanri
	 * 功能说明:分数提供类<br/>
	 * 创建时间:2017-1-6上午9:27:51
	 */
	private class MyCustomScoreProvider extends CustomScoreProvider{
		private String [] names;
		public MyCustomScoreProvider(IndexReader reader) {
			super(reader);
			try {
				names = FieldCache.DEFAULT.getStrings(reader, "name");
				System.out.println(StringUtil.join(names,","));
				System.out.println(names.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * doc 是取的文档序号,当有文档删除时,names 缓存中的数据也会被清除,但占位符还在,所以下面的取法不会出问题
		 */
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
			String name = names[doc];
//			System.out.println("当前文档:"+name);
			if(name.startsWith("j")){
				return (float) Math.pow(valSrcScore, subQueryScore);
			}
			return subQueryScore/valSrcScore;
		}
	}
}
