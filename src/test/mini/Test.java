package test.mini;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		int a = 11;
		boolean flag=true;
		int n=0;
		while(flag){
			if(Math.pow(2,n) > a){
				flag = false;
			}else{
				n++;
			} 
		}
		List list = new ArrayList();
		int num1 = (int)Math.pow(2,n-1);
		list.add(num1);
		
		System.out.println("power="+a);
		
		bbb(a, n, num1,list);
		
		System.out.println("解析数值="+list);
		
	}

	private static void bbb(int a, int n, int num1,List list) {
		for(int j = n-2;j>=0;j--){
			int num2 = (int)Math.pow(2,j);
			int num3 = num1+num2;
			if(num3 == a){
				list.add(num2);
				break;
			}else if(num3 < a){
				list.add(num2);
			}
		}
	}

}
