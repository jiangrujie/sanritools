package com.sanri.designmode.create.builder;

import java.util.ArrayList;
import java.util.List;

import com.sanri.designmode.create.MailSender;
import com.sanri.designmode.create.Sender;
import com.sanri.designmode.create.SmsSender;

/**
 * 建造者模式
 * @author sanri
 *
 */
public class Builder {
	private List<Sender> senders;
	
	public List<Sender> createSmsSenders(int count){
		senders = new ArrayList<Sender>();
		for(int i= 0 ;i<count;i++){
			senders.add(new SmsSender());
		}
		return this.senders;
	}
	
	public List<Sender> createMailSenders(int count){
		senders =new ArrayList<Sender>();
		for(int i= 0 ;i<count;i++){
			senders.add(new MailSender());
		}
		return senders;
		
	}
}
