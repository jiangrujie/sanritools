package com.sanri.designmode.structor.proxy.static_;

import com.sanri.designmode.structor.decorator.Sourceable;
/**
 * 静态代理和装饰模式差不多,区别就是代理模式需要代理的类的方法在使用者看来可能晦涩难懂,
 * 经过代理之后变成容易理解的方法,并在方法前后做相应处理,或者在代理方法中合成一些方法,
 * 代理可能是对用户使用所需代理的类隐藏一些隐密的东西;如房子中介,就会对你隐藏房东信息
 */
public class StaticProxy implements Sourceable {
	private Sourceable sourceable = new SourceableImpl();
	/**
	 * 限制使用,偶数时间不让进,否则输出当前时间
	 */
	@Override
	public void method() {
		if(System.currentTimeMillis() % 2 != 0)return ;
		sourceable.method();
		System.out.println(System.currentTimeMillis());
	}
	
}
