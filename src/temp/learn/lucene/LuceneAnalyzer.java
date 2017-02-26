package temp.learn.lucene;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * @author sanri
 * 功能说明:lucene 分词演示<br/>
 * 创建时间:2017-1-4上午9:27:00
 * TokenStream{
 * 	CharTermAttribute
 * 	OffsetAttribute
 * 	PositionIncrementAttribute
 * 	TypeAttribute
 * }
 * 	Tokenizer
 * 	TokenFilter
 */
public class LuceneAnalyzer{
	private Version V = Version.LUCENE_35;
	
	@Test
	public void testAnalyzer(){
		try {
			Analyzer analyzer = new StandardAnalyzer(V);
			TokenStream tokenStream = analyzer.tokenStream("content", new FileReader("E:/project/sanritools/src/temp/demo/JSoupDemo.java"));
			CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			while(tokenStream.incrementToken()){
				System.out.println(addAttribute);
			}
			analyzer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllTokenInfo(){
		displayAllTokenInfo("默认分词器很烂的", new IKAnalyzer() );
	}
	
	@Test
	public void testStopWordAnalyzer(){
		Analyzer analyzer = new MyStopAnalyzer();
		displayAllTokenInfo("my name is xxx", analyzer);
	}
	
	/**
     * 
     * Description:        显示分词的全部信息
     * @param str
     * @param analyzer
     *
     */
    public  void displayAllTokenInfo(String str, Analyzer analyzer){
        try {
            //第一个参数只是标识性没有实际作用
            TokenStream stream = analyzer.tokenStream("", new StringReader(str));
            //获取词与词之间的位置增量
            PositionIncrementAttribute postiona = stream.addAttribute(PositionIncrementAttribute.class);
            //获取各个单词之间的偏移量
            OffsetAttribute offseta = stream.addAttribute(OffsetAttribute.class);
            //获取每个单词信息
            CharTermAttribute chara = stream.addAttribute(CharTermAttribute.class);
            //获取当前分词的类型
            TypeAttribute typea = stream.addAttribute(TypeAttribute.class);
            while(stream.incrementToken()){
                System.out.print("位置增量" +postiona.getPositionIncrement()+":\t");
                System.out.println(chara+"\t[" + offseta.startOffset()+" - " + offseta.endOffset() + "]\t<" + typea +">");
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSameWord(){
    	String str = "java 是个很烂的语言";
    	SameWordAnalyzer sameWordAnalyzer = new SameWordAnalyzer();
//    	IKAnalyzer ikAnalyzer = new IKAnalyzer(true);
    	displayAllTokenInfo(str, sameWordAnalyzer);
    }
}
