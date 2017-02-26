package com.sanri.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 作者: sanri<br/>
 * 时间:2016-9-18上午9:53:05
 * 功能:同步 u 盘与 电脑上的文件策略如下: <br/>
 * <ul>
 * 	<li>把 u 盘当成是云,每隔一段时间进行一次同步 </li>
 * 	<li>如果 u 盘中没有,而电脑上有的文件上传至 u 盘</li>
 * 	<li>电脑中没有, u 盘中有,则下载到电脑</li>
 * 	<li>两者中都有,则上传电脑中的文件至 u 盘</li>
 * </ul>
 * 问题:
 * 复制大文件时不能阻塞当前线程
 * 需要以较快的速度检测两个文件是否相同,暂时采用的 md5 
 * 大文件在复制一次之后,到下次复制还没有复制完成检测到 md5 不同,又复制了一次
 * 文件占用的问题,无法修改 : 这个流未关闭
 * 扫描速度太快,刚新建的文本文档就复制过云了,写了内容后又复制了一次
 * 文件夹深度太深导致复制问题,有阻塞
 */
public class Sync {
	private File companyFolder;
	private File uFolder;
	
	private final static Log LOGGER = LogFactory.getLog(Sync.class);
	
	private final static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	
	public Sync(File companyFolder,File uFolder){
		if(companyFolder == null || uFolder == null || !companyFolder.isDirectory() || !uFolder.isDirectory()){
			return ;
		}
		this.companyFolder = companyFolder;
		this.uFolder = uFolder;
		
//		syncFolder(this.companyFolder,this.uFolder);
		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("同步一次文件夹"+DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
				syncFolder(Sync.this.companyFolder, Sync.this.uFolder);
			}
		}, 100, 60000, TimeUnit.MILLISECONDS);
	}
	/*
	 * 作者:sanri<br/>
	 * 时间:2016-9-18上午10:52:50<br/>
	 * 功能:比较两个文件是否相同</br/>
	 * 入参:源文件,目标文件</br/>
	 * 出参:是否相同</br/>
	 */
	private boolean compare(File src,File desc){
		if(getFileMd5(src).equals(getFileMd5(desc))){
			return true;
		}
		return false;
	}
	
	private static String getFileMd5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[8192];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			BigInteger bigInt = new BigInteger(1, digest.digest());
			return bigInt.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	/*
	 * 作者:sanri<br/>
	 * 时间:2016-9-18上午10:05:36<br/>
	 * 功能:同步两个目录中的文件 </br/>
	 * 入参:源目录和目标目录第一个指电脑文件夹,第二个指 u 盘文件夹  </br/>
	 */
	private void syncFolder(final File src,final File desc){
		//任何一端目录或文件不存在,都是创建目录或者复制文件 
		if(!src.exists()){
			LOGGER.info("创建目录:"+src);
			src.mkdir();
		}
		if(!desc.exists()){
			LOGGER.info("创建目录:"+desc);
			desc.mkdir();
		}
		
		//先从 u 盘同步到电脑
		new Thread(){
			public void run() {
				File[] descFiles = desc.listFiles();
				
				if(descFiles != null && descFiles.length > 0){
					for (File file : descFiles) {
						if(file.isDirectory()){
							syncFolder(new File(src,file.getName()),file);
							continue;
						}
						if(file.isFile()){
							syncFile(new File(src,file.getName()),file);
						}
					}
				}
			}
		}.start();
		//再从电脑同步到 u 盘
		new Thread(){
			public void run() {
				File[] srcFiles = src.listFiles();

				if(srcFiles != null && srcFiles.length > 0){
					for (File file : srcFiles) {
						if(file.isDirectory()){
							syncFolder(file,new File(desc,file.getName()));
							continue;
						}
						if(file.isFile()){
							syncFile(file,new File(desc,file.getName()));
						}
					}
				}
			}
		}.start();
	}
	/*
	 * 作者:sanri<br/>
	 * 时间:2016-9-18上午10:13:44<br/>
	 * 功能:同步两个文件 </br/>
	 * 入参:源文件和目标文件 desc 指 u 盘上的文件 </br/>
	 */
	private void syncFile(File src,File desc){
		try {
			if(!src.exists()){
//				IOUtils.copy(new FileInputStream(desc), new FileOutputStream(src));
				new CopyThread(desc,src).start();
				return ;
			}
//			IOUtils.copy(new FileInputStream(src), new FileOutputStream(desc));
			if(!compare(src, desc)){
//				System.out.println("从 "+src+" 复制到 "+desc);
				LOGGER.info("从 "+src+" 复制到 "+desc);
				new CopyThread(src, desc).start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class CopyThread extends Thread{
		private File src;
		private File desc;
		
		public CopyThread(File src,File desc){
			this.src = src;
			this.desc = desc;
		}
		@Override
		public void run() {
			FileInputStream fileInputStream = null;
			FileOutputStream fileOutputStream = null;
			try {
				fileInputStream = new FileInputStream(src);
				fileOutputStream = new FileOutputStream(desc);
				IOUtils.copy(fileInputStream,fileOutputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				IOUtils.closeQuietly(fileInputStream);
				IOUtils.closeQuietly(fileOutputStream);
			}
		}
	}
	
	public static void main(String[] args) {
		new Sync(new File("e:/sync"), new File("g:/sync"));
	}
}
