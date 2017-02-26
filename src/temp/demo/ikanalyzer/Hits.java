package temp.demo.ikanalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

public class Hits {
	 public static void main(String []args) throws Exception { 
	        Directory directory = new RAMDirectory();  
	        Analyzer analyzer = new IKAnalyzer();  
	        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_34, analyzer);  
	        IndexWriter indexWriter = new IndexWriter(directory, iwc);  
	          
	        String str = "你好沈阳，这里是辽宁！欢迎光临！";  
	        Document doc = new Document();  
	        doc.add(new Field("contents",str,Field.Store.YES,Field.Index.ANALYZED));  
	        indexWriter.addDocument(doc);  
	          
	        str = "我是作者，在沈阳工作！现在还是一个普通的职员";  
	        doc = new Document();  
	        doc.add(new Field("contents",str,Field.Store.YES,Field.Index.ANALYZED));  
	        indexWriter.addDocument(doc);  
	        indexWriter.close();  
	          
	        IndexSearcher searcher = new IndexSearcher(directory);  
	        searcher.setSimilarity(new IKSimilarity());  
	        String keyWords = "沈阳";  
	        Query query = IKQueryParser.parse("contents",keyWords);  
	        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);  
	          
	        System.out.println(topDocs.totalHits);  
	        
	    }  
  
}
