package temp.ftp;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

public class PropFtp {
	public static void main(String[] args) {
		try {
			FtpServerFactory serverFactory = new FtpServerFactory();
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
			userManagerFactory.setFile(new File("users.properties"));
			
			serverFactory.setUserManager(userManagerFactory.createUserManager());
			
			FtpServer server = serverFactory.createServer();
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}
}
