package com.sanri.file.split;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class SplitThread implements Callable<File> {
	private RandomAccessFile accessFile;		//这个不能做共享数据,因为里面包含有文件指针,如果做共享数据,则文件指针在乱动导致结果不正确
	private FileChannel fileChannel;
	private Charset charset = Charset.forName("utf-8");
	private SplitPosition splitPosition;
	private SplitFileRenamePolicy fileRenamePolicy;
//	private int buffSize = 3145728; //每次缓冲 3M
	private int buffSize = 1024; //每次缓冲 1kb
	private File outputDir;
	private int index;
	private File largeFile;
	
	public SplitThread(File largeFile){
		this.largeFile = largeFile;
		try {
			this.accessFile = new RandomAccessFile(largeFile, "r");
			this.fileChannel = accessFile.getChannel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public SplitPosition getSplitPosition() {
		return splitPosition;
	}

	public void setSplitPosition(SplitPosition splitPosition) {
		this.splitPosition = splitPosition;
	}


	@Override
	public File call() throws Exception {
		//目标文件创建
		long start = splitPosition.getStart();
		long length = splitPosition.getLength();
		String fileName = fileRenamePolicy.handle(index, largeFile, start,length);
		File outFile = new File(outputDir,fileName);
		outFile.createNewFile();
		
		//写文件通道
		RandomAccessFile outAccessFile = new RandomAccessFile(outFile, "rw");
		FileChannel writeChannel = outAccessFile.getChannel();
		
		//查找位置开始写入
		accessFile.seek(start);
		long readLength = 0,sum = 0;
		int count = 0;			//读取次数,每隔一定次数强制写入磁盘
		ByteBuffer buffer = ByteBuffer.allocate(buffSize);
		boolean last = false;
		do{
			int remainder = (int) (length - count * buffSize);
			if(remainder < buffSize || length < buffSize){
				buffer.limit(remainder);		//当没有足够的字节需要读取时,限制读取的范围; 或本身文件很小不足够 buffSize
				last = true;
			}
			readLength = fileChannel.read(buffer);
			buffer.flip();
			writeChannel.write(buffer);
			buffer.clear();		//写完之后清空些缓冲区,否则就读不到数据了
			count ++;
			sum += readLength;
			if(count % 1024 == 0){	//每 1M 强制写入磁盘
				writeChannel.force(true);
			}
		}while(readLength > 0 && !last);
		
		System.err.println("线程 "+Thread.currentThread().getName()+"总共写了 "+sum+" 字节数据");
		writeChannel.force(true);
		return outFile;
	}


	public SplitFileRenamePolicy getFileRenamePolicy() {
		return fileRenamePolicy;
	}

	public void setFileRenamePolicy(SplitFileRenamePolicy fileRenamePolicy) {
		this.fileRenamePolicy = fileRenamePolicy;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public File getLargeFile() {
		return largeFile;
	}

	public void setLargeFile(File largeFile) {
		this.largeFile = largeFile;
	}

}
