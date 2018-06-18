package sentiment;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import static java.lang.String.valueOf;
import static java.sql.JDBCType.INTEGER;
import java.util.List;

/**
 * @author sorre
 */
public class MongoDB {
    MongoClient mongoClient = null;
    DB database = null;
    
    public MongoDB(){
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27000"));
        database = mongoClient.getDB("Sentiment");               
    }
    
    public MongoClient secondaryClient (int port){
        MongoClientOptions l_opts =
            MongoClientOptions
            .builder()
            .readPreference( ReadPreference.secondary() )
            .build();
        
        ServerAddress l_addr = new ServerAddress( "localhost", port );     
        MongoClient l_conn = new MongoClient( l_addr, l_opts );
        return l_conn;
    }
     
    public DBCollection getCollection(String nameTable){
        DBCollection collection = database.getCollection(nameTable);
        return collection;
    }
    
    public void insertInCollection(String nameTable, DBObject data){
        DBCollection collection = database.getCollection(nameTable);
        collection.insert(data);
    }
        
    public void generalInsertion(DBCollection collection, DBObject data){
        collection.insert(data);
    }
    
    public void processLine(DBCollection collection, String[] data){
        DBObject word = null;
        if (data.length == 1 )
            word = new BasicDBObject("WORD", data[0]);
        else
            word = new BasicDBObject("WORD", data[0]).append("SCORE", data[1]);
        generalInsertion(collection,word);
    }
    
    public String getScore(String nameTable, String word){
        DBCollection collection = database.getCollection(nameTable);
        DBObject query = new BasicDBObject("WORD", word);
        DBCursor cursor = collection.find(query);
        String ret = "";
        
        //System.out.println("cursor  lenght "+(String)cursor.one().get("WORD"));

        
        if (cursor.length() == 0)
            ret = "0";
        else
            if (cursor.one().containsField("SCORE"))
                ret = (String)cursor.one().get("SCORE");
            else
                ret = "1";
        
        cursor.close();
        return ret;
    }
    
    public boolean updateFrequency(String sentiment, String word){
        DBCollection collection = database.getCollection(sentiment);
        DBObject query = new BasicDBObject("NESTED_MAP.WORD", word);
        DBCursor cursor = collection.find(query);   
              
        boolean isPresent = false;
        isPresent = cursor.length() > 0;
        
        if (isPresent){
            cursor.one().get("NESTED_MAP");
            DBObject nested_map = (DBObject)cursor.one().get("NESTED_MAP");
            int frequency = (int)nested_map.get("Frequency");
            collection.update(new BasicDBObject("NESTED_MAP.WORD", word), new BasicDBObject("$set", new BasicDBObject("NESTED_MAP.Frequency", frequency+1)));
        }
        
        return isPresent;        
    }       
           
    
    public void inserWordInSentiment(String original_word, String stem_word, String sentiment, List<String> listFiles){       
        if (!updateFrequency(sentiment, original_word)){ 
            BasicDBObject nested_map = new BasicDBObject(); 
            nested_map.append("WORD", original_word);
                    
            for ( String filename : listFiles ) {
                if (!filename.startsWith("NRC_") && !filename.startsWith("sentisense_") && !filename.startsWith("EmoSN_")){
                    nested_map.append(filename, getScore(filename, stem_word));
                }   
            }
            
            String nameTable = "";
            
            //NRC_SENTIMENT
            nameTable = "NRC_"+sentiment;
            nested_map.append(nameTable, getScore(nameTable, stem_word));
         
            //SENTISENSE_SENTIMENT
            nameTable = "SENTISENSE_"+sentiment;
            nested_map.append(nameTable, getScore(nameTable, stem_word));

            //EMOSN_SENTIMENT
            if (sentiment.equals("ANGER") || sentiment.equals("JOY")){  
                nameTable = "EmoSN_"+sentiment;
                nested_map.append(nameTable, getScore(nameTable, stem_word));
            }
            
            nested_map.append("Frequency", 1);
            
            BasicDBObject word = new BasicDBObject(); 
            word.append("SENTIMENT", sentiment);
            word.append("NESTED_MAP", nested_map);

            generalInsertion(getCollection(sentiment) , word);     
        }
    }

    public void provaMap(){   
        
        String map_res ="function () {"+
                    "emit(this.WORD, {SCORE : this.SCORE});"+
                    "}";
        
        String map_words ="function () {"+
                    "emit(this.WORD, {SCORE : this.SENTIMENT});"+
                    "}";
        
        String reduce = "function (key,values) { "+
                        " return {key} }";
        
        database.getCollection("prova1").mapReduce(map_res, reduce, "prova3", MapReduceCommand.OutputType.REDUCE , null);
        database.getCollection("prova2").mapReduce(map_words, reduce, "prova3", MapReduceCommand.OutputType.REDUCE , null);
                       
    }
    
    /*
    String map ="function () {"+
                    "emit(this.nome, {count:1});"+
                    "}";
        
        String reduce = "function (key,values) { "+
                        " total = 0;"+
                        " for (var i in values) { "+
                            " total += values[i].count; "+
                        " } "+
                        " return {count:total} }";
        
        MapReduceOutput out1 = database.getCollection("prova1").mapReduce(map, reduce, "prova3", MapReduceCommand.OutputType.REDUCE , null);
        MapReduceOutput out2 = database.getCollection("prova2").mapReduce(map, reduce, "prova3", MapReduceCommand.OutputType.REDUCE , null);
    */
    
    
}
