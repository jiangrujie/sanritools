package com.sanri.file.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

import com.sanri.thread.AbstractTaskThread;

public class SearchThread extends AbstractTaskThread<TextFile, List<Find>> implements Callable<List<Find>> {
	private List<Pattern> patterns;
	private long up = 10;
	private long down = 10;
	
	@Override
	public List<Find> call() throws Exception {
		List<Find> finds = null;
		if(!Validate.isEmpty(this.taskDataList)){
			finds = new ArrayList<Find>();
			try{
				for (TextFile file : this.taskDataList) {
					List<Find> handleFile = handleFile(file);
					finds.addAll(handleFile);
					file.close();
				}
			}catch(Exception e){
				throw e;
			}
		}
		return finds;
	}
	private List<Find> handleFile(TextFile file) throws IOException {
		List<Find> finds = new ArrayList<Find>();
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		if(!Validate.isEmpty(patterns)){
			String content = "";
			long line = 0;
			while((content = br.readLine() ) != null){
				for (Pattern pattern : patterns) {
					StringBuffer matcherStr = new StringBuffer();
					Matcher matcher = pattern.matcher(content);
					while (matcher.find()) {
						matcherStr.append(",").append(matcher.group(0));
					}
					if(!StringUtil.isBlank(matcherStr.toString())){
						//有匹配才加入
						matcherStr = new StringBuffer("["+matcherStr.substring(1)+"]");
						//查找上下文 ,对于每一行记录
						long beginIndex = line - up;
						if(beginIndex < 0)beginIndex = 0;
						long endIndex = line + down;
						if(endIndex > file.lineCount()) endIndex = file.lineCount();
						String [] contextArray = file.subline(beginIndex, endIndex);
						String context = StringUtil.join(contextArray, "");
							
						Find find = new Find();
						find.setFile(file);
						find.setLine(line);
						find.setInterest(pattern.pattern());
						find.setMark(matcherStr.toString());
						find.setContext(context);
						
						finds.add(find);
					}
					
				}
				line ++;
			}
			
		}
		return finds;
	}
	public List<Pattern> getPatterns() {
		return patterns;
	}
	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}
	public long getUp() {
		return up;
	}
	public void setUp(long up) {
		this.up = up;
	}
	public long getDown() {
		return down;
	}
	public void setDown(long down) {
		this.down = down;
	}

}
