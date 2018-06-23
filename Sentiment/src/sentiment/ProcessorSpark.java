
package sentiment;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;


/**
 * @author Sorrentino Luca
 */
public class ProcessorSpark{
    JavaSparkContext jsc = null; 

    public ProcessorSpark(){
        SparkSession spark = SparkSession.builder()
        .master("local")
        .appName("Sentiment")
        .getOrCreate();
        this.jsc = new JavaSparkContext(spark.sparkContext());
    }
    
    
    public JavaPairRDD<String, WordWrapper> preprocess_tweet (String path, String sentiment){ 
        
        //Parse tweet
        JavaRDD<WordWrapper> tokens =  jsc.textFile(path).flatMap(tweet ->{
            TweetParser parser = new TweetParser();
            parser.setSentiment(sentiment);
            parser.process_tweet(tweet);
            return parser.getListWordWrapper().iterator();           
        });
        
        //Count frequency
        JavaPairRDD<String, WordWrapper> words_frequency = tokens.mapToPair(wrapper -> {
            return new Tuple2<>(wrapper.getStemmedWord(), wrapper);
        }).reduceByKey((w1,w2) -> {
            return new WordWrapper(w1.getWord(), w1.getSentiment(),w1.getType(),w1.getFrequency()+w2.getFrequency());
        });
        
        
        return words_frequency;        
    }
          
    public JavaPairRDD<String, WordWrapper> filterResults(int threshold, JavaPairRDD<String, WordWrapper> preprocessed_tweet){
        return preprocessed_tweet.filter(w -> {
                if ( w._2.getFrequency() >= threshold)
                   return true;
                return false;
        });
    }
    
    public void quitSpark(){
        jsc.close();
    }    
    
    public JavaPairRDD<String, Float> loadResource(String filename, String folder){
        //load data from resources
        JavaPairRDD<String, Float> res = jsc.textFile("resources/"+folder+"/"+filename+".txt").mapToPair(s -> {
            return new Tuple2<>(s,new Float(0));
        }).reduceByKey((w1,w2) -> {
            return w2;
        });
        
        return res;
    }
    
    public JavaPairRDD<String, Float> loadScoredResource(String filename){
        //load data from resources
        JavaPairRDD<String, Float> res = jsc.textFile("resources/with_score/"+filename+".txt").mapToPair(s -> {
            String input[] = s.split("\\s+");
            return new Tuple2<>(input[0],Float.parseFloat(input[1]));
        }).reduceByKey((w1,w2) -> {
            return w2;
        });
        
        return res;
    }
    
    
    public JavaPairRDD<String, WordWrapper> process_resource(JavaPairRDD<String, Float> res, JavaPairRDD<String, WordWrapper> preprocessed_tweet, String filename){      
        //Join tra i tweet al data_resources
        JavaPairRDD<String, WordWrapper> words_match = preprocessed_tweet.join(res).mapToPair(w -> {
            WordWrapper temp = w._2._1;
            temp.addResources(filename);
            return new Tuple2<>(w._1,temp);
        });
        
        JavaPairRDD<String, WordWrapper> words_not_matched = preprocessed_tweet.subtractByKey(words_match);
        return words_match.union(words_not_matched);
    }
    
    public void printJavardd(JavaPairRDD<String, WordWrapper> rdd){
        for(Tuple2<String, WordWrapper> line : rdd.collect()){
            System.out.println( line._2.getSentiment()+line._2.getWord()+ line._2.getType()+ line._2.getResources()+ line._2.getResorcesWithScore()+ line._2.getFrequency());
            
        }
    }
    
    public void printJavarddFloat(JavaPairRDD<String, Float> rdd){
        for(Tuple2<String, Float> line : rdd.collect()){
            //System.out.println( line._2.getSentiment()+line._2.getWord()+ line._2.getType()+ line._2.getResources()+ line._2.getResorcesWithScore()+ line._2.getFrequency());
            System.out.println(line._1+" "+ line._2);
        }
    }
    
    public JavaPairRDD<String, WordWrapper> process_resource_with_score(JavaPairRDD<String, Float> words_score, JavaPairRDD<String, WordWrapper> preprocessed_tweet, String filename){        
        
        //Join tra i tweet al data_resources
        JavaPairRDD<String, WordWrapper> words_match = preprocessed_tweet.join(words_score).mapToPair(w -> {
            WordWrapper temp = w._2._1;
            temp.addResources(filename, w._2._2);
            return new Tuple2<>(w._1,temp);
        });
        
        JavaPairRDD<String, WordWrapper> words_not_matched = preprocessed_tweet.subtractByKey(words_match);
              
        return words_match.union(words_not_matched);        
    }
    
}
