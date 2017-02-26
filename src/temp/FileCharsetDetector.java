package temp;

import java.io.BufferedInputStream;
import java.net.URL;

import org.mozilla.intl.chardet.HtmlCharsetDetector;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

public class FileCharsetDetector {

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out.println("usage:Main url <int>lang");
			return;
		}
		int lang = (args.length == 2) ? Integer.parseInt(args[1])
				: nsPSMDetector.ALL;
		// 实现nsICharsetDetectionObserver接口，这个接口只有一个Notify()方法.
		// 当jchardet引擎自己认为已经识别出字符串的字符集后(不论识别的对错)，都会调用这个Notify方法。
		nsICharsetDetectionObserver cdo = new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				HtmlCharsetDetector.found = true;
				System.out.println("CHARSET = " + charset);
			}
		};
		/**
		 * 初始化nsDetector() lang为一个整数，用以提示语言线索，可以提供的语言线索有以下几个： Japanese Chinese
		 * Simplified Chinese Traditional Chinese Korean Dont know (默认)
		 */
		nsDetector det = new nsDetector(lang);
		// 设置一个Oberver
		det.Init(cdo);
		URL url = new URL(args[0]);
		BufferedInputStream imp = new BufferedInputStream(url.openStream());
		byte[] buf = new byte[1024];
		boolean done = false; // 是否已经确定某种字符集
		boolean isAscii = true;// 假定当前的串是ASCII编码
		int len;
		boolean found = false;
		while ((len = imp.read(buf, 0, buf.length)) != -1) {
			// 检查是不是全是ascii字符，当有一个字符不是ASC编码时，则所有的数据即不是ASCII编码了。
			if (isAscii)
				isAscii = det.isAscii(buf, len);
			// 如果不是ascii字符，则调用DoIt方法.
			if (!isAscii && !done)
				done = det.DoIt(buf, len, false);// 如果不是ASCII，又还没确定编码集，则继续检测。
		}
		det.DataEnd();// 最后要调用此方法，此时，Notify被调用。
		if (isAscii) {
			System.out.println("CHARSET = ASCII");
			found = true;
		}
		if (!found) {// 如果没找到，则找到最可能的那些字符集
			String prob[] = det.getProbableCharsets();
			for (int i = 0; i < prob.length; i++) {
				System.out.println("Probable Charset = " + prob[i]);
			}
		}
	}

}
