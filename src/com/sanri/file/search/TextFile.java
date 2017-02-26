package com.sanri.file.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

/**
 * 以行为标准,将文本看成是一个一维数组
 * @author sanri
 *
 */
public class TextFile extends File{
	private static final long serialVersionUID = 1L;
	/** the encoding of this text*/
	public String charset = "UTF-8";
	public Charset cs;
	/** The value is used for String storage.the value use lazy load,load when use */
	protected String [] value;
	/**The offset is the first index of the storage that is used. */
	protected long offset;
	/** The count is the number of line in the text. */
	protected long count;
	/** the line byte begin of this text when this text is exist */
	protected Map<Long,Long> lineMarkBegin;
	/** the line byte end of this text when this text is exist */
	protected Map<Long,Long> lineMarkEnd;
	
	
	protected RandomAccessFile accessFile;
	protected FileChannel fileChannel;
	
	public TextFile(String pathname) {
		super(pathname);
		init();
	}
	public TextFile(File file) {
		this(file.getPath());
		init();
	}
	public TextFile(File parent, String child) {
		super(parent, child);
		init();
	}
	public TextFile(String parent, String child) {
		super(parent, child);
		init();
	}
	public TextFile(URI uri) {
		super(uri);
		init();
	}
	/**
	 * 
	 * 功能:初始化读入<br/>
	 * 创建时间:2016-9-25下午3:31:13<br/>
	 * 作者：sanri<br/>
	 */
	protected boolean init() {
		cs = Charset.forName(charset);
		// the file is not exist.may want to generate file
		if(!this.exists()) return false;
		//else the file is exist ,then mark the byte begin and end for every line
		lineMarkBegin = new HashMap<Long, Long>();
		lineMarkEnd = new HashMap<Long, Long>();
		BufferedReader br = null;
		try{
			accessFile = new RandomAccessFile(this,"rw");
			fileChannel = accessFile.getChannel();
			br = new BufferedReader(new FileReader(this));
			long line = 0;
			String content = "";
			long preByte = 0;
			while((content = br.readLine()) != null){
				long contentLength = content.getBytes(charset).length;
				//because readLine method can not read \r\n,so must add 2 to end byte
				long endByte = preByte + contentLength - 1 + 2;
				lineMarkBegin.put(line, preByte);
				lineMarkEnd.put(line, endByte);
				preByte = endByte + 1;
				line ++;
			}
			count = line - 1;
			if(count < 0)count = 0; // where text is empty,not into while count is 0
			//because the last line not \r\n so the last mark must sub 2 when the last line is exist
			if(!Validate.isEmpty(lineMarkEnd)){
				Long lastLineEnd = lineMarkEnd.get(count) - 2;
				lineMarkEnd.put(count, lastLineEnd);
			}
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 加载全部内容
	 * @param filter
	 * @throws FileNotFoundException
	 */
	public String [] loadValue(StringFilter filter) throws FileNotFoundException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(this));
			String readLine = "";
			List<String> list = new ArrayList<String>();
			while((readLine = br.readLine()) != null){
				if(filter != null){
					readLine = filter.handle(readLine);
					if(filter.accept(readLine)){
						list.add(readLine);
					}
				}
				list.add(readLine);
			}
			if(this.value == null)
				this.value = new String[list.size()];
			list.toArray(this.value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.value;
	}
	/**
	 * position spec line
	 * @param line
	 * return the begin of byte of this line
	 */
	public long seekLine(long line) {
		return this.position(line, 0);
	}
	/**
	 * position spec line and col
	 * @param line
	 * @param col
	 * @return
	 */
	public long position(long line,long col){
		if(line > count || line < 0){
			throw new IndexOutOfBoundsException(String.valueOf(line));
		}
		this.offset = line;
		long byteBegin = lineMarkBegin.get(new Long(line));
		long byteEnd = lineMarkEnd.get(new Long(line));
		long newPosition = byteBegin + col;
		if(newPosition > byteEnd){
			throw new IndexOutOfBoundsException(String.valueOf(byteEnd - newPosition)); 
		}
		this.seek(newPosition);
		return newPosition;
	}
	/**
	 * position of byte
	 */
	public long seek(long position){
		try {
			accessFile.seek(position);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return position;
	}
	/**
	 * return the line begin byte
	 * @param line
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public long lineBeginByte(long line){
		if(line > count || line < 0){
			throw new IndexOutOfBoundsException(String.valueOf(line));
		}
		return lineMarkBegin.get(new Long(line));
	}
	/**
	 * return the line end byte
	 * @param line
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public long lineEndByte(long line){
		if(line > count || line < 0){
			throw new IndexOutOfBoundsException(String.valueOf(line));
		}
		return lineMarkEnd.get(new Long(line));
	}
	/**
	 * begin offset and read the text of offset line
	 * return null when EOF
	 * @return
	 * @throws IOException 
	 */
	public String readLine() throws IOException{
		if(this.offset > count){
			//return null when the last line
			return null;
		}
		long beginByte = lineMarkBegin.get(this.offset);
		long endByte = lineMarkEnd.get(this.offset);	//every line not last have \r\n
		ByteBuffer buffer = ByteBuffer.allocate((int)(endByte - beginByte + 1));
		fileChannel.read(buffer);
		buffer.flip();
		CharBuffer decode = cs.decode(buffer);
		String lineString = decode.toString();
		if(offset != count){
			//TODO \r\n 有时可能只写了一个 - 2 可能会越界 这个以后解决 2016/09/27
			lineString = lineString.substring(0,lineString.length());
		}
		offset ++;
		return lineString;
	}
	/**
	 * 
	 * Returns a  string array that is a subline of this text. The
     * subline begins at the specified <code>beginIndex</code> and
     * extends to the String at index <code>endIndex - 1</code>.
     * Thus the length of the subline is <code>endIndex-beginIndex</code>.
     * <p>
     * Examples:
     * <blockquote><pre>
     * "hamburger".substring(4, 8) returns "urge"
     * "smiles".substring(1, 5) returns "mile"
     * </pre></blockquote>
     *
     * @param      beginIndex   the beginning index, inclusive.
     * @param      endIndex     the ending index, exclusive.
     * @return     the specified substring.
     * @exception  IndexOutOfBoundsException  if the
     *             <code>beginIndex</code> is negative, or
     *             <code>endIndex</code> is larger than the length of
     *             this <code>String</code> object, or
     *             <code>beginIndex</code> is larger than
     *             <code>endIndex</code>.
	 */
	public String[] subline(long beginIndex,long endIndex){
		if (beginIndex < 0) {
		    throw new IndexOutOfBoundsException(String.valueOf(beginIndex));
		}
		if (endIndex > count) {
		    throw new IndexOutOfBoundsException(String.valueOf(endIndex));
		}
		if (beginIndex > endIndex) {
		    throw new IndexOutOfBoundsException(String.valueOf(endIndex - beginIndex));
		}
		try {
			if((beginIndex == 0) && (endIndex == count)) return this.loadValue(null);
			this.seekLine(beginIndex);
			int loop = (int) (endIndex - beginIndex);
			if(endIndex == count) loop += 1;//the last line 
			String [] lineArray = new String[loop];
			for(int i = 0;i < loop;i++){
				lineArray[i] = this.readLine();
			}
			return lineArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * the endIndex is the count of line of this text
	 * @see subline(beginIndex,endIndex)
	 * @param beginIndex
	 * @return
	 */
	public String [] subline(long beginIndex){
		return this.subline(beginIndex, this.count);
	}
	
    /**
     * Returns the lines of this text.
     */
    public long lineCount() {
        return count;
    }
    
    public void close(){
    	if(fileChannel != null){
    		try {
				fileChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    /**
     * insert a string to current offset.Status of insert or rewrite is decided by the variable insert 
     * @param content
     * @param insert true is status of rewrite
     * @return
     */
    public String insertString(String content,boolean insert){
    	if(!StringUtil.isBlank(content)){
			try {
				if(!this.exists()){
					this.createNewFile();
					init();
				}
				/*if not the status of rewrite then copy after offset of string and 
				* append to after when write the content
				*/
				long position = accessFile.getFilePointer(); //remember pre position
				String[] after = this.subline(this.offset);
				accessFile.seek(position);
				writeString(content);
				if(!insert){
					accessFile.seek(position + content.getBytes(this.charset).length);
					for (int i=0;i<after.length;i++) {
						writeString(after[i]);
						if(i != after.length - 1)
							writeString("\r\n");
					}
				}
				return content;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return "";
    }
	private void writeString(String string)
			throws UnsupportedEncodingException, IOException {
		byte[] bytesContent = string.getBytes(this.charset);
		ByteBuffer buff = ByteBuffer.allocate(bytesContent.length);
		buff.put(bytesContent);
		buff.flip();
		fileChannel.write(buff);
	}
    public String insertString(String content){
    	return this.insertString(content, false);
    }
    public interface StringFilter{
		/**
		 * 接受什么样的字符串
		 * @param str
		 * @return
		 */
		boolean accept(String str);
		/**
		 * 怎么处理当前读到的字符串,之后会调用是否接受
		 * @param str
		 * @return
		 */
		String handle(String str);
	}
}
