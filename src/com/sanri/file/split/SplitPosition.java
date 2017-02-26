package com.sanri.file.split;

public class SplitPosition {
	private long start;
	private long length;
	
	public SplitPosition(long start,long length){
		this.start = start;
		this.length = length;
	}
	
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
}
