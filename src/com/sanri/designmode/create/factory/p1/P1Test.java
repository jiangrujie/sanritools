package com.sanri.designmode.create.factory.p1;

import com.sanri.designmode.create.Sender;

public class P1Test {
	public static void main(String[] args) {
		SenderFactory factory = new SenderFactory();
		Sender produce = factory.produce("sms");
		produce.send();
	}
}
