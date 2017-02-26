package temp.demo.apachecomments;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;

public class Codec {
	public static void main(String[] args) {
		try {
			hex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static Charset charset = Charset.forName("utf-8");
	
	public static void base64() throws UnsupportedEncodingException{
		String str="中文.";
		byte[] bytes = str.getBytes(charset);
		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		System.out.println(new String(encodeBase64));
		byte[] decodeBase64 = Base64.decodeBase64(encodeBase64);
		System.out.println(new String(decodeBase64,charset));
	}
	
	//hex 在网上没有找到,但照这个情况应该是 4bit 一编码成一个字母,"中文" utf-8 编码是 6 字节,所以最终生成 12 位
	//hex 编码原理 取 1 字节 的前4 位和后 4 位在 DIGITS 表中的映射,可自定义 digits 表,一般用数字和字母的组合 
	public static void hex() throws DecoderException{
		String str="中文";
		char[] encodeHex = Hex.encodeHex(str.getBytes(charset));
		System.out.println(new String(encodeHex));
		byte[] decodeHex = Hex.decodeHex(encodeHex);
		System.out.println(new String(decodeHex,charset));
		System.out.println("e4b8ade69687".length());
	}
	
	public static void md5(){
		//md5 本身是没有密匙的,可以自定义一个密匙和密码做一定规则的合并,然后调用 md5 可以达到自定义密匙的作用
		//md5 生成后感觉像是乱码,可再调用 hex 编码(生成32位)或 base64 编码来只生成数字和字符串的组合 
		byte[] md5 = DigestUtils.md5("h196944sanri".getBytes(charset));
		System.out.println(ArrayUtils.toString(md5));
		byte[] encodeBase64 = Base64.encodeBase64(md5);
		System.out.println(new String(encodeBase64));
		
		System.out.println(DigestUtils.md5Hex("h196944{sanri}"));
		System.out.println("8a733b82333ef6f70acaa07f5b2e0b30".length());
	}
	
	public static void sha(){
		//和 md5 一样,但它生成的固定长度是  160 位的,强度要强于 md5
		String sha = DigestUtils.shaHex("h196944sanri");
		System.out.println(sha);
		System.out.println("8586bb61ed45e321ea36cff136633d2e51aad10e".length());
	}
	
}
