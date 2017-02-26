package test;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.sanri.codegenerate.meta.MetaManager;
import com.sanri.codegenerate.meta.Table;

import junit.framework.TestCase;

public class TestMeta extends TestCase{
	
	@Test
	public void testMeata(){
		List<Table> allTables = MetaManager.allTables();
		System.out.println(JSONObject.toJSONString(allTables));
	}
}
