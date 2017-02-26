package com.sanri.designmode.create.factory.p3;

import com.sanri.designmode.create.MailSender;
import com.sanri.designmode.create.Sender;
import com.sanri.designmode.create.SmsSender;
/**
 * 简单工厂模式改进3 方法改为了静态
 * @author sanri
 *
 */
public class SenderFactory {
	public static Sender produceSms(){
		return new SmsSender();
	}
	public static Sender produceMail(){
		return new MailSender();
	}
}
