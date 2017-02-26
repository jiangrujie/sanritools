package temp.demo.apachecomments;
/**
 * 
 * 作者: sanri<br/>
 * 时间:2016-9-12上午10:08:44
 * 功能:为了不重复造轮子,先测试一些 apache 中原来就有的工具类,有的话就直接用了<br/>
 */
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DurationFormatUtils;



public class Lang {
	public static void main(String[] args) {
		System.out.println("判断两个数组是否相等,这个方法排列顺序都必须相等,不同类型也不能相等");
		String [] array1 = new String []{"333","ddd"};
		String [] array2 = new String []{"333","ddd"};
		
		int [] array4 = new int []{1,2,3};
		String [] array5 = new String []{"1","2","3"};
		
		System.out.println(ArrayUtils.isEquals(array1, array2));
		System.out.println(ArrayUtils.isEquals(array4, array5));
		
		System.out.println("输出数组内容,这个比较(实用)");
		System.out.println(ArrayUtils.toString(array4));
		
		System.out.println("获取类名,包名,字节码工具类");
		System.out.println(ClassUtils.getPackageName(Lang.class));
		System.out.println(ClassUtils.getShortClassName(Lang.class));
		
		System.out.println("随机数字字母(实用)");
		System.out.println(RandomStringUtils.randomAlphanumeric(5));
		System.out.println(RandomStringUtils.randomNumeric(5));
		System.out.println(RandomStringUtils.randomAlphabetic(5));
		System.out.println(RandomStringUtils.random(5, "人是中国为"));
		
		System.out.println("escape,把特殊符号编码");
		System.out.println(StringEscapeUtils.escapeHtml("<html>"));
		System.out.println(StringEscapeUtils.escapeJava("String><;"));
		
		System.out.println("字符串工具类(都是比较实用的),首字母大写,join 方法,判空方法,"); 
		//调用了 Character.isWhitespace
		System.out.println(StringUtils.isBlank(" ")); //space true
		System.out.println(StringUtils.isBlank("	"));  //tab true
		System.out.println(StringUtils.isBlank(null));	//null true
		
		System.out.println(StringUtils.join(array5, "$"));
		System.out.println(StringUtils.capitalize("hello world"));
		System.out.println(StringUtils.rightPad("abc", 6, "t"));
		System.out.println(StringUtils.leftPad("3933232", 6, "t"));
		System.out.println(StringUtils.isNumeric("d"));; // Character.isDigit
		
		System.out.println("对象工具类");
		System.out.println(ObjectUtils.toString(array5));
		
		System.out.println("文本格式工具类(实用)");
		System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateFormatUtils.format(23232332, "yyyy-MM-dd HH:mm:ss"));
		
		System.out.println("单词处理类");
		System.out.println(WordUtils.capitalize("hello world. i am sanri")); //所有单词的首字母都大写了,与 StringUtil 不一样
		
		System.out.println("日期工具类(实用)");
		Date now = new Date();
		String format="yyyy-MM-dd HH:mm:ss";
		System.out.println(DateFormatUtils.format(DateUtils.addDays(now, 3), format));
		System.out.println(DateFormatUtils.format(DateUtils.addDays(now, -3), format));
		System.out.println(DateFormatUtils.format(DateUtils.addWeeks(now, -1), format));
		System.out.println(DateFormatUtils.format(DateUtils.addMonths(now, -1), format));
		String formatDate = DurationFormatUtils.formatDuration(-(10+20*60+13*3600+4*24*3600) * 1000,  format);  
		System.out.println(formatDate);
		//计算已过去的天数 
		long fragmentInDays = DateUtils.getFragmentInDays(now, Calendar.MONTH);
		System.out.println(fragmentInDays);		//从当月起已过去 8 天
		System.out.println(DateUtils.getFragmentInHours(now, Calendar.MONTH));	//从当月起已过去多少小时
		System.out.println(DateUtils.getFragmentInDays(now, Calendar.YEAR));	//从当年起已过去多少天
		
		Date addWeeks = DateUtils.addWeeks(now, -1);
		System.out.println(DateFormatUtils.format(addWeeks, format));
		
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY);
		calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		System.out.println(DateFormatUtils.format(calendar, format));
		
		//获取每日,每周,每月的 0 点和 24 点的时间戳
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		System.out.println(cal.getTimeInMillis());
		System.out.println(DateFormatUtils.format(cal, "yyyy-MM-dd HH:mm:ss"));
	}
}
                                           