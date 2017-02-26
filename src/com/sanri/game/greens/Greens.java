package com.sanri.game.greens;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jetty.util.ArrayQueue;

/**
 * 
 * 创建时间:2016-11-11下午7:20:36<br/>
 * 创建者:sanri<br/>
 * 功能:菜的实体类<br/>
 */
public class Greens extends BaseEntity{
	private String name;
	private List<Food> foods = new ArrayList<Food>();
	private List<String> cookingQueue = new ArrayList<String>();
	private String cuisines;
	private String image;
	private String taste;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Food> getFoods() {
		return foods;
	}
	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}
	public String getCuisines() {
		return cuisines;
	}
	public void setCuisines(String cuisines) {
		this.cuisines = cuisines;
	}
	
	public Greens addFood(Food food){
		this.foods.add(food);
		return this;
	}
	public Greens addCookingMethod(String method){
		this.cookingQueue.add(method);
		return this;
	}
	public List<String> getCookingQueue() {
		return cookingQueue;
	}
	public void setCookingQueue(List<String> cookingQueue) {
		this.cookingQueue = cookingQueue;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
}
