package test.mini;

import static sanri.utils.Validate.IDENTIFIER;
import static sanri.utils.Validate.PART;
import static sanri.utils.Validate.PART_NAME;

import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.TestCase;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

public class TestRegex extends TestCase{
	public final static String FUNCTION = PART+"*function"+PART+"+"+"("+IDENTIFIER+")"+PART+"*\\("+PART+"*((var"+PART+")?"+IDENTIFIER+PART+"*\\,?"+PART+"*)*"+PART+"*\\)"+PART+"*\\{";
	public final static String ANONYMOUS_FUNCTION = PART+"*function"+"("+PART+"+"+IDENTIFIER+")?"+PART+"*\\("+PART+"*((var"+PART+")?"+IDENTIFIER+PART+"*\\,?"+PART+"*)*"+PART+"*\\)"+PART+"*\\{";
	
	public final static Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION);
	public final static Pattern PROPERTY_FUNCTION1 = Pattern.compile("((var"+PART+")?"+PART_NAME+")"+PART+"*="+ANONYMOUS_FUNCTION);
	public final static Pattern PROPERTY_FUNCTION2 = Pattern.compile(PART+"*("+IDENTIFIER+")"+PART+"*:"+PART+"*"+ANONYMOUS_FUNCTION);
	public final static Pattern AUTO_FUNCTION = Pattern.compile(PART+"*."+PART+"*\\("+ANONYMOUS_FUNCTION);

	@Test
	public void testPattern1(){
		String[] match = Validate.match("function setProperties(var prop, var param2){", FUNCTION_PATTERN);
		System.out.println(StringUtil.join(match,","));
	}
	
	public void testPattern2(){
		String[] match = Validate.match("abc.def.funName=function(mmm){", PROPERTY_FUNCTION1);
		System.out.println(StringUtil.join(match,","));
	}
	
	public void testPattern3(){
		String[] match = Validate.match("abc:function(mmm){", PROPERTY_FUNCTION2);
		System.out.println(StringUtil.join(match,","));
	}
}
