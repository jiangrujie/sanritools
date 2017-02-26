package temp.learn.lucene;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 
 * @author sanri
 * 功能说明:同义词过滤器<br/>
 * input 表示上一个的 tokenStream ,this 表示本身的 tokenStream
 * 创建时间:2017-1-5下午2:10:26
 */
public class MySameWordFilter extends TokenFilter{
	private Log logger = LogFactory.getLog(getClass());
	private Map<String,List<String>> SAME_WORD_MAP = new HashMap<String, List<String>>();
	
	private CharTermAttribute charTermAttribute;
	
	protected MySameWordFilter(TokenStream input) {
		super(input);
		charTermAttribute = input.addAttribute(CharTermAttribute.class);
	}

	/**
	 * 每个 token 都会执行一次这个方法
	 * return true 都会走下一个
	 */
	@Override
	public boolean incrementToken() throws IOException {
		if("个".equals(charTermAttribute.toString())){
			charTermAttribute.setEmpty();
			charTermAttribute.append("只");
		}
		return input.incrementToken();
	}

	
}
