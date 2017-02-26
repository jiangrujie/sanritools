package temp.learn.lucene;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.noneDSA;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * @author sanri
 * 功能说明:lucene 高亮<br/>
 * 创建时间:2017-1-6下午2:30:07
 * 	Fragmenter 文本分段
 * 	Highlighter
 */
public class LuceneHighlight {
	
	@Test
	public void light(){
		try {
			String text = "我爱北京天安门,天安门上红旗飘,伟大领袖毛泽东,指引我们向前去";
			TermQuery query = new TermQuery(new Term("f","毛1泽东"));
			QueryScorer queryScorer = new QueryScorer(query);
			
			Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
			Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
			Highlighter highlighter = new Highlighter(formatter,queryScorer);
			highlighter.setTextFragmenter(fragmenter);
			//这里是获取 最好的分段信息,也就是如果有一堆文本只会取其中关联度最大的一段,如果没有关联,则返回空
			String bestFragment = highlighter.getBestFragment(new IKAnalyzer(), "f",text);
			if(bestFragment == null){
				System.out.println(text);
			}else{
				System.out.println(bestFragment);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
	}
}
