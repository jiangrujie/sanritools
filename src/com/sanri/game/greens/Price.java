package com.sanri.game.greens;

public class Price {
	private boolean countable;
	private String unit;
	private int money;	//以分做单位
	
	public Price(){}
	public Price(int money,String unit){
		this.money = money;
		this.unit = unit;
	}
	public Price(int money,String unit,boolean countable){
		this(money,unit);
		this.countable = countable;
	}
	
	public boolean isCountable() {
		return countable;
	}
	public void setCountable(boolean countable) {
		this.countable = countable;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
}
