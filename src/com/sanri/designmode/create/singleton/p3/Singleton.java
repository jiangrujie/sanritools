package com.sanri.designmode.create.singleton.p3;

/**
 * 单例模式改进 3 
 * @author sanri
 *
 */
public class Singleton {
	private static Singleton instance = new Singleton();
	
	public static Singleton getInstance(){
		return instance;
	}
	
	/* 如果该对象被用于序列化，可以保证对象在序列化前后保持一致 */
	public Object readResolve() {
		return instance;
	}
}
