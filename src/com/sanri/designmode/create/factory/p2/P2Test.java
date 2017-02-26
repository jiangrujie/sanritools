package com.sanri.designmode.create.factory.p2;

import com.sanri.designmode.create.Sender;

public class P2Test {
	public static void main(String[] args) {
		SenderFactory factory =new SenderFactory();
		Sender produceSms = factory.produceSms();
		produceSms.send();
	}
}
