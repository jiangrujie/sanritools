package test.mini;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import sanri.utils.PathUtil;

public class T2 {
	public static void main(String[] args) throws URISyntaxException, MalformedURLException {
		File file =new File(PathUtil.pkgPath("sanri.utils") + File.separator+"StringUtil.class");
		String absolutePath = file.getAbsolutePath();
		System.out.println(absolutePath);
		URI uri = new URL("/"+absolutePath).toURI();
//		URL uri2 = PathUtil.URL;
		System.out.println(uri);
//		System.out.println(uri2);
	}
}
