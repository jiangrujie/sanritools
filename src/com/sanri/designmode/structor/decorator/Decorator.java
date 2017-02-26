package com.sanri.designmode.structor.decorator;

/**
 * 装饰者模式,两个类实现同一接口,装饰类拥有被装饰类的实例,在被装饰类的方法调用前后为其增加内容
 * @author sanri
 *
 */

public class Decorator  implements Sourceable{
	private Sourceable source;
	//通过构造传入实现同一接口的对象
	public Decorator(Sourceable source){
		this.source = source;
	}
	@Override
	public void method() {
		System.out.println("原始方法调用之前的处理");
		source.method();
		System.out.println("原始方法调用之后的处理");
	}
	
}
