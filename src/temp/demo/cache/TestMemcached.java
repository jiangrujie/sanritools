package temp.demo.cache;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class TestMemcached extends TestCase{
	MemCachedClient memCachedClient = new MemCachedClient();
	
	
	static {
		String[] servers = new String[] { "127.0.0.2:11211" };

		SockIOPool sockIOPool = SockIOPool.getInstance();
		sockIOPool.setServers(servers);

		sockIOPool.setNagle(false);
		sockIOPool.setSocketTO(3000);
		sockIOPool.setSocketConnectTO(0);
		
		//初始化连接池
		sockIOPool.initialize();
	}
	
	@Test
	public void testSimple(){
		boolean set = memCachedClient.set("name", "三日");
		if(set){
			Object object = memCachedClient.get("name");
			System.out.println(object);
		}
	}

	Log logger = LogFactory.getLog(TestMemcached.class);
	
	@Test
	public void testStats(){
		Map<String, Map<String, String>> stats = memCachedClient.stats();
		System.out.println(JSONObject.toJSONString(stats));
		logger.info(stats);
	}
}
