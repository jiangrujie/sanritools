package com.sanri.designmode.visitor;

public class Lisi extends Visitor {

	@Override
	public void visitCoffee(Coffee f) {
		super.visitCoffee(f);
		f.addMilk();
		f.addSugar();
	}

	@Override
	public void visitMeat(Meat f) {
		super.visitMeat(f);
		System.out.println("一大盘");
	}

	@Override
	public void visitVegetable(Vegetable f) {
		super.visitVegetable(f);
		System.out.println("不吃");
	}
	
}
