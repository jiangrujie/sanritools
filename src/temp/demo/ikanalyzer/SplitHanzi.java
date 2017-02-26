package temp.demo.ikanalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class SplitHanzi {  
    public static void main(String[] args) throws IOException {  
        String text="java是一个好语言从到2015年12月1日它已经有20年的历史了"; 
        List<String> extStopWordDictionarys = Configuration.getExtStopWordDictionarys();
        //创建分词对象  
        Analyzer analyzer=new IKAnalyzer(true);       
        StringReader reader=new StringReader(text);  
        //分词  
        TokenStream ts=analyzer.tokenStream("", reader);  
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
        //遍历分词数据  
        while(ts.incrementToken()){  
            System.out.print(term.toString()+"|");  
        }  
        reader.close();  
        System.out.println();  
    }  
  
}  