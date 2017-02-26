package com.sanri.designmode.visitor;
/**
 * 
 * 创建时间:2016-10-3上午9:02:06<br/>
 * 创建者:sanri<br/>
 * 功能:抽象食物<br/>
 */
public abstract class Food {
	public abstract void accept(Visitor visitor);//经过的意思 在 buffetDinner 中的 foreach 中可以看出,把所有食物顺序的经过某个人,看这个人想要什么
	public abstract String getId();
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Food){
			Food other = (Food) obj;
			return this.getId().equals(other.getId());
		}
		return super.equals(obj);
	}
}
