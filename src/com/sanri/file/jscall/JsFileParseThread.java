package com.sanri.file.jscall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sanri.utils.Validate;

import com.sanri.thread.AbstractTaskThread;
/**
 * 
 * 创建时间:2016-10-12下午2:12:19<br/>
 * 创建者:sanri<br/>
 * 功能:对分出来的多个 js 文件的解析线程<br/>
 */
public class JsFileParseThread extends AbstractTaskThread<File, List<JsFile>>{

	@Override
	public List<JsFile> call() throws Exception {
		List<JsFile> jsFiles = new ArrayList<JsFile>();
		if(!Validate.isEmpty(this.taskDataList)){
			for (File currentFile : taskDataList) {
				JsFile jsFile = new JsFile(currentFile);
				jsFile.parse();
				jsFiles.add(jsFile);
			}
		}
		return jsFiles;
	}

}
