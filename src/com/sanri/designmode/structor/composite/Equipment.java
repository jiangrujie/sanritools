package com.sanri.designmode.structor.composite;
import java.util.Iterator;

public abstract class Equipment {
	protected String name;
	
	public Equipment(String name) {
		this.name = name;
	}
	// 实体价格
	public abstract double netPrice();
	// 折扣价格
	public abstract double discountPrice();
	// 增加部件的方法
	public abstract boolean add(Equipment equipment);
	// 移除部件方法
	public abstract boolean remove(Equipment equipment); 
	// 组合体内访问各个部件的方法.
	public abstract Iterator<Equipment> iter() ;
}
        