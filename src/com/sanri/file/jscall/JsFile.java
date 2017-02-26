package com.sanri.file.jscall;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

import com.alibaba.fastjson.JSONObject;
import com.sanri.file.search.TextFile;

import static sanri.utils.Validate.IDENTIFIER;
import static sanri.utils.Validate.PART;
import static sanri.utils.Validate.PART_NAME;


/**
 * 
 * 创建时间:2016-10-12下午2:17:51<br/>
 * 创建者:sanri<br/>
 * 功能:单个 js 文件的解析,解析的最终结果是 Function,同时保存本文件的函数个数,如果是 html等文件,则同时解析出事件<br/>
 * 思路:找到启动方法,然后对启动方法调用的方法进行递归,直到没有方法可找; 
 * 		第一遍扫描先找出所有方法和行数的标记,用方法名@行数来唯一标识一个方法
 *  <ul>
 * 		<li>@see resetStartMethod(Function... methods);</li>
 * 		<li>@see addStartMethod(Function method);</li>
 * 		<li>@see removeStartMethod(Function method);</li>
 *  </ul>
 *  对于函数的匹配,采用正则匹配的方式,常用的函数定义方法有
 *  function setProperties(var prop){
 *  function setAbcde(prop){
 *    function   setParameter(params){
 *  var funName =   function(){
 *  abc.def.funName.funName = function(){
 *  abc['xxx'] = function(){
 *  ;(function(){  | +(function(){ | -(function(){ ...
 *  funFlag: function(){
 */
public class JsFile extends TextFile{
	private int functionCount;
	private Function mainFunction;
	private StartFunction startFunction;
	private List<JsEvents> jsEvents = new ArrayList<JsEvents>();
	private Map<String,Function> functionsMap = new HashMap<String, Function>();		//这里放的所有函数 函数名@行数==>函数
	private Map<String,Function> noCallBackFunctions = new HashMap<String, Function>();		//这里放的除回调函数的所有函数 函数名==>函数,当有相同函数名时,取最后一个
	
	// function method ( var parama , paramb ) { 
	public final static String FUNCTION = PART+"*function"+PART+"+"+"("+IDENTIFIER+")"+PART+"*\\("+PART+"*((var"+PART+")?"+IDENTIFIER+PART+"*\\,?"+PART+"*)*"+PART+"*\\)"+PART+"*\\{"+PART+"*";
	public final static String ANONYMOUS_FUNCTION = PART+"*function"+"("+PART+"+"+IDENTIFIER+")?"+PART+"*\\("+PART+"*((var"+PART+")?"+IDENTIFIER+PART+"*\\,?"+PART+"*)*"+PART+"*\\)"+PART+"*\\{"+PART+"*";
	
