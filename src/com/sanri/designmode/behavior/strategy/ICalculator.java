package com.sanri.designmode.behavior.strategy;
/**
 * 策略模式
 * 策略模式定义了一系列算法,并将它们封装起来,使它们可以相互替换,且算法的变化不会影响到使用算法的客户;
 * 需要设计一个接口,为一系列实现类提供统一的方法;再设计一个抽象类提供辅助函数
 * @author sanri
 *
 */
public interface ICalculator {
	int calculate(String exp);
}
