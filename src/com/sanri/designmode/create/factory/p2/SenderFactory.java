package com.sanri.designmode.create.factory.p2;

import com.sanri.designmode.create.MailSender;
import com.sanri.designmode.create.Sender;
import com.sanri.designmode.create.SmsSender;
/**
 * 简单工厂模式改进 2 
 * @author sanri
 *
 */
public class SenderFactory {
	public Sender produceSms(){
		return new SmsSender();
	}
	public Sender produceMail(){
		return new MailSender();
	}
}
