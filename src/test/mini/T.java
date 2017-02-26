package test.mini;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;

public class T {
	public static void main(String[] args) {
		int power = 48,remained =48,count = 0,max =48;
		while(remained > 0){
			count ++;
			int findVal = findVal(max, remained);
			max = findVal;
			int val = (int) Math.pow(2, findVal);
			System.out.println("第 "+count + "个值为:"+val);
			remained = remained - val;
		}
	}
	
	public static int findVal(int max,int power){
		for(int i=0;i<max;i++){
			if(Math.pow(2, i) > power){
				max = i;
				break;
			}
		}
		return max - 1;
	}
}
