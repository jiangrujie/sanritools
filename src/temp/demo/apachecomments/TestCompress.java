package temp.demo.apachecomments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import junit.framework.TestCase;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;

/**
 * 
 * 创建时间:2016-10-1下午11:27:47<br/>
 * 创建者:sanri<br/>
 * 功能: apache commons compress 包的使用<br/>
 * 可以压缩/解压缩的文件包括<br/>
 * <ul>
 * 	<li>gzip</li>
 * 	<li>bzip2</li>
 * 	<li>zip</li>
 * </ul>
 * 如果不需要这么复杂的功能,则可以选用本类提供的只能进行 zip 的压缩和解压缩方法<br/>
 */
public class TestCompress extends TestCase{
	CompressorStreamFactory factory = new CompressorStreamFactory();
	
	@Test
	public void testGzipIn(){
		InputStream gzipInputStream = null;
		InputStream bzip2InputStream = null;
		try {
			CompressorInputStream gzipIn = factory.createCompressorInputStream("gz",gzipInputStream);
			CompressorInputStream bzip2In = factory.createCompressorInputStream("bzip2",bzip2InputStream);
		} catch (CompressorException e) {
			e.printStackTrace();
		} finally{
			try {
				gzipInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testGzipOut(){
		OutputStream gzipOutputStream = null;
		OutputStream bzip2OutputStream = null;
		try {
			CompressorOutputStream gzipOut = factory.createCompressorOutputStream("gz",gzipOutputStream);
			CompressorOutputStream bzip2Out = factory.createCompressorOutputStream("bzip2",bzip2OutputStream);
		} catch (CompressorException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 执行压缩操作
	 * 
	 * @param srcPathName 被压缩的文件/文件夹
	 */
	public static void compressExe(String srcPathName, String localPath,String zipFileName) {
		File file = new File(srcPathName);
		if (!file.exists()) {
			throw new RuntimeException(srcPathName + "不存在！");
		}
		try {
			File zipFileDir = new File(localPath);
			if (!zipFileDir.exists()) {
				zipFileDir.mkdirs();
			}
			File zipFile = new File(zipFileDir, zipFileName);
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compressByType(file, out, basedir);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法
	 * 
	 * @param file
	 * @param out
	 * @param basedir
	 */
	private static void compressByType(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			compressDirectory(file, out, basedir);
		} else {
			compressFile(file, out, basedir);
		}
	}

	/**
	 * 压缩一个目录
	 * 
	 * @param dir
	 * @param out
	 * @param basedir
	 */
	private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists()) {
			return;
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compressByType(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/**
	 * 压缩一个文件
	 * 
	 * @param file
	 * @param out
	 * @param basedir
	 */
	private static void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static final int BUFFER = 8192;
}
