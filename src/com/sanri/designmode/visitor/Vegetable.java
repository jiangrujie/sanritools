package com.sanri.designmode.visitor;

public class Vegetable extends Food {
	@Override
	public void accept(Visitor visitor) {
		visitor.visitVegetable(this);
	}

	@Override
	public String getId() {
		return "vegetable";
	}
}
