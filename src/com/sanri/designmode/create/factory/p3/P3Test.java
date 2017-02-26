package com.sanri.designmode.create.factory.p3;

import com.sanri.designmode.create.Sender;

public class P3Test {
	public static void main(String[] args) {
		Sender produceSms = SenderFactory.produceSms();
		produceSms.send();
	}
}
