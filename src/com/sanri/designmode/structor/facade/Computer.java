package com.sanri.designmode.structor.facade;
/**
 * 外观模式,即整体与部分,把部分组合起来
 * @author sanri
 *
 */
public class Computer {
	private CPU cpu;
	private Memory memory;
	private Disk disk;
	
	public Computer (){
		cpu  = new CPU();
		memory = new Memory();
		disk = new Disk();
	}
	
	public void start(){
		System.out.println("computer start...");
		cpu.start();
		memory.start();
		disk.start();
		System.out.println("computer start finish");
	}
	
	public void close(){
		System.out.println("computer close...");
		cpu.close();
		memory.close();
		disk.close();
		System.out.println("computer close finish");
	}
	
}
