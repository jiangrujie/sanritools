package temp.demo.apachecomments;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import sanri.utils.PathUtil;

public class TestXml {
	public static void main(String[] args) {
		String pkgPath = PathUtil.pkgPath("temp");
		try {
			Configuration configuration = new XMLConfiguration(pkgPath+"/test2.xml");
			String string = configuration.getString("Url");
			System.out.println(string);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}
