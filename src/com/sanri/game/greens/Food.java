package com.sanri.game.greens;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 创建时间:2016-11-11下午7:20:23<br/>
 * 创建者:sanri<br/>
 * 功能:食物实体类<br/>
 */
public class Food extends BaseEntity{
	private String name;
	private String foodType;
	private String quality;
	private String taste;
	private List<String> cookingMethods = new ArrayList<String>();
	private int singleScore;
	private GrowthTime growthTime;
	private Price price;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFoodType() {
		return foodType;
	}
	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
	public List<String> getCookingMethods() {
		return cookingMethods;
	}
	public void setCookingMethods(List<String> cookingMethods) {
		this.cookingMethods = cookingMethods;
	}
	public int getSingleScore() {
		return singleScore;
	}
	public void setSingleScore(int singleScore) {
		this.singleScore = singleScore;
	}
	public GrowthTime getGrowthTime() {
		return growthTime;
	}
	public void setGrowthTime(GrowthTime growthTime) {
		this.growthTime = growthTime;
	}
	public Price getPrice() {
		return price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	
	public Food addCookingMethod(String method){
		this.cookingMethods.add(method);
		return this;
	}
}
