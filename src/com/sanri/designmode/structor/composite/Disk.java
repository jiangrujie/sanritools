package com.sanri.designmode.structor.composite;

import java.util.Iterator;

public class Disk extends Equipment {
	// 硬盘实体价格
	public static double diskNetPrice = 2.0;
	// 硬盘折扣价格
	public static double diskDiscountPrice = 1.0;

	public Disk(String name) {
		super(name);
	}
	@Override
	public double netPrice() {
		return diskNetPrice;
	}
	@Override
	public double discountPrice() {
		return diskDiscountPrice;
	}
	@Override
	public boolean add(Equipment equipment) {
		return false;
	}
	@Override
	public boolean remove(Equipment equipment) {
		return false;
	}
	@Override
	public Iterator<Equipment> iter() {
		return null;
	}
}

