package temp.demo.ikanalyzer.n;

import org.junit.Before;
import org.junit.Test;

public class QueryTest {
	private LuceneQuery luceneQuery;
	
	@Before
	public void init(){
		luceneQuery = new LuceneQuery();
	}
	
	@Test
	public void testCreateIndex(){
		luceneQuery.createIndex(true);
	}
	
	@Test
	public void testTermQuery(){
		luceneQuery.termQuery();
	}
	
	@Test
	public void testTermRangeQuery(){
		luceneQuery.termRangeQuery();
	}
	
	
	@Test
	public void testWildcardQuery(){
		luceneQuery.wildcardQuery();
	}
	
	@Test
	public void testQueryAll(){
		luceneQuery.matchAllQuery();
	}
	
	@Test
	public void testFuzzyQuery(){
		luceneQuery.fuzzyQuery();
	}
	
	@Test
	public void testPrefixQuery(){
		luceneQuery.prefixQuery();
	}
	
	@Test
	public void testPhraseQuery(){
		luceneQuery.phraseQuery();
	}
	
	@Test
	public void testBooleanQuery(){
		luceneQuery.booleanQuery();
	}
	
	@Test
	public void testParserQuery(){
		luceneQuery.parserQuery();
	}
	
	@Test
	public void testSearchPage(){
		luceneQuery.searchPage(2, 2);
	}
}