	public final static Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION);
	public final static Pattern PROPERTY_FUNCTION1 = Pattern.compile("((var"+PART+")?"+PART_NAME+")"+PART+"*="+ANONYMOUS_FUNCTION);
	public final static Pattern PROPERTY_FUNCTION2 = Pattern.compile(PART+"*("+IDENTIFIER+")"+PART+"*:"+PART+"*"+ANONYMOUS_FUNCTION);
	public final static Pattern AUTO_FUNCTION = Pattern.compile(PART+"*."+PART+"*\\("+ANONYMOUS_FUNCTION);
	//   obj.funName(param1,param2,{a:b});
	public final static Pattern FUNCTION_CALL = Pattern.compile(PART+"*("+PART_NAME+")"+PART+"*\\("+PART+"*(.+,)*(.+)?"+PART+"*\\)"+PART+"*;?"+PART+"*");
	public final static Pattern FUNCTION_BEGIN = Pattern.compile("\\{");
	public final static Pattern FUNCTION_END = Pattern.compile("\\}");
	
	private static Log logger = LogFactory.getLog(JsFile.class);
	
	public JsFile(String pathname) throws FileNotFoundException{
		super(pathname);
	}
	
	/**
	 * 
	 * 功能:扫描文件,得到所有方法和行数标记<br/>
	 * 	同时找到启动方法
	 * 创建时间:2016-10-13上午9:31:05<br/>
	 * 作者：sanri<br/><br/>
	 */
	private void scannerMethods() {
		//初始化启动方法
		if(startFunction == null){
			startFunction = DefaultStartFunction.DEFAULT_START_FUNCTION;
		}
		Reader fileReader = null;
		try {
			fileReader = new InputStreamReader(new FileInputStream(this),charset);
			List<String> readLines = IOUtils.readLines(fileReader);
			if(!Validate.isEmpty(readLines)){
				for(int line=0;line<readLines.size();line++){
					String lineString = readLines.get(line);
					String functionName = "";
					if(Validate.validate(lineString, FUNCTION_PATTERN)){
						String[] match = Validate.match(lineString, FUNCTION_PATTERN);
						if(!Validate.isEmpty(match)){
							functionName = match[0];
						}
					}else if(Validate.validate(lineString, AUTO_FUNCTION)){
						//这里所有的函数都是匿名函数
						functionName = "anonymous";
					}else if(Validate.validate(lineString, PROPERTY_FUNCTION1)){
						String[] match = Validate.match(lineString, PROPERTY_FUNCTION1);
						if(!Validate.isEmpty(match)){
							functionName = match[0];
						}
					}else if(Validate.validate(lineString, PROPERTY_FUNCTION2)){
						String[] match = Validate.match(lineString, PROPERTY_FUNCTION2);
						if(!Validate.isEmpty(match)){
							functionName = match[0];
						}
					} 
					//如果找到了函数,则放入 map 
					if(!StringUtil.isBlank(functionName)){
						Function currentFunction = new Function(line,functionName,this);
						if(startFunction.accept(currentFunction)){
							//这里只找最后一个启动方法,多外启动方法是不支持的
							mainFunction = currentFunction;
						}
						this.functionCount ++;
						functionsMap.put(functionName+"@"+line,currentFunction );
						noCallBackFunctions.put(functionName,currentFunction );
					}
					
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fileReader);
		}
	}

	public JsFile(File file) throws FileNotFoundException {
		this(file.getAbsolutePath());
	}
	
	/**
	 * 
	 * 功能:对 js 文件的解析<br/>
	 * 创建时间:2016-10-12下午2:36:14<br/>
	 * 作者：sanri<br/><br/>
	 */
	public void parse() {
		scannerMethods();
		parseFunction(this.mainFunction);
	}
	
	
	/**
	 * 
	 * 功能:对方法的解析,给出的方法只有一个方法名或只有一个行数,需要得到方法所有内容<br/>
	 * 创建时间:2016-10-12下午2:38:31<br/>
	 * 作者：sanri<br/><br/>
	 */
	private void parseFunction(Function function){
		if(function == null){return ;}		//当前方法为空,不需要解析
		
		int line = function.getLine();
//		this.seekLine(line);
		long currentReadLine = line;		//需要记住当前读到的行数
		try {
			int stack = 0;int funCountLine = 0;
			do{
				this.seekLine(currentReadLine++);
				String readLine = this.readLine();		//读出来的一行是有换行符的
				funCountLine ++;
				long beginCount = Validate.countRegex(readLine, FUNCTION_BEGIN);
				long endCount = Validate.countRegex(readLine, FUNCTION_END);
				
				stack += (beginCount - endCount);
				//查找调用的方法
				if(Validate.validate(readLine, FUNCTION_CALL)){
					//可能一行调用了多个函数,暂时只考虑一行只调用一个函数的情况 TODO 
					
					String[] match = Validate.match(readLine, FUNCTION_CALL);
					//如果里面有 function ,则为错误的匹配 ,暂时先这样做 TODO 
					if(match[0].indexOf("function") != -1){
						continue;
					}
					if(match[0].equals(function.getName())){
						//当读第一行时,还是本身方法,继续读下一行
						continue;
					}
					//如果满足调用方法正则,则得到调用的方法
					Function callFunction = noCallBackFunctions.get(match[0]);
					if(callFunction == null){
						logger.warn(match[0]+" 函数在 "+this +" 文件中未定义,可能是引用别的文件中的方法或系统方法 ");
						continue;
					}
					
					function.addCallee(callFunction);
//					callFunction.addCaller(function);
					
					//继续解析被调用的方法
					parseFunction(callFunction);
				}
			}while(stack != 0);		//直到 stack == 0 结束 
			function.setCount(funCountLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getFunctionCount() {
		return functionCount;
	}

	public void setFunctionCount(int functionCount) {
		this.functionCount = functionCount;
	}

	public Function getMainFunction() {
		return mainFunction;
	}

	public void setMainFunction(Function mainFunction) {
		this.mainFunction = mainFunction;
	}

	public StartFunction getStartFunction() {
		return startFunction;
	}

	public void setStartFunction(StartFunction startFunction) {
		this.startFunction = startFunction;
	}

	public List<JsEvents> getJsEvents() {
		return jsEvents;
	}

	public void setJsEvents(List<JsEvents> jsEvents) {
		this.jsEvents = jsEvents;
	}

	public Map<String, Function> getFunctionsMap() {
		return functionsMap;
	}

	public void setFunctionsMap(Map<String, Function> functionsMap) {
		this.functionsMap = functionsMap;
	}
}
