package com.sanri.game.greens;

public class GrowthTime {
	private String season;
	private int month;
	
	public GrowthTime(String season,int month){
		this.season = season;
		this.month = month;
	}
	
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
}
