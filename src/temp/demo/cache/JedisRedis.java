package temp.demo.cache;

import redis.clients.jedis.Jedis;

public class JedisRedis {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379, 1000);
//		jedis.auth("")
		jedis.set("name", "试试");
		System.out.println(jedis.get("name"));
	}
}
