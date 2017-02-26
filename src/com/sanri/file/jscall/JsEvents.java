package com.sanri.file.jscall;

import org.w3c.dom.Element;

/**
 * 
 * 创建时间:2016-10-12下午2:22:35<br/>
 * 创建者:sanri<br/>
 * 功能: dom 事件<br/>
 */
public class JsEvents {
	private Element element;
	private Function caller;
	private int line;
	
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public Function getCaller() {
		return caller;
	}
	public void setCaller(Function caller) {
		this.caller = caller;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
}
