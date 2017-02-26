package com.sanri.designmode.visitor;

import junit.framework.TestCase;

import org.junit.Test;

public class ClientTest extends TestCase {
	
	@Test
	public void testDinner(){
		BuffetDinner dinner = new BuffetDinner();
		Food coffee = new Coffee();
		Food vegetable = new Vegetable();
		Food meat = new Meat();
		
		dinner.attach(meat);
		dinner.attach(vegetable);
		dinner.attach(coffee);
		
		Visitor zhangsan = new Zhangsan();
		Visitor lisi = new Lisi();
		System.out.println("*****张三");
		dinner.accept(zhangsan);
		System.out.println("*****李四");
		dinner.accept(lisi);
	}
}
