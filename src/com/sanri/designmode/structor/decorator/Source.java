package com.sanri.designmode.structor.decorator;

public class Source implements Sourceable {

	@Override
	public void method() {
		System.out.println("被装饰者的原始方法");
	}

}
