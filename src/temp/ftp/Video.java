package temp.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.usermanager.impl.BaseUser;

public class Video {
	public static void main(String[] args) {
		try {
			FtpServerFactory ftpServerFactory = new FtpServerFactory();
			FtpServer createServer = ftpServerFactory.createServer();
			//加一个非匿名的用户
			BaseUser testUser = new BaseUser();
			testUser.setHomeDirectory("e:/video");
			testUser.setName("sanri");
			testUser.setPassword("h123");
			
			ftpServerFactory.getUserManager().save(testUser);
			
			createServer.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}
}
