package com.sanri.designmode.visitor;

public class Coffee extends Food {
	/**
	 * 这个方法就是这个食物经过客户的时候 ,客户的动作,现在是访问这个食物
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visitCoffee(this);	//客户访问 this
	}
	
	public void addSugar(){
		System.out.println("加糖");
	}
	
	public void addMilk(){
		System.out.println("加牛奶");
	}

	@Override
	public String getId() {
		return "coffee";
	}
	
}
