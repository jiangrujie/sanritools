package com.sanri.file.search;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;

public class SearchInterest {
	/**
	 * 感兴趣的内容
	 */
	protected List<String> interests;
	/**
	 * 查找的目录
	 */
	protected List<File> dirs;
	/**
	 * 文件过滤
	 */
	protected IOFileFilter fileFilter;
	/**
	 * 距上下多少行为截取内容
	 */
	protected long up = 10;
	protected long down = 10;
	
	public SearchInterest(List<File> dirs,List<String> interests){
		this.dirs = dirs;
		this.interests = interests;
	}
	
	public List<String> getInterests() {
		return interests;
	}
	public void setInterests(List<String> interests) {
		this.interests = interests;
	}
	public List<File> getDirs() {
		return dirs;
	}
	public void setDirs(List<File> dirs) {
		this.dirs = dirs;
	}
	public long getUp() {
		return up;
	}
	public void setUp(long up) {
		this.up = up;
	}
	public long getDown() {
		return down;
	}
	public void setDown(long down) {
		this.down = down;
	}

	public IOFileFilter getFileFilter() {
		return fileFilter;
	}

	public void setFileFilter(IOFileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}
}
