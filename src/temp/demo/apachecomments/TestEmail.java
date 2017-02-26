package temp.demo.apachecomments;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class TestEmail extends TestCase{

	/**
	 * @param args
	 */
	private String emailServer = "smtp.163.com";
	private String userName = "ningxiangsanri@163.com";
	private String password = "h196944";
	private String emailEncoding = "GBK"; // Email编码

	/**
	 * 测试发送不包含附件的简单纯文本邮件
	 * 
	 * @throws EmailException
	 */
	@Test
	public void testSimpleEmail() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setHostName(emailServer); // 服务器名
		email.setAuthentication(userName, password); // 用户名，密码
		email.setCharset(emailEncoding); // 邮件编码方式

		email.addTo("2441719087@qq.com", "收信,这个名字啥意思"); // 收信人
		email.setFrom("ningxiangsanri@163.com", "发信人是谁"); // 发信人
		email.setSubject("主题"); // 标题
		email.setMsg("这是内容主体了吧"); // 正文

		email.send(); // 发送
	}

	/**
	 * 测试发送包含附件的邮件
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws EmailException
	 * @throws MalformedURLException
	 */
	@Test
	public void testMultiPartEmail() throws UnsupportedEncodingException, EmailException, MalformedURLException {
		// 本地附件
		EmailAttachment att1 = new EmailAttachment();
		att1.setPath("e:/sanri-tools.log"); // 附件原始路径
		att1.setDisposition(EmailAttachment.ATTACHMENT);
		// 是否乱码无法测试，若乱码可参考下一句解决方法
		att1.setDescription("第三版-鸟哥的Linux私房菜.chm 附件描述"); // 附件描述
		// 防止附件名乱码
		att1.setName(MimeUtility.encodeText("第三版-鸟哥的Linux私房菜.chm")); // 附件名称

		// 网络附件
		EmailAttachment att2 = new EmailAttachment();
		att2.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
		att2.setDisposition(EmailAttachment.ATTACHMENT);
		att2.setDescription("attachemnt description logo 中文");
		att2.setName(MimeUtility.encodeText("logo 中文.gif"));

		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(emailServer);
		email.setAuthentication(userName, password);
		email.setCharset(emailEncoding);

		email.addTo("2441719087@qq.com", "收信,这个名字啥意思"); // 收信人
		email.setFrom("ningxiangsanri@163.com", "发信人是谁"); // 发信人
		email.setSubject("主题"); // 标题
		email.setMsg("这是内容主体了吧"); // 正文
		email.attach(att1); // 发送
		email.attach(att2);

		email.send();
	}

	/**
	 * 测试发送HTML格式的邮件，经测试发现：用到的图片在QQ的邮箱界面中没有附件的图标
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws EmailException
	 * @throws MalformedURLException
	 */
	@Test
	public void testHtmlEmail() throws UnsupportedEncodingException, EmailException, MalformedURLException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(emailServer);
		email.setAuthentication(userName, password);
		email.setCharset(emailEncoding);

		email.addTo("2441719087@qq.com", "收信,这个名字啥意思"); // 收信人
		email.setFrom("ningxiangsanri@163.com", "发信人是谁"); // 发信人
		email.setSubject("主题"); // 标题

		// 本地图片
//		File file = new File("C:/测试邮件.pdf");
//		String cid1 = email.embed(file, "测试邮件.pdf");
		// 网络图片
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		String cid2 = email.embed(url, "logo 中文.gif");

		email.setHtmlMsg("pretty gril - " + "" + "The apache logo - " + "");
		email.setTextMsg("Your email client does not support HTML messages");

		email.send();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestEmail test = new TestEmail();
		try {
			test.testSimpleEmail();
			test.testMultiPartEmail();
			test.testHtmlEmail();
			System.out.println("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}