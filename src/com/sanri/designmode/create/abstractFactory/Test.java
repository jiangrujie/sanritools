package com.sanri.designmode.create.abstractFactory;

import com.sanri.designmode.create.Sender;

public class Test {
	public static void main(String[] args) {
		Provide provide = new SmsSenderFactory();
		Sender produce = provide.produce();
		produce.send();
	}
}
