package com.sanri.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.Validate;

/**
 * 
 * 创建时间:2016-9-30下午9:15:12<br/>
 * 创建者:sanri<br/>
 * 功能:项目目录清理<br/>
 * 思路:查找到文件或目录先复制到临时文件夹,避免误删不能恢复,然后手动删除整个临时文件夹
 */
public class ProjectFolderClean {
	private File cleanDir;		//需要清理的目录
	private File tempDir;		//复制到临时目录
	private List<Pattern> dirPattern = new ArrayList<Pattern>();	//匹配目录(正则)
	private List<Pattern> filePattern = new ArrayList<Pattern>();	//匹配文件(正则)
	private File listFile ;
	
	private Log logger = LogFactory.getLog(ProjectFolderClean.class);
	private String timeFormat = "yyyy-MM-dd HH:mm:ss";
	public void startClean(){
		if(!tempDir.exists()){
			logger.info("创建目录:"+tempDir);
			tempDir.mkdirs();
		}
		listFile = new File(tempDir,"listFile.txt");
		try {
			logger.info("创建文件:"+listFile);
			listFile.createNewFile();
			long startTime = System.currentTimeMillis();
			logger.info("开始清理:"+DateFormatUtils.format(startTime, timeFormat));
			findCleanObject(cleanDir, this.tempDir);
			logger.info("清理结束,花费时间:"+(System.currentTimeMillis() - startTime ) + " ms");
			logger.info("清单文件路径:"+listFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 功能:查找并清理匹配对象,不能一次性查找然后清理,因为有文件夹需整个复制<br/>
	 * 创建时间:2016-9-30下午9:29:00<br/>
	 * 作者：sanri<br/>
	 * @throws IOException 
	 */
	public void findCleanObject(File dir,File tempDir) throws IOException{
		if(dir == null || !dir.exists()){
			return ;
		}
		String fileName = dir.getName();
		if(dir.isDirectory()){
			if(".metadata".equalsIgnoreCase(fileName)){
				logger.info("过滤例外文件夹 :"+fileName);
				return ;
			}
			for(Pattern pattern:dirPattern){
				if(pattern.matcher(fileName).find()){
					//需要清理的目录不再继续递归,直接清理 
					logger.info("清理文件夹:"+dir);
					//记录清理清单
					FileUtils.writeStringToFile(listFile, "文件夹:"+dir.toString()+"\n", "utf-8", true);
					copyFolder(dir,tempDir);
					FileUtils.deleteDirectory(dir);
					return ;
				}
			}
			
			File destDir = new File(tempDir,fileName);
			if(!destDir.exists()){
				destDir.mkdir();
			}
			//查看子文件夹是否需要清理 
			File[] listFiles = dir.listFiles();
			if(!Validate.isEmpty(listFiles)){
				for (File file : listFiles) {
					findCleanObject(file, destDir);
				}
			}
		}else if(dir.isFile()){
			for(Pattern pattern:filePattern){
				if(pattern.matcher(fileName).find()){
					logger.info("清理文件:"+dir);
					//记录清理清单
					FileUtils.writeStringToFile(listFile, "文件:"+dir.toString()+"\n", "utf-8", true);
					copyFile(dir, tempDir);
					dir.deleteOnExit();
				}
			}
		}
	}
	/**
	 * 
	 * 功能:复制目录到目录,成为目标目录的子目录<br/>
	 * 创建时间:2016-9-30下午9:50:59<br/>
	 * 作者：sanri<br/>
	 */
	private void copyFolder(File src,File dest){
		if(src != null && src.exists() && src.isDirectory()){
			File destDir = new File(dest,src.getName());
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			File[] listFiles = src.listFiles();
			if(!Validate.isEmpty(listFiles)){
				for (File file : listFiles) {
					if(file.isDirectory()){
						copyFolder(file, destDir);
						continue;
					}
					copyFile(file, destDir);
				}
			}
		}
	}
	/**
	 * 
	 * 功能:复制文件到目录<br/>
	 * 创建时间:2016-9-30下午9:51:11<br/>
	 * 作者：sanri<br/>
	 */
	private void copyFile(File src,File dest){
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			if(!dest.exists()){
				dest.mkdirs();
			}
			File destFile = new File(dest,src.getName());
			in = new FileInputStream(src);
			out = new FileOutputStream(destFile);
			IOUtils.copy(in,out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
	
	/**
	 * 
	 * 功能:增加目录匹配规则<br/>
	 * 创建时间:2016-9-30下午9:28:19<br/>
	 * 作者：sanri<br/>
	 */
	public void addDirPattern(String pattern){
		if(dirPattern == null){
			dirPattern = new ArrayList<Pattern>();
		}
		dirPattern.add(Pattern.compile(pattern));
	}
	/**
	 * 
	 * 功能:增加文件匹配规则<br/>
	 * 创建时间:2016-9-30下午9:28:33<br/>
	 * 作者：sanri<br/>
	 */
	public void addFilePattern(String pattern){
		if(filePattern == null){
			filePattern = new ArrayList<Pattern>();
		}
		filePattern.add(Pattern.compile(pattern));
	}
	public File getCleanDir() {
		return cleanDir;
	}
	public void setCleanDir(String cleanDir) {
		this.cleanDir = new File(cleanDir);
	}
	public File getTempDir() {
		return tempDir;
	}
	public void setTempDir(String tempDir) {
		this.tempDir = new File(tempDir);
	}
	public File getListFile() {
		return listFile;
	}
	public void setListFile(File listFile) {
		this.listFile = listFile;
	}
	
}
