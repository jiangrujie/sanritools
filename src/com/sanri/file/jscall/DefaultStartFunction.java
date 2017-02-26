package com.sanri.file.jscall;

import static sanri.utils.Validate.PART;

import java.util.regex.Pattern;

import sanri.utils.Validate;


public class DefaultStartFunction implements StartFunction {
	private Pattern startFunction = Pattern.compile(PART+"*\\$"+PART+"*\\("+JsFile.ANONYMOUS_FUNCTION);
	
	public final static DefaultStartFunction DEFAULT_START_FUNCTION = new DefaultStartFunction();
	
	/**
	 * 此处只对 jquery 的启动函数做了验证
	 */
	@Override
	public boolean accept(Function function) {
		JsFile jsFile = function.findLocationFile();
		String functionName = function.getName();
		if("anonymous".equals(functionName)){
			//匿名函数式启动函数 
			int line = function.getLine();
			String[] subline = jsFile.subline(line,line+1);
			if(!Validate.isEmpty(subline)){
				String currentLine = subline[0];
				if(Validate.validate(currentLine, startFunction)){
					return true;
				}
			}
		}else if("window.onload".equals(functionName) || "document.onload".equals(functionName)){
			return true;
		}
		return false;
	}

}
