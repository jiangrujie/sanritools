package temp.demo.ikanalyzer.n;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * 
 * @author sanri
 * 功能说明:lucene 操作<br/>
 * 创建时间:2016-12-30上午9:32:30
 */
public class LuceneMain {
	
	final static Version V = Version.LUCENE_34;
	private File indexDir = new File("d:/test/lucene/index1");
	/**
	 * 
	 * 创建时间:2016-12-30上午9:32:44<br/>
	 * 功能:创建索引 <br/>
	 * 作者: sanri<br/>
	 * @throws IOException 
	 */
	public void index() throws IOException{
		//创建目录
		Directory directory = FSDirectory.open(indexDir);
		//创建 indexWriter
		IndexWriterConfig writerConfig = new IndexWriterConfig(V, new StandardAnalyzer(V));
		IndexWriter indexWriter = new IndexWriter(directory, writerConfig);
		File indexFilesDir = new File("d:\\test\\lucene\\files");
		int index = 1;
		for(File file:indexFilesDir.listFiles()){
			//创建 document 并写入 
			String absolutePath = file.getAbsolutePath();
			String name = file.getName();
			Document document = new Document();
			//document 中写入 field
			document.add(new Field("id",String.valueOf(index),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			Field contentField = new Field("content", new FileReader(file));
//			contentField.setBoost(boost);
			document.add(contentField);
			document.add(new Field("name", name, Field.Store.YES, Field.Index.ANALYZED));
			document.add(new Field("path", absolutePath, Field.Store.YES, Field.Index.NOT_ANALYZED));
			indexWriter.addDocument(document);
			
			index ++;
		}
		//关闭 indexWriter
		indexWriter.close();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30上午9:50:43<br/>
	 * 功能:搜索索引<br/>
	 * 作者: sanri<br/>
	 * @throws ParseException 
	 */
	public void searcher() throws IOException, ParseException{
		//获取索引目录
		Directory directory = FSDirectory.open(indexDir);
		//创建 IndexReader
		IndexReader indexReader = IndexReader.open(directory);
		//根据 indexReader 创建 indexSearch
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//使用 QueryParser 来解析查询关键字得到 Query 对象
		QueryParser queryParser = new QueryParser(V, "content", new StandardAnalyzer(V));
		//用 indexSearch 来 search Query 对象得到 TopDocs 对象
		Query query = queryParser.parse("exception");
		TopDocs search = indexSearcher.search(query, 10);		//查询 10 条记录
		System.out.println("命中次数:"+search.totalHits);
		//取得 TopDocs 中的 ScoreDoc 对象
		for (ScoreDoc scoreDoc : search.scoreDocs) {
			//根据 ScoreDoc 对象获取文档Id ,用 indexSearch 获取到 Document 对象
			int doc = scoreDoc.doc;
			Document document = indexSearcher.doc(doc);
			System.out.println(document.get("path"));
		}
		//关闭 indexReader
		indexSearcher.close();
		indexReader.close();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30上午10:54:08<br/>
	 * 功能:获取文档信息<br/>
	 * 作者: sanri<br/>
	 * @throws IOException
	 */
	public void documentInfo() throws IOException{
		//获取索引目录
		Directory directory = FSDirectory.open(indexDir);
		//创建 IndexReader
		IndexReader indexReader = IndexReader.open(directory);
		int maxDoc = indexReader.maxDoc();
		int numDocs = indexReader.numDocs();
		int numDeletedDocs = indexReader.numDeletedDocs();
		System.out.println("文档数(numDocs):"+numDocs);
		System.out.println("删除文档数(numDeletedDocs):"+numDeletedDocs);
		System.out.println("最大文档数(maxDoc):"+maxDoc);
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30上午10:42:20<br/>
	 * 功能:删除索引 ,删除以文档为单位<br/>
	 * 这个删除有问题,好像网上说要用 indexReader 来删除 ,但测试如果用纯英文字段来删除的话,两种删除方式都是可以的
	 * http://jfzhang.blog.51cto.com/1934093/413854
	 * 作者: sanri<br/>
	 * @throws IOException 
	 */
	public void deleteIndex() throws IOException{
		//使用 indexWriter 来删除索引 ,以文档为单位
		//获取索引目录
		Directory directory = FSDirectory.open(indexDir);
		IndexWriterConfig writerConfig = new IndexWriterConfig(V, new StandardAnalyzer(V));
		IndexWriter indexWriter = new IndexWriter(directory, writerConfig);
//		indexWriter.deleteAll();		//删除所有文档 
//		indexWriter.deleteDocuments(query 对象)
		indexWriter.deleteDocuments(new Term("id", "2"));
		indexWriter.close();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30上午11:03:21<br/>
	 * 功能:删除索引,这为什么要有?
	 *  <br/>
	 * 作者: sanri<br/>
	 * @throws IOException
	 */
	public void deleteIndexByReader() throws IOException{
		//获取索引目录
		Directory directory = FSDirectory.open(indexDir);
		IndexReader indexReader = IndexReader.open(directory,false);	//不以 readonly 打开
		int deleteDocuments = indexReader.deleteDocuments(new Term("id", "1"));
		System.out.println("删除条数:"+deleteDocuments);
		indexReader.close();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30上午11:15:41<br/>
	 * 功能:恢复索引 ,使用 IndexReader 来恢复<br/>
	 * 作者: sanri<br/>
	 * @throws IOException 
	 */
	public void recoveryIndex() throws IOException{
		Directory directory = FSDirectory.open(indexDir);
		IndexReader indexReader = IndexReader.open(directory,false);	//不以 readonly 打开
		indexReader.undeleteAll();			//只能全部恢复
		indexReader.close();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30上午11:21:33<br/>
	 * 功能:清空回收站,把删除索引生成的 .del 文件删除<br/>
	 * 好像这个方法不起作用,还把文件增多了 
	 * 3.5 以后叫 forceMergeDelete
	 * 作者: sanri<br/>
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 */
	public void forceMergeDelete() throws CorruptIndexException, IOException{
		Directory directory = FSDirectory.open(indexDir);
		IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(V, new StandardAnalyzer(V)));
		indexWriter.forceMergeDeletes();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30下午1:16:56<br/>
	 * 功能:强制将索引合并为两段,这两段中被删除的数据会被清空;不建议使用,因为要重建索引 <br/>
	 * 作者: sanri<br/>
	 * @throws IOException
	 */
	public void forceMerge() throws IOException{
		Directory directory = FSDirectory.open(indexDir);
		IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(V, new StandardAnalyzer(V)));
		indexWriter.forceMerge(2);
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30下午1:18:59<br/>
	 * 功能:更新索引,lucene 并不提供更新,其实是先删除后增加 <br/>
	 * 作者: sanri<br/>
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 */
	public void updateIndex() throws CorruptIndexException, LockObtainFailedException, IOException{
		Directory directory = FSDirectory.open(indexDir);
		IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(V, new StandardAnalyzer(V)));
		Document document = new Document();
		document.add(new Field("title","这标题",Field.Store.YES,Field.Index.ANALYZED));
		indexWriter.updateDocument(new Term("id", "1"), document);
//		indexWriter.commit();			如果不关闭 writer 需要提交
		indexWriter.close();
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30下午1:30:00<br/>
	 * 功能:加权<br/>
	 * 作者: sanri<br/>
	 */
	public void boost(){
		Document document = new Document();
		document.setBoost(2F);		//默认是 1.0 数字越大,权限越高
	}
	
	/**
	 * 
	 * 创建时间:2016-12-30下午1:44:00<br/>
	 * 功能:为日期和数字加索引 <br/>
	 * 作者: sanri<br/>
	 */
	public void numdateIndex(){
		Document document = new Document();
		Fieldable field = new Field("title",new byte[1024]);	//实现类 1
		field = new NumericField("age").setDoubleValue(2);	//添加数字域的键值对
		//存储日期也是存储日期的 long 类型的值
		document.getBoost();		//这个在查询的时候没有把原来的权值设置进来
		ScoreDoc scoreDoc = new ScoreDoc(1, 1.5f);
		float score = scoreDoc.score;
	}

	/**
	 * indexReader 打开非常消耗时间
	 * indexsearch 可以多次创建,每次用完也是要关闭的 
	 * 不关闭 indexWriter 可使用 commit() 方法
	 */
	
	IndexReader indexReader = null;
	public void getSearch () throws Exception{
		if(indexReader == null){
			indexReader = IndexReader.open(FSDirectory.open(indexDir));
		}else{
			IndexReader reader = IndexReader.openIfChanged(indexReader);
			if(reader != null){
				//关闭原来的 reader
				indexReader.close();
				indexReader = null;
				indexReader = reader;
			}
		}
	}
}
