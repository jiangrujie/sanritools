package temp.demo.ikanalyzer.n;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

public class QueryIndex {
	public static void main(String[] args) {
		File storeFile = new File("E:\\resources\\projectupload");
		Directory directory;
		try {
			directory = new SimpleFSDirectory(storeFile);
			IndexSearcher indexSearcher = new IndexSearcher(directory);
			indexSearcher.setSimilarity(new IKSimilarity());
			String searchWord = "索引目录";
			
//	        Query query = IKQueryParser.parse("title",searchWord);  
//	        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);  
//	        System.out.println(topDocs.totalHits);
			
//			Term term = new Term("title", searchWord);
//			Query query = new TermQuery(term);
//			TopDocs search = indexSearcher.search(query, Integer.MAX_VALUE);
//	        System.out.println(search.totalHits);
			
//			Analyzer analyzer = new IKAnalyzer(true);
//			QueryParser queryParser = new QueryParser(Version.LUCENE_34, "title", analyzer);
//			Query query = queryParser.parse(searchWord);
//			TopDocs search = indexSearcher.search(query,Integer.MAX_VALUE);
//			System.out.println(search.totalHits);
//			ScoreDoc[] scoreDocs = search.scoreDocs;
//			for (ScoreDoc scoreDoc : scoreDocs) {
//				Document doc = indexSearcher.doc(scoreDoc.doc);
//				String string = doc.get("title");
//				System.out.println(string);
//			}
			
			Analyzer analyzer = new IKAnalyzer(true);
			QueryParser queryParser = new QueryParser(Version.LUCENE_34, "content", analyzer);
			Query query = queryParser.parse(searchWord);
			TopDocs search = indexSearcher.search(query,Integer.MAX_VALUE);
			int totalHits = search.totalHits;
			System.out.println(totalHits);
			ScoreDoc[] scoreDocs = search.scoreDocs;
			for (ScoreDoc scoreDoc : scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				String title = doc.get("title");
				String content = doc.get("content");
				System.out.println("title:="+title);
				System.out.println("content:="+content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}
