package temp.demo.ikanalyzer.n;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Index {
	public static void main(String[] args) {
		File storeDir = new File("E:\\resources\\projectupload");
		Directory director;
		try {
			director = new SimpleFSDirectory(storeDir);
			Analyzer analyzer = new IKAnalyzer(true);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34, analyzer);
			IndexWriter writer = new IndexWriter(director, config);
			Document doc = new Document();
			doc.add(new Field("title", "并指定存放索引的目录为“/data/index", Store.YES, org.apache.lucene.document.Field.Index.ANALYZED));
			doc.add(new Field("content","这里很长一段文字,里面包含索引的目录,这个设置为有索引但不存储,这样可以被索引 ?",Store.NO,org.apache.lucene.document.Field.Index.ANALYZED));
			writer.addDocument(doc);
			writer.optimize();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
