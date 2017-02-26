package com.sanri.designmode.structor.composite;
public class Chassis extends CompositeEquipment {
	public static double chassisNetPrice = 2.0;
	public static double chassisDiscountPrice = 1.0;

	public Chassis(String name) {
		super(name);
	}

	// 盒子的价格以及盒子里面硬盘价格.
	public double netPrice() {
		return chassisNetPrice + super.netPrice();
	}

	//
	public double discountPrice() {
		return chassisDiscountPrice + super.discountPrice();
	}

	@Override
	public boolean remove(Equipment equipment) {
		// TODO Auto-generated method stub
		return false;
	}
}
