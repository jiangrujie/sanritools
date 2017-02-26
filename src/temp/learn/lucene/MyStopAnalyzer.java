package temp.learn.lucene;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

/**
 * 
 * 创建时间:2017-1-4下午10:14:28<br/>
 * 创建者:sanri<br/>
 * 功能:我的停止词分词器,加入自定义停止词<br/>
 */
public class MyStopAnalyzer extends Analyzer {
	private final static String [] stopWords = new String []{"的","是","了","也"};
	private Set<Object> MY_STOP_WORDS_SET;
	private Version V = Version.LUCENE_35;
	
	public MyStopAnalyzer(){
		Set<?> englishStopWordsSet = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
		MY_STOP_WORDS_SET = StopFilter.makeStopSet(V, stopWords, true);
		MY_STOP_WORDS_SET.addAll(englishStopWordsSet);
	}

	@Override
	public TokenStream tokenStream(String filed, Reader reader) {
		return new StopFilter(V, new LowerCaseFilter(V, new LetterTokenizer(V, reader)), MY_STOP_WORDS_SET);
	}
	
}
