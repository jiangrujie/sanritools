package com.sanri.designmode.structor.adapter.class_;

import com.sanri.designmode.structor.Target;

public class Test {
	public static void main(String[] args) {
		Target target = new Adapter();
		target.method2();
	}
}
