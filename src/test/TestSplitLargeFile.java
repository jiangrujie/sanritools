package test;

import java.io.File;

import org.junit.Test;

import com.sanri.file.split.SplitLargeFile;

import junit.framework.TestCase;

public class TestSplitLargeFile extends TestCase{
	
	@Test
	public void testSplit(){
		SplitLargeFile splitLargeFile = new SplitLargeFile("d:/test.txt");
		File outdir = new File("d:/split");
		splitLargeFile.setOutputDir(outdir);
//		splitLargeFile.setSingleFileSize(1468);//每个文件 100M
		splitLargeFile.setFileListSize(10);
		splitLargeFile.startSplit();
	}
}
