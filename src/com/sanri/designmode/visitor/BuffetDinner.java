package com.sanri.designmode.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 创建时间:2016-10-3上午9:09:38<br/>
 * 创建者:sanri<br/>
 * 功能:吃饭的时候,先上好食物,然后让客户自已选自己想要的,就像自助餐一样<br/>
 */
public class BuffetDinner {
	//食物列表
	private List<Food> foods = new ArrayList<Food>();
	
	public void attach(Food food){
		foods.add(food);
	}
	public void detach(Food food){
		foods.remove(food);
	}
	
	public void accept(Visitor visitor){
		for (Food food:foods) {
			food.accept(visitor);
		}
	}
}
