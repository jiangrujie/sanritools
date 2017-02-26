package com.sanri.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFolder{
	
	public static void copyFile(File src,File desc) throws FileNotFoundException, Exception{
		if(src.getName().charAt(0) == '.')return ;
		if(src.isDirectory()){
			File descFolder = new File(desc,src.getName());
			descFolder.mkdirs();
			File[] listFiles = src.listFiles();
			for (File file : listFiles) {
				copyFile(file, descFolder);
			}
		}else{
			copy(new FileInputStream(src), new FileOutputStream(desc.getAbsolutePath()+File.separator+src.getName()), 4096);
		}
	}
	
	public static void copyFile(String src,String desc) throws FileNotFoundException, Exception{
		copyFile(new File(src), new File(desc));
	}
	/**
	 * 复制流
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	protected static boolean copy(InputStream in, OutputStream out,int size) throws Exception {
		// 缓冲输入流
		BufferedInputStream bis = new BufferedInputStream(in);
		// 缓冲输出流
		BufferedOutputStream bos = new BufferedOutputStream(out);
		// 缓冲 1kB
		byte[] buff = new byte[size];
		try {
			int len = 0;
			while ((len = bis.read(buff)) != -1) {
				bos.write(buff,0,len);
				bos.flush();
			}
			bos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			// 关流
			if (null != bis) {
				bis.close();
			}
			if (null != bos) {
				bos.close();
			}
		}
		return false;
	} 
	
	public static void main(String[] args) throws FileNotFoundException, Exception {
		String property = System.getProperty("user.dir");
//		copyFile("F:\\svn\\梅州监狱项目\\branches\\Src\\Wifi2", "d:/");
//		copyFile("F:/svn/sanri-tools", "d:/");
		System.out.println("复制成功");
	}
}