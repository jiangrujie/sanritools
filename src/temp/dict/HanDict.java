package temp.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 汉字字典，可以查询汉字的拼音，部首和笔画
 * 
 */
public class HanDict {
	/** 汉字最小unicode值 */
	public static final char HAN_MIN = '一';
	/** 汉字最大unicode值 */
	public static final char HAN_MAX = '龥';
	/** 汉字数据，从"一"到"龥" */
	public static final String[] HAN_DATA = new String[HAN_MAX - HAN_MIN + 1];
	/** 汉字数据文件 */
	private static final String HAN_DATA_FILE = "data.txt";
	/** 汉字数据文件编码 */
	private static final Charset FILE_CHARSET = Charset.forName("utf-8");

	/** 拼音数据下标 */
	private static final int INDEX_PY = 0;
	/** 部首数据下标 */
	private static final int INDEX_BS = 1;
	/** 笔画数据下标 */
	private static final int INDEX_BH = 2;
	/** 拼音数据（中文字母注音）下标 */
	private static final int INDEX_PY_HAN = 0;
	/** 拼音数据（英文字母注音）下标 */
	private static final int INDEX_PY_EN = 1;

	static {
		try {
			loadHanData();
		} catch (IOException e) {
			System.err.println("载入汉字数据错误：" + e.getMessage());
		}
	}

	/**
	 * 获取汉字笔画，如 "大"的笔画为"134"<br>
	 * 12345 对应 "横竖撇捺折"
	 * 
	 * @param str
	 *            单个汉字
	 * @return String
	 */
	public static String getBH(String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}

		return getBH(str.charAt(0));
	}

	/**
	 * 获取汉字笔画，如 "大"的笔画为"134"<br>
	 * 12345 对应 "横竖撇捺折"
	 * 
	 * @param ch
	 *            汉字
	 * @return String
	 */
	public static String getBH(char ch) {
		if (isHan(ch)) {
			return HAN_DATA[ch - HAN_MIN].split("\\|")[INDEX_BH];
		}
		return "";
	}

	/**
	 * 获取汉字部首，如果没有部首，返回""
	 * 
	 * @param str
	 *            单个汉字
	 * @return String
	 */
	public static String getBS(String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}

		return getBS(str.charAt(0));
	}

	/**
	 * 获取汉字部首，如果没有部首，返回""
	 * 
	 * @param ch
	 *            汉字
	 * @return String
	 */
	public static String getBS(char ch) {
		if (isHan(ch)) {
			return HAN_DATA[ch - HAN_MIN].split("\\|")[INDEX_BS];
		}
		return "";
	}

	/**
	 * 返回单个汉字的读音列表，读音可能是多个
	 * 
	 * @param ch
	 *            汉字
	 * @param useHanFormat
	 *            true=汉字字母注音，如yī，false=英文字母注音，如yi1
	 * @return List
	 */
	public static List<String> getPY(char ch, boolean useHanFormat) {
		List<String> list = new ArrayList<String>();
		if (isHan(ch)) {
			int i = useHanFormat ? INDEX_PY_HAN : INDEX_PY_EN;
			String pyStr = HAN_DATA[ch - HAN_MIN].split("\\|")[INDEX_PY];
			for (String py : pyStr.split(";")) {
				list.add(py.split(",")[i]);
			}
		}
		return list;
	}

	/**
	 * 返回汉字字符串注音，如果字符串中字符不是汉字，那么使用原字符。<br>
	 * 注意：对于有多个注音的汉字，我们取第一个注音。 <br>
	 * 如："今年的收入为123万。" 返回的结果为："jīn nián de shōu rù wèi 123 wàn 。"
	 * 
	 * @param str
	 *            汉字字符串
	 * @param useHanFormat
	 *            true=汉字字母注音，如yī，false=英文字母注音，如yi1
	 * @return
	 */
	public static String getPY(String str, boolean useHanFormat) {
		if (str == null) {
			return "";
		}
		boolean lastBlank = true;
		StringBuffer sb = new StringBuffer();
		for (char ch : str.toCharArray()) {
			if (isHan(ch)) {
				List<String> pyList = getPY(ch, useHanFormat);
				if (!pyList.isEmpty()) {
					if (!lastBlank) {
						sb.append(" ");
					}
					sb.append(pyList.get(0)).append(' ');
					lastBlank = true;
				}
			} else {
				sb.append(ch);
				lastBlank = false;
			}
		}
		return sb.toString();
	}

	/**
	 * 检查是否汉字
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isHan(char ch) {
		if (ch >= HAN_MIN && ch <= HAN_MAX) {
			return true;
		}
		return false;
	}

	private static void loadHanData() throws IOException {
		InputStream in = HanDict.class.getResourceAsStream(HAN_DATA_FILE);

		if (in == null) {
			throw new IOException(HAN_DATA_FILE + "汉字数据文件不存在！");
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, FILE_CHARSET));
			String line = null;
			int index = 0;
			while ((line = br.readLine()) != null) {
				HAN_DATA[index++] = line;
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * 使用测试
	 */
	public static void main(String[] args) {
		char ch = '郑';
		System.out.println(ch + "的拼音（中式注音）为：" + HanDict.getPY(ch, true));
		System.out.println(ch + "的拼音（英式注音）为：" + HanDict.getPY(ch, false));
		System.out.println(ch + "的部首为　　　　　　：" + HanDict.getBS(ch));
		System.out.println(ch + "的部首笔画为　　　　：" + HanDict.getBH(HanDict.getBS(ch)));
		System.out.println(ch + "的笔画顺序为　　　　：" + HanDict.getBH(ch));
		System.out.println(ch + "的笔画数为　　　　　：" + HanDict.getBH(ch).length());
		System.out.println();
		ch = '大';
		System.out.println(ch + "的拼音（中式注音）为：" + HanDict.getPY(ch, true));
		System.out.println(ch + "的拼音（英式注音）为：" + HanDict.getPY(ch, false));
		System.out.println(ch + "的部首为　　　　　　：" + HanDict.getBS(ch));
		System.out.println(ch + "的部首笔画为　　　　：" + HanDict.getBH(HanDict.getBS(ch)));
		System.out.println(ch + "的笔画顺序为　　　　：" + HanDict.getBH(ch));
		System.out.println(ch + "的笔画数为　　　　　：" + HanDict.getBH(ch).length());
		System.out.println();
		ch = 'a';
		System.out.println(ch + "的拼音（中式注音）为：" + HanDict.getPY(ch, true));
		System.out.println(ch + "的拼音（英式注音）为：" + HanDict.getPY(ch, false));
		System.out.println(ch + "的部首为　　　　　　：" + HanDict.getBS(ch));
		System.out.println(ch + "的部首笔画为　　　　：" + HanDict.getBH(HanDict.getBS(ch)));
		System.out.println(ch + "的笔画顺序为　　　　：" + HanDict.getBH(ch));
		System.out.println(ch + "的笔画数为　　　　　：" + HanDict.getBH(ch).length());
		System.out.println();

		String str = "今年的收入为123万。";
		System.out.println(str + " 的拼音（中式）为：" + getPY(str, true));
		System.out.println(str + " 的拼音（英式）为：" + getPY(str, false));
	}
}
