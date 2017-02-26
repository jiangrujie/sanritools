package temp;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import sanri.utils.StringUtil;

public class FindFile {
	public static void main(String[] args) {
		File dir = new File("E:/doc/project/example/companyProject/kingdom/newFeature/WebRoot");
		try {
			findFile(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void findFile(File dir) throws IOException {
		if(dir.isDirectory()){
			File[] listFiles = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.isDirectory()){
						return true;
					}
					String name = pathname.getName();
					String suffix = StringUtil.suffix(name);
					if(".js".equalsIgnoreCase(suffix)|| ".html".equalsIgnoreCase(suffix)){
						return true;
					}
					return false;
				}
			});
			for (File file : listFiles) {
				if(file.isDirectory()){
					findFile(file);
					continue;
				}
				handlerFile(file);
			}
			return ;
		}
		handlerFile(dir);
	}

	private static void handlerFile(File dir) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(dir);
		List<String> readLines = IOUtils.readLines(fileInputStream);
		if(readLines.size() > 1000){
			System.out.println(dir);;
		}
	}
	
}
