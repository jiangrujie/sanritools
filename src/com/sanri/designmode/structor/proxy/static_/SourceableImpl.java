package com.sanri.designmode.structor.proxy.static_;

import com.sanri.designmode.structor.decorator.Sourceable;

public class SourceableImpl implements Sourceable {

	@Override
	public void method() {
		System.out.println("真实的实现类");
	}

}
