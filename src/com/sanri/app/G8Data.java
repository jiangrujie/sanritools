package com.sanri.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import sanri.utils.RandomUtil;

public class G8Data {
	public static void main(String[] args) throws FileNotFoundException {
		FileOutputStream outputStream = new FileOutputStream("f:/test.data.txt");
		Charset charset = Charset.forName("utf-8");
		String timeFormat = "yyyy-MM-dd HH:mm:ss";
		try {
			long startTime = System.currentTimeMillis();
			
			System.out.println("开始时间:"+DateFormatUtils.format(startTime, timeFormat));
			//13421772
			for(int j=0;j<19421772;j++){
				StringBuffer uuid = new StringBuffer(UUID.randomUUID().toString()).append("::");
				String data = RandomStringUtils.randomAlphanumeric(45);
				char[] charArray = data.toCharArray();
	
				for (int i = 0; i < charArray.length; i++) {
					String binaryString = Integer.toBinaryString(charArray[i]);
//					uuid.append(StringUtils.leftPad(binaryString, 8, '0'));
					uuid.append(binaryString.substring((int)RandomUtil.randomNumber(6)));
				}
				uuid.append("\r\n");
				IOUtils.write(uuid.toString(), outputStream, charset.name());
				//262144 ->100M
				if(j % 262144 == 0){
					int g = 0,m=0,k=0,b=0;long total= ((long)j)*400;
					System.out.println("j:"+j+" 现在总共:"+total);
					g = (int) (total/(1024*1024*1024));
					m = (int) ((total - g*1024*1024*1024)/(1024*1024));
					k = (int) ((total - g*1024*1024*1024 - m * 1024*1024)/1024);
					b = (int) (total - g*1024*1024*1024 - m * 1024*1024 - k * 1024);
					
					if(g == 1 && m > 600){ //快达到 2G 时重新开流
//						System.err.println("重新开流");
//						IOUtils.closeQuietly(outputStream);
//						outputStream = new FileOutputStream("f:/test.data", true);
					}
					
					System.out.println("当前文件大小:"+g+"G "+m+"M "+k+"K "+b+"b " +DateFormatUtils.format(System.currentTimeMillis(),timeFormat ));
				}
			}
			long endTime = System.currentTimeMillis();
			long spendTime = endTime - startTime;
			System.out.println("开始时间:"+DateFormatUtils.format(startTime, timeFormat));
			
			System.out.println("完成时间:"+DateFormatUtils.format(endTime, timeFormat));
			
			System.out.println("花费时间:"+spendTime);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(outputStream);
		}
	}
}
