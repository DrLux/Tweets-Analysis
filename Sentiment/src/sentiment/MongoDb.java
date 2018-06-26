package sentiment;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 * @author Luca Sorrentino
 */
public class MongoDb {
    MongoClient mongoClient = null;
    MongoDatabase database = null;
    MongoCollection<Document> collection = null;

    
    public MongoDb(int port, Boolean isSecondary){
        if (!isSecondary){
            this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:"+port));
        } else {
            MongoClientOptions l_opts =
            MongoClientOptions
            .builder()
            .readPreference( ReadPreference.secondary() )
            .build();
        
        ServerAddress l_addr = new ServerAddress( "localhost", port );     
        mongoClient = new MongoClient( l_addr, l_opts );
        }
        this.database = mongoClient.getDatabase("Sentiment");        
    }
    
    public void setCollection(String sentiment){
        this.collection = database.getCollection(sentiment);
    }
    
    public void insertDocument(String sentiment, String word, String type, Set<String> resources, Map<String, Float> resorcesWithScore, int freq){
        Document document = new Document("SENTIMENT", sentiment)
                                        .append("WORD", word)
                                        .append("TYPE", type)
                                        .append("FREQUENCY", freq)
                                        .append("RESOURCES", resources)
                                        .append("SCORE_RESOURCES", resorcesWithScore);
        this.collection.insertOne(document);
    }
    
    public void printCollection(){
        MongoCursor<Document> all = this.collection.find().iterator();
        Document next = all.next();
        while (next != null){
            System.out.println(next);
            next = all.next();
        }
            
    }
    
    public void dumpWordFrequency(String sentiment, String type, int treshold){
        try {
            
            FileOutputStream fos = new FileOutputStream("dump/"+sentiment+"_"+type+".txt");
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);

            FindIterable<Document> document = database.getCollection("anger").find(
            new Document("FREQUENCY", new Document("$gte", treshold))).projection(Projections.fields(Projections.include("WORD"), Projections.include("FREQUENCY")));
            for (Document doc : document) 
                bw.append(doc.get("WORD")+" : "+ doc.get("FREQUENCY")+"\n");
            
            bw.flush();
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MongoDb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MongoDb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MongoDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}


        
    