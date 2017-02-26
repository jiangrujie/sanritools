package com.sanri.designmode.visitor;

public class Zhangsan extends Visitor{

	@Override
	public void visitCoffee(Coffee f) {
		super.visitCoffee(f);
		f.addMilk();
	}

	@Override
	public void visitMeat(Meat f) {
		super.visitMeat(f);
		System.out.println("不吃");
	}

	@Override
	public void visitVegetable(Vegetable f) {
		super.visitVegetable(f);
		System.out.println("一大盘");
	}

}
