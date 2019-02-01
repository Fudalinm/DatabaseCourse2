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
		db = mongoClient.getDB("ydc");
	}
	
	private void showCollections(){
		for(String name : db.getCollectionNames()){
			System.out.println("colname: "+name);
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		MongoLab mongoLab = new MongoLab();
		mongoLab.showCollections();
		
		
		fiveStarPlaces(db);
		restaurantsInCity();
		hotesWiFi();
		opinionTypes();
		System.out.print(mostPositiveReviews());
	}
	
	//five star places
	//db.business.find({"stars": 5})
	public static void fiveStarPlaces(DB db){
		BasicDBObject query = new BasicDBObject("stars",5);
		DBCursor cursor = db.getCollection("business").find(query);
		
		while(cursor.hasNext()){
			DBObject tmp = cursor.next();
			System.out.println(tmp.toString());
		}
		
	}
//	db.business.aggregate([
//	                       { $match: {categories: "Restaurants"} },
//	                       { $group: { _id: "$city", total: {$sum: 1} } },
//	                       { $sort: {total:-1} }
//	                   ])
	public static void restaurantsInCity(){
		Iterable<DBObject> out = 
		db.getCollection("business").aggregate(
				Arrays.asList(
						(DBObject) new BasicDBObject("$match", new BasicDBObject("categories","Restaurants")),
						(DBObject) new BasicDBObject("$group", new BasicDBObject("_id","$city")
								.append("total:", new BasicDBObject("$sum",1))
								),
						(DBObject) new BasicDBObject("$sort",new BasicDBObject("total:",-1))
				)
		).results();
		
		for (DBObject x : out){
			System.out.println(x);
		}
	}
//	db.business.group( {
//		 key: {state: 1},
//		 cond: {categories: "Hotels", stars: {$gte: 4.5}, 'attributes.Wi-Fi':"free"},
//		 reduce: function(cur, result) {result.total += 1},
//		 initial: {total:1}
//		 })
	public static void hotesWiFi(){
		Iterable<DBObject> out = 
		db.getCollection("business").aggregate(
				Arrays.asList(
						(DBObject) new BasicDBObject("$match", new BasicDBObject("categories","Hotels").append("stars",new BasicDBObject("$gte",4.5)).append("attributes.Wi-Fi", "free")),
						(DBObject) new BasicDBObject("$group", new BasicDBObject("_id","$state")
								.append("total:", new BasicDBObject("$sum",1))
								),
						(DBObject) new BasicDBObject("$sort",new BasicDBObject("total:",-1))
				)
		).results();
		
		for (DBObject x : out){
			System.out.println(x);
		}
	}
//	db.review.mapReduce(
//		    function(){
//		        if(this.votes && this.votes["funny"] > 0){
//		            emit("funny",1)
//		        }
//		        if(this.votes && this.votes["cool"] > 0){
//		            emit("cool",1)
//		        }
//		        if(this.votes && this.votes["useful"] > 0){
//		            emit("useful",1)
//		        }
//		    },
//		    function(cat,one){
//		        return Array.sum(one);
//		    },
//		    {out: "sum "}
//		)
	public static void opinionTypes(){
		DBCollection coll = db.getCollection("review");
		String map = " function(){ "
				+ "if(this.votes && this.votes[\"funny\"] > 0)"
					+ "{ emit(\"funny\",1) } "
				+ "if(this.votes && this.votes[\"cool\"] > 0)"
					+ "{emit(\"cool\",1)}"
				+ "if(this.votes && this.votes[\"useful\"] > 0)"
				+ "{emit(\"useful\",1)}"
				+ "}";
		String reduce = "function(cat,one){ return Array.sum(one); }";

		MapReduceCommand mapcmd = new MapReduceCommand(coll,map,reduce,null,MapReduceCommand.OutputType.INLINE,null);
		
		MapReduceOutput out = coll.mapReduce(mapcmd);
		
		for(DBObject o : out.results()){
			System.out.println(o);
		}
	}
	
	public static String mostPositiveReviews(){
		//na poczatku bierzemy wszystkie recenzje
		Iterable<DBObject> out =
		db.getCollection("review").aggregate(
				Arrays.asList(
					(DBObject) new BasicDBObject("$match", new BasicDBObject("stars",new BasicDBObject("$gte",4.5))),
					(DBObject) new BasicDBObject("$group", new BasicDBObject("_id","$user_id").append("total:", new BasicDBObject("$sum",1)) ),
					(DBObject) new BasicDBObject("$sort",new BasicDBObject("total:",-1))
				)
				).results();
		
		String UserId =  out.iterator().next().get("_id").toString();
		Iterable<DBObject> out2 = db.getCollection("user").find( new BasicDBObject("user_id",UserId) );
		
		
		return out2.iterator().next().get("name").toString();
	}


	
}
