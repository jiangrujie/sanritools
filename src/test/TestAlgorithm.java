package test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.sanri.algorithm.PaiDataProvide;
import com.sanri.algorithm.PaiDataProvide.GenType;

/**
 * 
 * 创建时间:2016-9-29下午8:43:09<br/>
 * 创建者:sanri<br/>
 * 功能:算法测试<br/>
 */
public class TestAlgorithm  extends TestCase{
	
	@Test
	public void testFanPai(){
		PaiDataProvide provide = new PaiDataProvide(GenType.STACK, 13);
		Map<Integer,String> map = new HashMap<Integer, String>();
		map.put(11, "J");
		map.put(12, "Q");
		map.put(13, "K");
		provide.setKeyValue(map);
		
		for (PaiDataProvide.Item item : provide.getData()) {
			System.out.println("index = " + item.getIndex() + " value = " + item.getValue());
		}
//		provide.setLength(50);
//		for (PaiDataProvide.Item item : provide.getData()) {
//			System.out.println("index = " + item.getIndex() + " value = " + item.getValue());
//		}
	}
}
