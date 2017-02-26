package temp.demo.cache;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

public class LinkMongo {
//	public static void main(String[] args) {
//		try {
//			Mongo mongo = new Mongo("localhost", 27017);
//			DB db = mongo.getDB("test");
//			
//			Set<String> collectionNames = db.getCollectionNames();
//			Iterator<String> iterator = collectionNames.iterator();
//			while(iterator.hasNext()){
//				String next = iterator.next();
//				System.out.println(next);
//			}
//			
//			
////			DBCollection collection = db.getCollection("person");
////			DBCursor find = collection.find();
////			Iterator<DBObject> iterator = find.iterator();
////			while(iterator.hasNext()){
////				DBObject next = iterator.next();
////				Object object = next.get("name");
////				System.out.println(object);
////			}
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (MongoException e) {
//			e.printStackTrace();
//		}
//	}
	//crdu
	DB db = null;
	public LinkMongo(){
		Mongo mongo;
		try {
			mongo = new Mongo("localhost", 27017);
			db = mongo.getDB("test");
			boolean authenticate = db.authenticate("root", "rdoot".toCharArray());
			if(!authenticate){
				throw new MongoException("无法验证用户");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public void findAll(){
		DBCollection collection = db.getCollection("user");
		DBCursor find = collection.find();
		Iterator<DBObject> iterator = find.iterator();
		
		while(iterator.hasNext()){
			DBObject next = iterator.next();
//			Set<String> keySet = next.keySet();
			System.out.println(JSONObject.toJSONString(next));
		}
	}
	
	public void insert(){
		DBCollection collection = db.getCollection("user");
		DBObject dbObject = new BasicDBObject();
		dbObject.put("name", RandomStringUtils.randomAscii(5));
		dbObject.put("age", RandomStringUtils.randomNumeric(2));
		collection.insert(dbObject);
	}
	
	public void update(){
		DBCollection collection = db.getCollection("user");
//		collection.update(new BasicDBObject("_id", new ObjectId(s)), o)
		DBObject findOne = collection.findOne(new BasicDBObject("age",98)); //数字和字符串有区分,当没有数据时返回 null
		Object object = findOne.get("_id");
		String id = object.toString();
		DBObject updateObject = new BasicDBObject("_id",new ObjectId(id));		//这里必须要 new ObjectId 因为源码中有判断key 是否是 _id value 是否是 ObjectId  的实例 
		System.out.println(id);
		//new BasicDBObject("birthday",DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd")
//		updateObject.put("birthday", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
		DBObject updateTo = new BasicDBObject("birthday", new Date().getTime());
		DBObject updateToSet = new BasicDBObject("$set",updateTo);
		updateTo.put("age", 98);
		updateTo.put("go", "中文可以正常显示吗?");
		
		WriteResult update = collection.update(updateObject,  updateToSet,
					true,	//如果本条记录不存在是否增加
					false	//false 只修改第一条,true如果有多条就不修改
				); //这样修改直接覆盖了
		System.out.println(update.getN());
		
//		collection.updateMulti(q, o)
	}
	
	public void delete(){
		DBCollection collection = db.getCollection("user");
		DBObject findOne = collection.findOne(new BasicDBObject("age", "48"));
		Object object = findOne.get("_id");
		String id = object.toString();
		WriteResult remove = collection.remove(new BasicDBObject("_id",new ObjectId( id)));
		System.out.println(remove.getN());
	}
	
	public static void main(String[] args) {
		LinkMongo linkMongo = new LinkMongo();
//		linkMongo.insert();
//		linkMongo.update();
//		linkMongo.delete();
		linkMongo.findAll();
	}
	
}
