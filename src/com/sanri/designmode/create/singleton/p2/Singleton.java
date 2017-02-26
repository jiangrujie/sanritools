package com.sanri.designmode.create.singleton.p2;


/**
 * 单例模式改进 2 网上的
 * 错误:这个会报错,synchronized 不支持空对象 ?????
 * @author sanri
 *
 */
public class Singleton {
	/* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
	private static Singleton instance = null;

	/* 私有构造方法，防止被实例化 */
	private Singleton() {
	}

	/* 静态工程方法，创建实例 */
	public static Singleton getInstance() {
		if (instance == null) {
			synchronized (instance) {
				instance = new Singleton();
			}
		}
		return instance;
	}

	/* 如果该对象被用于序列化，可以保证对象在序列化前后保持一致 */
	public Object readResolve() {
		return instance;
	}
	
	public static void main(String[] args) {
		Singleton instance2 = Singleton.getInstance();
		System.out.println(instance2);
	}

}
