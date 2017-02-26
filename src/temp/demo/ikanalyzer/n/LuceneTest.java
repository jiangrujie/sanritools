package temp.demo.ikanalyzer.n;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class LuceneTest {
	
	LuceneMain luceneMain ;
	
	@Before
	public void setUp(){
		luceneMain = new LuceneMain();
	}
	
	@Test
	public void testIndex(){
		try {
			luceneMain.index();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch(){
		try {
			luceneMain.searcher();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInfo(){
		try {
			luceneMain.documentInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDelete(){
		try {
			luceneMain.deleteIndex();
			luceneMain.documentInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDelete2() throws IOException{
		luceneMain.deleteIndexByReader();
		luceneMain.documentInfo();
	}
	
	@Test
	public void testRecovery() throws IOException{
		luceneMain.recoveryIndex();
	}
	
	@Test
	public void testForceMergeDelete() throws CorruptIndexException, IOException{
		luceneMain.forceMergeDelete();
		luceneMain.documentInfo();
	}
}
