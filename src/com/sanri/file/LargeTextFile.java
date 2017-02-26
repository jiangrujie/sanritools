package com.sanri.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 作者:sanri</br>
 * 时间:2016-9-27下午1:56:38</br>
 * 功能:大文件的处理,仿 linux sed 功能,只能处理静态文件,对于动态的文件需重新初始化<br/>
 * 功能:<br/>
 * <ul>
 * </ul>
 */
public class LargeTextFile extends File{
	private RandomAccessFile accessFile;
	private FileChannel fileChannel;
	private Log logger = LogFactory.getLog(LargeTextFile.class);
	private long count;		//文件的行数
	private Charset charset = Charset.forName("utf-8");
	//每一行的行尾和行首字节标记
	private Map<Long,Long> lineMarkBegin = new HashMap<Long, Long>();
	private Map<Long,Long> lineLengthMark = new HashMap<Long, Long>();
	
	public LargeTextFile(String pathname) throws IOException {
		super(pathname);
		if(!this.exists()){
			throw new FileNotFoundException("文件不存在,请使用普通文件读写功能");
		}
		
		accessFile = new RandomAccessFile(this, "rw");
		fileChannel = accessFile.getChannel();

		//迭代整个文件,而不是把每一行都读出内存,不会占用很大的内存,会占用比较长的时间 
		//3.8G 的文件使用 31438 ms,大概半分钟的时间
		FileReader reader = new FileReader(this);
		long startTime = System.currentTimeMillis();
		LineIterator lineIterator = IOUtils.lineIterator(reader);
		long preLineEnd = 0;
		while(lineIterator.hasNext()){
			String nextLine = lineIterator.next();
			long lineLength = nextLine.getBytes(charset).length + IOUtils.LINE_SEPARATOR.length();
			
			lineMarkBegin.put(count, preLineEnd);
			lineLengthMark.put(count, lineLength ); 	//lineLength 包含了换行符的长度
			
			preLineEnd = preLineEnd + lineLength;
			count++;
		}
		IOUtils.closeQuietly(reader);
		logger.info("读取整个文件花费时间 :"+(System.currentTimeMillis() - startTime) + " ms");
	}
	
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:14:04</br>
	 * 功能:在某行前插入文本</br>
	 */
	public void insert(long current,List<String> lineText){
		
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:14:29</br>
	 * 功能:某行后追加文本</br>
	 */
	public void append(long current,List<String> lineText){
		
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:15:19</br>
	 * 功能:取出某行的内容</br>
	 */
	public String subLine(long lineNum){
		Long begin = lineMarkBegin.get(lineNum);
		Long length = lineLengthMark.get(lineNum);
		if(begin == null || lineNum > count){
			return null;
		}
		try {
			accessFile.seek(begin);
			ByteBuffer buffer = ByteBuffer.allocate(length.intValue());
			fileChannel.read(buffer);
			buffer.flip();
			CharBuffer decode = charset.decode(buffer);
			String lineString = decode.toString();
			
			return lineString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:16:05</br>
	 * 功能:取出行内容</br>
	 */
	public List<String> subLine(long lineBegin,long lineEnd){
		List<String> lineList = new ArrayList<String>();
		for(long i=lineBegin;i<=lineEnd;i++){
			lineList.add(subLine(i));
		}
		return lineList;
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:16:35</br>
	 * 功能:删除开始到结束行数据</br>
	 */
	public void delete(long lineBegin,long lineEnd){
		
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:17:08</br>
	 * 功能:删除行数据</br>
	 */
	public void delete(long lineNum){
		
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:18:04</br>
	 * 功能:取代开始行到结束行的数据</br>
	 */
	public List<String> replace(long lineBegin,long lineEnd,List<String> lineString){
		
		return null;
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-27下午2:19:24</br>
	 * 功能:按照正则表达式取代内容 </br>
	 */
	public void searchReplace(long lineBegin,long lineEnd,String regex,String content){
		
	}
	
	public static void main(String[] args) {
		try {
			LargeTextFile largeTextFile = new LargeTextFile("e:/WindowsUpdate.log");
			List<String> subLine = largeTextFile.subLine(5,6);
			System.out.println(subLine);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
