package com.sanri.designmode.structor.adapter.class_;

import com.sanri.designmode.structor.Source;
import com.sanri.designmode.structor.Target;
/**
 * 类的适配器模式,将 Source 的方法适配到 target
 * 详情见日记 20160120 这个java解释有问题
 */
public class Adapter extends Source implements Target {

	@Override
	public void method2() {
		System.out.println("this is you do implements methods2 from source method1");
		method1();
	}

}
