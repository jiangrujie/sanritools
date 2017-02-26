package com.sanri.designmode.create.factory.p1;

import com.sanri.designmode.create.MailSender;
import com.sanri.designmode.create.Sender;
import com.sanri.designmode.create.SmsSender;
/**
 * 简单工厂模式,可能存在字符串传错而导致不能正确创建对象
 * @author sanri
 *
 */
public class SenderFactory {
	public Sender produce(String type){
		if("sms".equals(type)){
			return new SmsSender();
		}else if("mail".equals(type)){
			return new MailSender();
		}
		return null;
	}
}
