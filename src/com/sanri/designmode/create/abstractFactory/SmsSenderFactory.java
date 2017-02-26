package com.sanri.designmode.create.abstractFactory;

import com.sanri.designmode.create.Sender;
import com.sanri.designmode.create.SmsSender;
/**
 * 抽象工厂模式,实现了开闭原则,但个人感觉好像变麻烦了,
 * 以前可以 Sender sender = new XXSender(); sender.send() 就好了
 * @author sanri
 *
 */
public class SmsSenderFactory implements Provide{

	@Override
	public Sender produce() {
		return new SmsSender();
	}

}
