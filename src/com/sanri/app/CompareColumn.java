package com.sanri.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

/**
 * 
 * 创建时间:2016-10-8下午5:48:54<br/>
 * 创建者:sanri<br/>
 * 功能:用于字段比较,常用比较文本工具有个问题,就是不能先排序<br/>
 * 简单的比较功能
 */
public class CompareColumn {
	private static File baseDir = new File("D:/test/compare/");
	private static Log LOGGER = LogFactory.getLog(CompareColumn.class);
	/**
	 * 功能:开始比较<br/>
	 * 创建时间:2016-10-8下午5:52:55<br/>
	 * 作者：sanri<br/>
	 */
	public static void startCompare(boolean ignoreCase){
		if(!baseDir.exists()){
			LOGGER.error("比较目录不存在");
			return ;
		}
		File newCol = new File(baseDir,"new.txt");
		File oldCol = new File(baseDir,"old.txt");
		
		InputStreamReader newColIn = null;
		InputStreamReader oldColIn = null;
		try {
			newColIn = new InputStreamReader(new FileInputStream(newCol),"utf-8");
			oldColIn = new InputStreamReader(new FileInputStream(oldCol),"utf-8");
			
			List<String> newLinesAll = IOUtils.readLines(newColIn);
			List<String> oldLinesAll = IOUtils.readLines(oldColIn);
			
			handler(newLinesAll,ignoreCase);handler(oldLinesAll,ignoreCase);
			print(newLinesAll, "新行所有内容");print(oldLinesAll, "旧行所有内容");
			
			List<String> eqLines = new ArrayList<String>();		//相同的列
			List<String> newLines = new ArrayList<String>();	//新文本比旧文本多出的列
			List<String> oldLines = new ArrayList<String>();	//旧文本比新文本多出的列
			
			//找出相同的行
			for (String oldLine: oldLinesAll) {
				if(StringUtil.isBlank(oldLine)){continue;}
				for (String newLine : newLinesAll) {
					if(StringUtil.isBlank(newLine)){continue;}

					if(newLine.equals(oldLine)){
						eqLines.add(newLine);
					}
				}
			}
			
			newLines = (List<String>) CollectionUtils.subtract(newLinesAll, eqLines);
			oldLines = (List<String>) CollectionUtils.subtract(oldLinesAll, eqLines);
			
			print(eqLines, "相同的行");
			print(oldLines, "旧行多出的行");
			print(newLines, "新行多出的行");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(newColIn);
			IOUtils.closeQuietly(oldColIn);
		}
	}
	
	/**
	 * 
	 * 功能:对读到的行进行处理,IOUtils.readLines 空行,空白全读了<br/>
	 * 创建时间:2016-10-8下午10:00:35<br/>
	 * 作者：sanri<br/>
	 * @param ignoreCase 
	 */
	private static void handler(Collection<String> elments, boolean ignoreCase){
		List<String> filterElements = new ArrayList<String>();
		if(!Validate.isEmpty(elments)){
			for (String element : elments) {
				element = element.trim();
				if(StringUtil.isBlank(element)){continue;}
				if(ignoreCase){
					element = element.toLowerCase();
				}
				filterElements.add(element);
			}
		}
		elments.clear();
		elments.addAll(filterElements);
	}
	/**
	 * 功能:打印集合中的数据<br/>
	 * 创建时间:2016-10-8下午10:03:53<br/>
	 * 作者：sanri<br/>
	 */
	private static void print(Collection<String> elments,String mark){
		System.out.println(mark+":");
		System.out.println(elments);
	}
	
	public static void main(String[] args) {
		startCompare(true);
	}
}
