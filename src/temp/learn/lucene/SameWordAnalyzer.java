package temp.learn.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKTokenizer;

/**
 * 
 * @author sanri
 * 功能说明:同义词分词器<br/>
 * 创建时间:2017-1-5下午2:07:00
 */
public class SameWordAnalyzer extends Analyzer {

	private Version V = Version.LUCENE_35;
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new MySameWordFilter(new LowerCaseFilter(V, new IKTokenizer(reader, true)));
	}
	
}
