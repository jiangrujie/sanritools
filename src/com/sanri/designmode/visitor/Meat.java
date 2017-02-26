package com.sanri.designmode.visitor;

public class Meat extends Food{
	@Override
	public void accept(Visitor visitor) {
		visitor.visitMeat(this);
	}

	@Override
	public String getId() {
		return "meat";
	}
}
