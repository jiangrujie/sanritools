package com.sanri.file.search;

import java.io.File;

public class Find implements Comparable<Find> {
	/**
	 * 查找到的文件
	 */
	private File file;
	/**
	 * 行数
	 */
	private long line;
	/**
	 * 正则
	 */
	private String interest;
	/**
	 * 上下文
	 */
	private String context;
	/**
	 * 标记字符串
	 */
	private String mark;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public long getLine() {
		return line;
	}
	public void setLine(long line) {
		this.line = line;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	@Override
	public int compareTo(Find o) {
		return interest.charAt(0) - o.interest.charAt(0);
	}
}
