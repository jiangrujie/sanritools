package com.sanri.file.jscall;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

/**
 * 
 * 创建时间:2016-10-10下午3:33:28<br/>
 * 创建者:sanri<br/>
 * 功能:脚本文件支持的后缀,默认是 *.js,*.htm,*.html,*.jsp,*.php <br/>
 */
public class JsFileFilter extends HashSet<String> implements FileFilter{
	public static JsFileFilter JS_FILE_FILTER = new JsFileFilter();
	private String suffixString = "";
	
	private JsFileFilter(){
		this.add(".js");
		this.add(".html");
		this.add(".htm");
		this.add(".jsp");
		this.add(".php");
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()){return true;}
		//文件需要脚本文件
		if(!Validate.isEmpty(pathname) && pathname.isFile()){
			String suffix = StringUtil.suffix(pathname.getName());
			if(!StringUtil.isBlank(suffix) && suffixString.toLowerCase().indexOf(suffix.toLowerCase()) != -1){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean add(String e) {
		boolean addRes = super.add(e);
		resetSuffixString();
		return addRes;
	}
	/*
	 * 重置 后缀字符串
	 */
	private void resetSuffixString() {
		suffixString = "$"+StringUtil.join(this,"$")+"$";
	}

	@Override
	public boolean remove(Object o) {
		boolean removeRes =  super.remove(o);
		resetSuffixString();
		return removeRes;
	}

}
