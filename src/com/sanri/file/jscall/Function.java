package com.sanri.file.jscall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 创建时间:2016-10-10下午2:34:48<br/>
 * 创建者:sanri<br/>
 * 功能:找到的 js function <br/>
 */
public class Function {
	private int line;					//方法所在行数
	private int count;					//方法占用行数
	private String name;				//方法名称,当为匿名方法时 name 为 (anonymous)
	private List<Function> callee = new ArrayList<Function>();		//调用了哪些方法
//	private List<Function> caller = new ArrayList<Function>();		//被哪些方法调用
	private JsFile jsFile;				//所在 js 文件,如果脚本代码在 html,jsp,php 中的话,则 File 为 html File,行数为实际行数,不是脚本代码行数 
	
	public Function(){}
	public Function(int line,String name,JsFile jsfile){
		this.line = line;
		this.name = name;
		this.jsFile = jsfile;
	}
	
	/**
	 * 
	 * 功能:增加调用了哪些方法<br/>
	 * 创建时间:2016-10-14下午11:23:31<br/>
	 * 作者：sanri<br/>
	 * @param calleeFunction<br/>
	 */
	public void addCallee(Function calleeFunction){
		this.callee.add(calleeFunction);
	}
	/**
	 * 
	 * 功能:增加被哪些方法调用<br/>
	 * 创建时间:2016-10-14下午11:24:04<br/>
	 * 作者：sanri<br/>
	 * @param callerFunction<br/>
	 */
//	public void addCaller(Function callerFunction){
//		this.caller.add(callerFunction);
//	}
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Function> getCallee() {
		return callee;
	}
	public void setCallee(List<Function> callee) {
		this.callee = callee;
	}
	
	/**
	 * 同一个 js 文件中的同一行方法名相同肯定是同一个方法,压缩 js 文件不考虑在内
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Function){
			Function other = (Function)obj;
			if(this.jsFile == null || other.jsFile == null){
				//没有 js 文件无法比较
				return false;
			}
			if(this.jsFile.equals(other.jsFile) && this.line == other.line){
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public JsFile findLocationFile(){
		return this.jsFile;
	}
	
	@Override
	public String toString() {
		return this.name+"@"+this.line;
	}
}
