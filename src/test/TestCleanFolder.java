package test;

import org.junit.Test;

import com.sanri.app.ProjectFolderClean;

import junit.framework.TestCase;

public class TestCleanFolder extends TestCase {

	@Test
	public void cleanTest(){
		ProjectFolderClean folderClean = new ProjectFolderClean();
		folderClean.setCleanDir("E:\\doc\\cleantest");
		folderClean.setTempDir("E:\\doc\\cleantesttemp");
		folderClean.addDirPattern("\\.settings");
		folderClean.addDirPattern("target");
		folderClean.addDirPattern("bin");
		folderClean.addDirPattern("\\.myeclipse");
		folderClean.addFilePattern("\\.jar");
		
		folderClean.startClean();
	}
	
	public static void main(String[] args) {
		ProjectFolderClean folderClean = new ProjectFolderClean();
		folderClean.setCleanDir("E:\\doc\\project\\webbackback");
		folderClean.setTempDir("E:\\doc\\project/webback");
//		folderClean.addDirPattern("\\.settings");
//		folderClean.addDirPattern("target");
//		folderClean.addDirPattern("bin");
//		folderClean.addDirPattern("\\.myeclipse");
//		folderClean.addDirPattern("META-INF");
//		folderClean.addDirPattern("classes");
		folderClean.addDirPattern("\\.svn");
		
//		folderClean.addFilePattern("\\.jar");
//		folderClean.addFilePattern("\\.classpath");
//		folderClean.addFilePattern("\\.mymetadata");
//		folderClean.addFilePattern("\\.project");
		
		folderClean.startClean();
	}
}
