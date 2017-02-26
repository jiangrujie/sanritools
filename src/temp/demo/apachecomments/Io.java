package temp.demo.apachecomments;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.IOUtils;

public class Io {
	public static void main(String[] args) {
		long freeSpaceKb;
		try {
			freeSpaceKb = FileSystemUtils.freeSpaceKb("c:/");
			System.out.println(freeSpaceKb);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 作者:sanri<br/>
	 * 时间:2016-9-13下午4:51:27<br/>
	 * 功能:读取一个文件形成 List</br/>
	 */
	public void readLines() throws FileNotFoundException, IOException{
		@SuppressWarnings("unchecked")
		List<String> readLines = IOUtils.readLines(new FileReader("D:\\collect\\collect.properties"));
		for (String string : readLines) {
			System.out.println(string);
		}
	}
	
	/**
	 * 
	 * 作者:sanri<br/>
	 * 时间:2016-9-13下午4:56:58<br/>
	 * 功能:复制两个流,比我写的方便多了</br/>
	 */
	public void copy(){
//		IOUtils.copy(input, output);
	}
	
}
