package com.sanri.designmode.structor.adapter.object;

import com.sanri.designmode.structor.Source;
import com.sanri.designmode.structor.Target;
/**
 * 对象的适配器模式 
 * 为了解决类的适配器的兼容性问题(什么兼容性问题?)
 * 可能是 source 的方法和 target 的方法完全不同,但有一些功能是一样的,
 * 直接继承 source 的话,会多出很多无用的方法(在 target 中用不上)
 * @author sanri
 *
 *新理解 :我适配你,帮你完成一些你以前没有接触过的事情 20160120 有日记
 */
public class Wrapper implements Target{
	private Source source;

	public Wrapper(Source source){
		this.source = source;
	}
	
	@Override
	public void method1() {
		source.method1();
	}

	@Override
	public void method2() {
		System.out.println("this is you do implements methods2 from source method1");
		method1();
	}
	
	
}
