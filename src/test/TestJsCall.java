package test;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.sanri.file.jscall.DefaultStartFunction;
import com.sanri.file.jscall.Function;
import com.sanri.file.jscall.JsFile;

import sanri.utils.PathUtil;
import junit.framework.TestCase;


public class TestJsCall extends TestCase {
	
	@Test
	public void testJsCall(){
		String pkgPath = PathUtil.pkgPath("temp.test");
		try {
			JsFile jsFile = new JsFile(pkgPath+File.separator+"acctDispManagerView.html");
			jsFile.setStartFunction(startFunction);
			jsFile.parse();
			System.out.println(JSONObject.toJSONString(jsFile.getMainFunction()));
			System.out.println(jsFile.getFunctionCount());
			System.out.println(JSONObject.toJSONString(jsFile.getFunctionsMap()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private StartFunction startFunction = new StartFunction();
	
	static class StartFunction extends DefaultStartFunction{
		@Override
		public boolean accept(Function function) {
			boolean superAccept =  super.accept(function);
			if(!superAccept){
				String name = function.getName();
				if("kuiloader.onReady".equals(name)){
					return true;
				}
			}
			return superAccept;
		}
	}
}
