package com.sanri.app.autoweeklog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import sanri.utils.PathUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;
import sanri.utils.VelocityUtil;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

/**
 * 
 * 创建时间:2016-10-2上午9:23:00<br/>
 * 创建者:sanri<br/>
 * 功能:自动根据模板生成 txt 文件;在填写完毕后,然后转成 excel,发送周报<br/>
 */
public class AutoWeekLog extends Thread{
	public static Configuration AUTO_WEEK_LOG;
	//模型数据,主要有 year,beginDate,endDate,week
	private static Map<String,Object> modulData = new HashMap<String, Object>();
	private Log logger = LogFactory.getLog(AutoWeekLog.class);
	private Map<String,Boolean> sended = new HashMap<String, Boolean>(); //如果这周已经发送过周报,则不需要再发送
	//读入属性
	static{
		String configFilePath = PathUtil.pkgPath("com.sanri.app.autoweeklog");
		try {
			AUTO_WEEK_LOG = new PropertiesConfiguration(configFilePath+"/autoWeekLog.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private AutoWeekLog(){}
	
	private static AutoWeekLog autoWeekLog = new AutoWeekLog();
	public static AutoWeekLog getInstance(){
		return autoWeekLog;
	}
	/**
	 * 
	 * 功能:主执行方法,会每12 小时 检测一次,如果到了要发周报的那一天,则调用 excel 生成,然后发送邮件<br/>
	 * 在此方法中获取年,周,上周开始日期和结束日期
	 * 创建时间:2016-10-4下午12:06:40<br/>
	 * 作者：sanri<br/>
	 */
	public void work(){
		int startDate = AUTO_WEEK_LOG.getInt("startDate");
		String dataPath = AUTO_WEEK_LOG.getString("dataPath");
		int weekBegin = AUTO_WEEK_LOG.getInt("weekBegin");
		String dateFormat = AUTO_WEEK_LOG.getString("dateFormat");
		
		Calendar calendar = Calendar.getInstance();
		int nowDay = calendar.get(Calendar.DAY_OF_WEEK);
		String now = DateFormatUtils.format(calendar, dateFormat);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		int nowWeek = weekOfYear - weekBegin;
		
		//如果今天没有数据文件,将会创建 
		File dataDir = new File(dataPath+File.separator+nowWeek);
		if(!dataDir.exists()){
			dataDir.mkdirs();
		}
		File dataFile = new File(dataDir,now+".txt");
		if(!dataFile.exists()){
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("检测是否需要发送周报 "+nowWeek);
		Boolean isSend = sended.get(String.valueOf(nowWeek - 1));
		if((nowDay - 1) != startDate || (isSend != null && isSend.booleanValue()) ){
			//不需要发周报
			return ;
		}
		logger.info("今天需要发送周报,今天是第星期:"+(nowDay - 1));
		
		
		//设置模板值
		calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);	//上周周一
		
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String beginDate = DateFormatUtils.format(calendar, dateFormat);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);	//上周周五
		String endDate = DateFormatUtils.format(calendar, dateFormat);
		
		modulData.put("year", year);
		modulData.put("week", String.valueOf(nowWeek - 1) );
		modulData.put("beginDate", beginDate);
		modulData.put("endDate", endDate);
		
		//生成 excel ,并发送邮件
		if(generateExcel()){
			sendMail();
		}
	}
	
	/**
	 * 
	 * 功能:excel 生成,会默认根据数据路径中的文本来组织 excel <br/>
	 * 创建时间:2016-10-4下午12:04:33<br/>
	 * 作者：sanri<br/>
	 * @return 
	 */
	XLSTransformer formatter = new XLSTransformer();
	public boolean generateExcel(){
		//周报路径和文件名
		String generatePath = AUTO_WEEK_LOG.getString("generatePath");
		String fileNameModul = AUTO_WEEK_LOG.getString("fileNameModul");
		String fileName = VelocityUtil.formatterString(fileNameModul, modulData);
		//模板路径
		String xlsTemplatePath = AUTO_WEEK_LOG.getString("tempPath");
		Map<String, Object> contextData = loadData();
		List<Work> doThings = (List<Work>) contextData.get("doThings");
		if(Validate.isEmpty(doThings)){
			logger.info("上周没有做事情,先手动发送吧");
			return false;
		}
		File tplFile = new File(xlsTemplatePath);
		if(Validate.isEmpty(tplFile)){
			logger.error("模板文件不存在");
			return false;
		}
		try {
			InputStream tplFileInputStream = new FileInputStream(tplFile);
			File generateDir = new File(generatePath);
			if(Validate.isEmpty(generateDir)){
				generateDir.mkdirs();
			}
			File generateFile = new File(generateDir,fileName);
			OutputStream generateFileOutputStream = new FileOutputStream(generateFile);
			Workbook workbook = formatter.transformXLS(tplFileInputStream, contextData);
			//居然还要自已来设置 sheet name ...
			Sheet sheetAt = workbook.getSheetAt(0);
			String sheetName = sheetAt.getSheetName();
			workbook.setSheetName(0, VelocityUtil.formatterString(sheetName, modulData));
			
			workbook.write(generateFileOutputStream);
			
			IOUtils.closeQuietly(generateFileOutputStream);
			IOUtils.closeQuietly(tplFileInputStream);
			
			return true;
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * 
	 * 功能:读取数据文件夹中数据<br/>
	 * 创建时间:2016-10-4下午1:56:14<br/>
	 * 作者：sanri<br/>
	 */
	private Map<String,Object> loadData() {
		Map<String,Object> contextData = new HashMap<String, Object>();
		//数据路径
		String dataPath = AUTO_WEEK_LOG.getString("dataPath");
		File dataDir = new File(dataPath);
		if(!dataDir.exists()){
			dataDir.mkdirs();
		}
		String lastWeek = String.valueOf(modulData.get("week"));
		File lastWeekDir = new File(dataDir,lastWeek);
		if(!lastWeekDir.exists()){
			logger.debug("上周没有做事??? "+lastWeek);
			return null;
		}
		File[] listFiles = lastWeekDir.listFiles();
		if(!Validate.isEmpty(listFiles)){
			List<Work> doThings = new ArrayList<Work>();
			try{
				String fileName = "";
				List<String> fileLines = null;
				InputStreamReader inputStreamReader = null;
				int count = 0;
				for (File currentFile : listFiles) {
					fileName = StringUtil.prefix(currentFile.getName());
					inputStreamReader= new InputStreamReader(new FileInputStream(currentFile), "utf-8");
					//可能存在空行
					fileLines = IOUtils.readLines(inputStreamReader);
					if(!Validate.isEmpty(fileLines)){
						for (String fileLine : fileLines) {
							if(!StringUtil.isBlank(fileLine)){
								String[] split = fileLine.split("\\s");
								Work work = new Work(count, fileName, split[0]);
								work.setStatus("已完成");
								if(split.length == 2){
									work.setStatus(split[1]);
								}
								if(split.length == 3){
									work.setRemark(split[2]);
								}
								doThings.add(work);
								count ++;
							}
						}
					}
					IOUtils.closeQuietly(inputStreamReader);
				}
				contextData.put("doThings", doThings);
			}catch(IOException e){
				e.printStackTrace();
			}
			contextData.putAll(modulData);
			return contextData;
		}
		logger.debug("上周没有做事,没有数据文件??? "+lastWeek);
		return contextData;
	}

	/**
	 * 
	 * 功能:发送邮件<br/>
	 * 创建时间:2016-10-8上午10:19:48<br/>
	 * 作者：sanri<br/>
	 */
	@SuppressWarnings("unchecked")
	public void sendMail(){
		//获取匹配
		String host = AUTO_WEEK_LOG.getString("mail.smtp.host");
//		int port = AUTO_WEEK_LOG.getInt("mail.smtp.port"); //端口固定是 25
		boolean auth = AUTO_WEEK_LOG.getBoolean("mail.smtp.auth");
		String[] addressFrom = parseAddress(AUTO_WEEK_LOG.getString("address.from"));
		String charset = AUTO_WEEK_LOG.getString("email.charset");
		List<String> sendTo = AUTO_WEEK_LOG.getList("sendTo");
		if(Validate.isEmpty(sendTo)){return ;}
		List<String> copyTo = AUTO_WEEK_LOG.getList("copyTo");
		String msg = VelocityUtil.formatterString(AUTO_WEEK_LOG.getString("msg"), modulData);
		String subjectModul = AUTO_WEEK_LOG.getString("subjectModul");
		String subject = VelocityUtil.formatterString(subjectModul, modulData);
		//周报路径和文件名
		String generatePath = AUTO_WEEK_LOG.getString("generatePath");
		String fileName = VelocityUtil.formatterString(AUTO_WEEK_LOG.getString("fileNameModul"), modulData);

		//发送邮件主体
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(host);
		email.setCharset(charset);
		
		if(auth){		//登录邮箱 
			String userName = AUTO_WEEK_LOG.getString("username");
			String userPass = AUTO_WEEK_LOG.getString("userpass");
			email.setAuthentication(userName, userPass);
		}
		try {
			//生成附件
			EmailAttachment weekLogFileAttach = new EmailAttachment();
			weekLogFileAttach.setDescription(fileName);
			weekLogFileAttach.setDisposition(EmailAttachment.ATTACHMENT);
			weekLogFileAttach.setPath(generatePath+File.separator+fileName);
			weekLogFileAttach.setName(MimeUtility.encodeText(fileName));
			
			email.setFrom(addressFrom[1], addressFrom[0]);	// 发信人
			email.setMsg(msg);
			email.setSubject(subject);
			email.attach(weekLogFileAttach);
			
			String [] addressToOrCopy = null;
			//设置发送到哪
			for (String sendAddress : sendTo) {
				addressToOrCopy = parseAddress(sendAddress);
				email.addTo(addressToOrCopy[1], addressToOrCopy[0]);
			}
			//设置抄送
			if(!Validate.isEmpty(copyTo)){
				for (String copyAddress : copyTo) {
					addressToOrCopy = parseAddress(copyAddress);
					email.addCc(addressToOrCopy[1], addressToOrCopy[0]);
				}
			}
			
			email.send();
			sended.put(String.valueOf(modulData.get("week")), true);
		} catch (EmailException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 
	 * 功能:地址解析主要针对于sanri1993<2441719087@qq.com> 的地址<br/>
	 * 第一栏为名称,第二栏为地址<br/>
	 * 解析出 名称和地址返回长度为 2 的 String 数组,如果没有名称,第一栏名称将为地址字符串<br/>
	 * 创建时间:2016-10-8上午10:42:54<br/>
	 * 作者：sanri<br/>
	 */
	Pattern addressPattern = Pattern.compile("(.+)<(.+)>");
	private String [] parseAddress(String address){
		String[] match = Validate.match(address, addressPattern);
		if(match != null){
			return match;
		}
		match = new String [2];
		match[0] = address;
		match[1] = address;
		return match;
	}
	
	@Override
	public void run() {
		work();
	}
	
	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(AutoWeekLog.getInstance(), 0, 12, TimeUnit.HOURS);
	}

}
