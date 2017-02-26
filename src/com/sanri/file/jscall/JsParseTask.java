package com.sanri.file.jscall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.Validate;

import com.alibaba.fastjson.JSONObject;
import com.sanri.thread.AbstractTask;
import com.sanri.thread.AbstractTaskThread;

/**
 * 
 * 创建时间:2016-10-12下午2:05:35<br/>
 * 创建者:sanri<br/>
 * 功能:根据一个目录创建一个文件解析任务,分多线程来解析 js 文件<br/>
 */
public class JsParseTask extends AbstractTask<File, List<JsFile>>{
	private File dir;			//需要解析的目录
	private Log logger = LogFactory.getLog(JsParseTask.class);
	
	public JsParseTask(){
		//更改下默认设置,默认为 10 个线程,同时运行所有线程
		MAX_THREAD_COUNT = 10;
		CONCURRENT_THREAD = 0;
	}
	
	@Override
	protected AbstractTaskThread<File, List<JsFile>> newInstance() {
		JsFileParseThread jsFileParseThread = new JsFileParseThread();
		return jsFileParseThread;
	}

	@Override
	public List<File> getTaskDataList() {
		List<File> jsFiles = new ArrayList<File>();
		findJsFiles(dir,jsFiles);
		return jsFiles;
	}
	
	/**
	 *  
	 * 功能:查找所有的脚本文件,根据一个目录,什么是脚本文件 @see JsFileFilter<br/>
	 * 创建时间:2016-10-10下午3:50:11<br/>
	 * 作者：sanri<br/>
	 * @param dir<br/>
	 */
	private void findJsFiles(File currentDir, List<File> jsFiles) {
		if(Validate.isEmpty(currentDir)){
			return ;
		}
		if(currentDir.isDirectory()){
			File[] listFiles = currentDir.listFiles(JsFileFilter.JS_FILE_FILTER);
			for (File file : listFiles) {
				if(file.isFile()){
					jsFiles.add(file);
					continue;
				}
				findJsFiles(file,jsFiles);
			}
			return ;
		}
		jsFiles.add(currentDir);
	}
	
	@Override
	public void afterHandle(List<JsFile> out) {
		logger.info(JSONObject.toJSONString(out));
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

}
