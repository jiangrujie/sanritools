package sanri.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 创建时间:2016-10-2上午9:34:36<br/>
 * 创建者:sanri<br/>
 * 功能:cookie 工具类,以后可能还需要改动<br/>
 */
public class CookieUtil {
	private static String default_path = "/";
	private static int default_age = 365 * 24 * 3600;
	private static Charset charset = Charset.forName("utf-8");

	public static void addCookie(String name, String value, HttpServletResponse response, int age) {

		Cookie cookie = new Cookie(name,new String(Base64.encodeBase64(value.getBytes(charset))));
		cookie.setMaxAge(age);
		cookie.setPath(default_path);
		response.addCookie(cookie);

	}

	public static void addCookie(String name, String value, HttpServletResponse response) {
		addCookie(name, value, response, default_age);

	}

	public static String findCookie(String name, HttpServletRequest request) throws UnsupportedEncodingException {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(name)) {
					value = new String(Base64.decodeBase64(cookie.getValue().getBytes()),charset);
				}
			}
		}
		return value;

	}

	public static void deleteCookie(String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setPath(default_path);
		response.addCookie(cookie);
	}
}
