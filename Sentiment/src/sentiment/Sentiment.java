package sentiment;

import com.mongodb.Mongo;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.spark.api.java.JavaPairRDD;
import scala.Tuple2;

/**
 * @author Sorrentino Luca
 */
public class Sentiment {
    private String sentiment = null;    
    private JavaPairRDD<String, WordWrapper> processed_tweet;
    private Resources res;
    private ProcessorSpark spark;
    private OracleDB oracle;
    private MongoDb mongo;
    
    public Sentiment(String sentiment, Resources res, ProcessorSpark spark, OracleDB oracle, MongoDb mongo){
        this.sentiment = sentiment;
        this.res = res;
        this.spark = spark;
        this.oracle = oracle;
        this.mongo = mongo;
        process_tweets();
        storeAllDbs();
        dump_for_wordcloud();
    }   
    
    public void process_tweets(){
        this.processed_tweet = spark.preprocess_tweet("tweets/dataset_dt_"+sentiment+"_60k.txt", sentiment); //tweet
        //this.processed_tweet = spark.preprocess_tweet("tweets/"+sentiment+".txt", sentiment); //tweet
        
        List<String> resource; 
                
        resource = res.getScoredResources();
        for (String r : resource)
            this.processed_tweet = spark.process_resource_with_score(this.res.nameToResources(r), processed_tweet, r);

        resource = res.getResources(sentiment);
        for (String r : resource)
            this.processed_tweet = spark.process_resource(this.res.nameToResources(r), processed_tweet, r);
        
        processed_tweet = spark.filterResults(10, processed_tweet);
        
        //ps.quitSpark();
    }   
    
    public void store_in_oracle(){
        oracle.createTable(sentiment);
        for(Tuple2<String, WordWrapper> line : processed_tweet.collect()){
            oracle.insertDocument(line._2.getSentiment(), line._2.getWord(), line._2.getType(), line._2.getResources(), line._2.getResorcesWithScore(), line._2.getFrequency());
        }    
    }
    
    public void store_in_mongo(){
        mongo.setCollection(sentiment);
        for(Tuple2<String, WordWrapper> line : processed_tweet.collect()){
            mongo.insertDocument( line._2.getSentiment(), line._2.getWord(), line._2.getType(), line._2.getResources(), line._2.getResorcesWithScore(), line._2.getFrequency());
        }   
    }
    
    public void storeAllDbs(){
        mongo.setCollection(sentiment);
        oracle.createTable(sentiment);

        for(Tuple2<String, WordWrapper> line : processed_tweet.collect()){
            oracle.insertDocument(line._2.getSentiment(), line._2.getWord(), line._2.getType(), line._2.getResources(), line._2.getResorcesWithScore(), line._2.getFrequency());
            mongo.insertDocument( line._2.getSentiment(), line._2.getWord(), line._2.getType(), line._2.getResources(), line._2.getResorcesWithScore(), line._2.getFrequency());
        }
    }
    
    public void dump_for_wordcloud(){
        mongo.dumpWordFrequency(sentiment, "word");
        mongo.dumpWordFrequency(sentiment, "hashtag");
        mongo.dumpWordFrequency(sentiment, "emoticon");
        mongo.dumpWordFrequency(sentiment, "emoji");
    }
    
}
