package com.sanri.file.split;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.StringUtil;

/**
 * 
 * 作者:sanri</br>
 * 时间:2016-9-27下午4:03:00</br>
 * 功能:大文件分割,可以设置分割出多少个文件或设置每个文件大小,将计算出分割任务,然后分割<br/>
 */
public class SplitLargeFile extends File {
	private int fileListSize = 0;
	private long singleFileSize = 0;		//单个文件大小,以字节为单位;设置 0 表示不限制此参数,当两个参数都设置时以单文件最大为准
	private File outputDir;
	private Charset charset = Charset.forName("utf-8");
	private boolean lineRequire = false;	//是否整行分割,当为 true 时,如果找到的指针处不是换行符,将继续向后查找,直到找到换行为止,下个分隔点从换行后开始
	private List<SplitPosition> splitPositions ;
	private SplitFileRenamePolicy fileRenamePolicy;
	private Log logger = LogFactory.getLog(SplitLargeFile.class);
	private final static int THREAD_LIMIT = 100;		//线程数限制 100 线程之内
	
	private static ExecutorService executorService;
    static{
        if(executorService == null){
            executorService = Executors.newCachedThreadPool();
        }
    }
	
	public SplitLargeFile(String pathname) {
		super(pathname);
		if(!this.exists()){
			throw new IllegalArgumentException("找不到要分割的文件");
		}
	}
	
	/**
	 * 作者:sanri</br>
	 * 时间:2016-9-28下午4:27:41</br>
	 * 功能:开始分割</br>
	 */
	public void startSplit(){
		if(outputDir == null){
			throw new IllegalArgumentException("没有输出目录");
		}
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		try {
			long length = this.length();
			if(fileListSize == 0 && singleFileSize == 0){
				throw new IllegalArgumentException("非法参数,必须设置分割单文件大小或分割文件数量");
			}
			if(singleFileSize != 0){
				fileListSize = (int) (length / singleFileSize + 1);
			}else{
				singleFileSize = length / fileListSize;
			}
			if(fileListSize > THREAD_LIMIT){
				throw new IllegalArgumentException("线程数超过指定值,将单文件大小增大以减少线程数,建议单文件大小 :"+(length / (THREAD_LIMIT -1)));
			}
			splitPositions = new ArrayList<SplitPosition>(fileListSize);
			if(lineRequire){
				//计算每个文件需要读到什么位置 //TODO 暂时不实现
			}else{
				for (int i = 0; i < fileListSize; i++) {
					if(i == fileListSize - 1){
						splitPositions.add(new SplitPosition(i* singleFileSize, length - i * singleFileSize));
						break;
					}
					splitPositions.add(new SplitPosition(i * singleFileSize, singleFileSize));
				}
			}
			List<Future<File>> futures = new ArrayList<Future<File>>();
			for (int i=0;i<splitPositions.size();i++) {
				SplitPosition splitPosition = splitPositions.get(i);
				SplitThread splitThread = new SplitThread(this);
				splitThread.setCharset(charset);
				splitThread.setSplitPosition(splitPosition);
				splitThread.setOutputDir(outputDir);
				splitThread.setIndex(i);
				splitThread.setFileRenamePolicy(fileRenamePolicy == null ? FILE_RENAME_POLICY : fileRenamePolicy);
				Future<File> submit = executorService.submit(splitThread);
				futures.add(submit);
			}
			//输出结果
			Map<Future<File>,Boolean> doneList = new HashMap<Future<File>, Boolean>();
			while(doneList.size() != futures.size()){	//一遍一遍的去看所有任务,当有任务完成了就标记完成,直到所有任务完成后才退出
				for (Future<File> future : futures) {
					if (future.isDone() && doneList.get(future) == null) {
						File out = future.get(); // 这个会一直阻塞,直到有结果返回
						logger.info("分割完成,文件路径为:" + out);
						doneList.put(future, true);
					}
				}
			}
			//关闭 线程服务
			if(executorService != null){
                executorService.shutdown();
            }
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public int getFileListSize() {
		return fileListSize;
	}

	public void setFileListSize(int fileListSize) {
		this.fileListSize = fileListSize;
	}

	public long getSingleFileSize() {
		return singleFileSize;
	}

	public void setSingleFileSize(long singleFileSize) {
		this.singleFileSize = singleFileSize;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean isLineRequire() {
		return lineRequire;
	}

	public void setLineRequire(boolean lineRequire) {
		this.lineRequire = lineRequire;
	}

	public SplitFileRenamePolicy getFileRenamePolicy() {
		return fileRenamePolicy;
	}

	public void setFileRenamePolicy(SplitFileRenamePolicy fileRenamePolicy) {
		this.fileRenamePolicy = fileRenamePolicy;
	}
	
	public final static SplitFileRenamePolicy FILE_RENAME_POLICY = new DefaultRenamePolicy();
	static class DefaultRenamePolicy implements SplitFileRenamePolicy{
		@Override
		public String handle(int index, File largefile,long startPosition,long fileSize) {
			String name = largefile.getName();
			String prefix = StringUtil.prefix(name);
			String suffix = StringUtil.suffix(name);
			return prefix+"_"+index+"_"+startPosition+"-"+(fileSize - 1)+suffix;
		}
		
	}
}
