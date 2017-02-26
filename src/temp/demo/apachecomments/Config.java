package temp.demo.apachecomments;

import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
/**
 * 
 * 创建时间:2016-10-3下午4:16:19<br/>
 * 创建者:sanri<br/>
 * 功能:<br/>
 * 注: properties 配置会默认以 , 分隔;所以文本中有逗号的需要转义
 */
public class Config {
	public static void main(String[] args) {
		//默认指向 classpath 目录 
		try {
			Configuration configuration = new PropertiesConfiguration("sanri/codegenerate/jdbc.properties");
			String user = configuration.getString("user");
			System.out.println(user);
			String string = configuration.getString("user2", "1000");
			System.out.println(string);
			
			XMLConfiguration xonConfiguration = new XMLConfiguration();
			XMLConfiguration subset = (XMLConfiguration) xonConfiguration.subset("");
			Node root = subset.getRoot();
			List children = root.getChildren();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}
