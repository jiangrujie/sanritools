package com.sanri.app.lrcparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sanri.utils.StringUtil;

public class LrcList extends ArrayList<Lrc>{
	private static final long serialVersionUID = 1L;

	private String path; // 当前歌词文件在哪

	private String title; // 歌曲标题
	private String artist; // 艺术家
	private String bySomeBody; // 谁制作的
	private int total; // 总时长
	private String album; // 专辑
	private int offset; // 偏移(未知)
	private String sign; // (未知)
//	private Map<Integer, String> current = new HashMap<Integer, String>(); // 当前时间歌词

	public LrcList(String path) throws IOException {
		this.path = path;
		parse();
	}

	/**
	 * 解析歌词
	 * 
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public boolean parse() throws IOException {
		if (StringUtil.isBlank(path)) {
			throw new FileNotFoundException("文件为空...");
		}
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("文件找不到...");
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			//System.out.println("line:" + line);
			parseLine(line);
		}
		return true;
	}
	/**
	 * 
	 * 功能: 解析歌词中的每一行<br/>
	 * 创建时间:2016-10-9下午10:26:48<br/>
	 * 作者：sanri<br/>
	 * @param str<br/>
	 */
	private void parseLine(String str) {
		int currentTime = 0;// 存放临时时间
		String currentContent = null;// 存放临时歌词
		// 取得歌曲名信息
		if (str.startsWith("[ti:")) {
			String title = str.substring(4, str.length() - 1);
			//System.out.println("title--->" + title);
			this.setTitle(title);

		}// 取得歌手信息
		else if (str.startsWith("[ar:")) {
			String artist = str.substring(4, str.length() - 1);
			//System.out.println("singer--->" + artist);
			this.setArtist(artist);

		}// 取得专辑信息
		else if (str.startsWith("[al:")) {
			String album = str.substring(4, str.length() - 1);
			//System.out.println("album--->" + album);
			this.setAlbum(album);

		}// 通过正则取得每句歌词信息
		else {
			// 设置正则规则
			String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
			// 编译
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(str);

			// 如果存在匹配项，则执行以下操作
			while (matcher.find()) {
//				// 得到匹配的所有内容
//				String msg = matcher.group();
//				// 得到这个匹配项开始的索引
//				int start = matcher.start();
//				// 得到这个匹配项结束的索引
//				int end = matcher.end();

				// 得到这个匹配项中的组数
				int groupCount = matcher.groupCount();
				// 得到每个组中内容
				for (int i = 0; i <= groupCount; i++) {
					String timeStr = matcher.group(i);
					if (i == 1) {
						// 将第二组中的内容设置为当前的一个时间点
						currentTime = strToInt(timeStr);
					}
				}

				// 得到时间点后的内容
				String[] content = pattern.split(str);
				// 输出数组内容
				for (int i = 0; i < content.length; i++) {
					if (i == content.length - 1) {
						// 将内容设置为当前内容
						currentContent = content[i];
					}
				}
				// 设置时间点和内容的映射
//				current.put((int) currentTime, currentContent);
				this.add(new Lrc(currentTime,currentContent));
				//System.out.println("put---currentTime--->" + currentTime+ "----currentContent---->" + currentContent);

			}
			//排序
			Collections.sort(this);
		}
	}

	/**
	 * 将解析得到的表示时间的字符转化为Long型
	 * 
	 * @param group
	 *            字符形式的时间点
	 * @return Long形式的时间
	 */
	private int strToInt(String timeStr) {
		// 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
		// 1:使用：分割 2：使用.分割
		String[] s = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min * 60 * 1000 + sec * 1000 + mill * 10;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getBySomeBody() {
		return bySomeBody;
	}

	public void setBySomeBody(String bySomeBody) {
		this.bySomeBody = bySomeBody;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) throws IOException {
		this.path = path;
		parse();
	}
}
