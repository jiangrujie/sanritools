package com.sanri.designmode.visitor;
/**
 * 
 * 创建时间:2016-10-3上午9:00:15<br/>
 * 创建者:sanri<br/>
 * 功能:抽象的客户<br/>
 */
public abstract class Visitor {
	public void visitCoffee(Coffee f){
		System.out.println("食物:coffee");
	}
	public  void visitMeat(Meat f){
		System.out.println("食物:meat");
	}
	public  void visitVegetable(Vegetable f){
		System.out.println("食物:vegetable");
	}
}
