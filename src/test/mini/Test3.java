package test.mini;  
  
import java.io.IOException;
import java.util.regex.Pattern;

import sanri.utils.StringUtil;
import sanri.utils.Validate;
  
public class Test3 {  
      
    public static void main(String[] args) throws IOException {  
//        String text="基于java语言开发的轻量级的中文分词工具包";  
//        StringReader sr=new StringReader(text);  
//        IKSegmenter ik=new IKSegmenter(sr, true);   //3.2.8 没有这个类
//        Lexeme lex=null;  
//        while((lex=ik.next())!=null){  
//            System.out.print(lex.getLexemeText()+"|");  
//        }  
    	Pattern addressPattern = Pattern.compile("(.+)<(.+)>");
    	String[] match = Validate.match("2441719087@qq.com", addressPattern);
    	System.out.println(StringUtil.join(match,","));
    	
    }  
  
}  