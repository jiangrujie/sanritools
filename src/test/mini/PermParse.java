package test.mini;
/**
 * 
 * 创建时间:2016-12-2下午8:19:19<br/>
 * 创建者:sanri<br/>
 * 功能:权限解析测试<br/>
 */
public class PermParse {
//	public static void main(String[] args) {
//		int power = 127,remainder = power;
//		while(remainder > 0){
//			int val = 0;
//			while(++val > 0){
//				if(Math.pow(2, val) > remainder){
//					break;
//				}
//			}
//			// 0000 0111
//			int childPower = (int) Math.pow(2, (val - 1));
//			System.out.println("解析出值:"+childPower);
//			remainder -= childPower;
//		}
//	}
	
	public static void main(String[] args) {
		int power = 48;
		for(int i=0;i<16;i++){
			int val = (int)Math.pow(2, i) & power;
			if(val == 0){
				continue;
			}
			System.out.println(i+"-"+val);
		}
	}
	
}
