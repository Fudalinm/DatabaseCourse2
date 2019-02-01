package pl.edu.agh.bd.mongo;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.MapReduceCommand;

public class MongoLab {
	
	private MongoClient mongoClient;
	private static DB db;
	
	public MongoLab() throws UnknownHostException {
		mongoClient = new MongoClient();
		db = mongoClient.getDB("jeopardy");
	}
	
	private void showCollections(){
		for(String name : db.getCollectionNames()){
			System.out.println("colname: "+name);
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		MongoLab mongoLab = new MongoLab();
		mongoLab.showCollections();
		System.out.println("SimpleOne:");
		DBCursor cursor = simpleOne();
		while(cursor.hasNext()){
			DBObject tmp = cursor.next();
			System.out.println(tmp.toString());
		}
		System.out.println("With Aggregation");
		Iterable<DBObject> out = withAggregation();
		for (DBObject x : out){
			System.out.println(x);
		}
		System.out.println("With map reduce");
		Iterable<DBObject> out2 = withMapReduce();
		for (DBObject x : out2){
			System.out.println(x);
		}
	
		
	}
	
//db.question.find({category: "HISTORY", value: { $eq: "$200"}}).sort({ air_date: 1})
	public static DBCursor simpleOne(){
		return db.getCollection("question").find(
				(DBObject) new BasicDBObject("category","HISTORY").append("value", (DBObject) new BasicDBObject("$eq","$200"))
		).sort((DBObject) new BasicDBObject("air_date", 1));	
	}
//	db.question.aggregate([
//	                       {$match: {category: "HISTORY"}},
//	                       { $group: { _id: "$value", total: {$sum: 1} } },
//	                       { $sort: {total:-1} }
//	                   ])
	public static Iterable<DBObject> withAggregation(){
		Iterable<DBObject> out =
				db.getCollection("question").aggregate(
						Arrays.asList(
							(DBObject) new BasicDBObject("$match", new BasicDBObject("category","HISTORY")),	
							(DBObject) new BasicDBObject("$group", new BasicDBObject("_id","$value")
							.append("total:", new BasicDBObject("$sum",1))),
							(DBObject) new BasicDBObject("$sort",new BasicDBObject("total:",-1))
						)
				).results();
		return out;
	}

//	db.question.mapReduce(
//		    function(){
//		        if(this.category && this.value){
//		            emit(this.category,this.value);
//		        }
//		    },
//		    function(cat,val){
//		        return Array.sum(val);
//		    },
//		    {
//		        
//		        out: "out23"
//		    }
//		)
	public static Iterable<DBObject> withMapReduce(){
		DBCollection coll = db.getCollection("question");
		String map = "function(){ "
					+ "if(this.category && this.value){ "
						+ "emit(this.category,this.value);"
					+ "}"
				+ "}";
		String reduce = "function(cat,val){"
				+ "return Array.sum(val);"
				+ "}";
		MapReduceCommand mapcmd = new MapReduceCommand(coll,map,reduce,null,MapReduceCommand.OutputType.INLINE,null);
		MapReduceOutput out = coll.mapReduce(mapcmd);
		return out.results();
	}
	


	
	
	
	
	
	
}
