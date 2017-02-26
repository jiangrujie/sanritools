package com.sanri.file.jscall;

/**
 * 
 * 创建时间:2016-10-13上午10:06:32<br/>
 * 创建者:sanri<br/>
 * 功能:启动函数接口,由用户实现,哪个函数为启动函数 <br/>
 */
public interface StartFunction {
	/**
	 * 
	 * 功能:接受哪些启动方法<br/>
	 * 创建时间:2016-10-13上午10:07:28<br/>
	 * 作者：sanri<br/>
	 * @param file 哪个 js 文件
	 * @param function 哪个方法为启动方法
	 * @return<br/>
	 */
	boolean accept(Function function);
}
