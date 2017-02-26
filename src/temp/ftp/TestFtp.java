package temp.ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class TestFtp {
	public static void main(String[] args) {
		try {
			FtpServerFactory ftpServerFactory = new FtpServerFactory();
			FtpServer createServer = ftpServerFactory.createServer();
			
			//添加可写的权限 
			List<Authority> authorities = new ArrayList<Authority>();
			authorities.add(new WritePermission());

			//添加用户
//			BaseUser baseUser = new BaseUser();
//			baseUser.setName("anonymous");
//			baseUser.setHomeDirectory("d:/test");
//			baseUser.setAuthorities(authorities);
			//加一个非匿名的用户
			BaseUser testUser = new BaseUser();
			testUser.setHomeDirectory("e:/soft");
			testUser.setName("sanri");
			testUser.setPassword("h123");
			testUser.setAuthorities(authorities);
			
			ftpServerFactory.getUserManager().save(testUser);
//			ftpServerFactory.getUserManager().save(baseUser);
			
			createServer.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}
}
