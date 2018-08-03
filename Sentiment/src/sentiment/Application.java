package sentiment;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sorrentino Luca
 */
public class Application {
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        ProcessorSpark spark = new ProcessorSpark();
        Resources resources = new Resources(spark);
        MongoDb mongo = new MongoDb(27000, false);
        OracleDB oracle = new OracleDB("system","masera");
        List<String> AllSentiments = Arrays.asList("anger", "anticipation", "disgust", "fear", "joy", "sadness", "surprise", "trust");
        Sentiment senti;
        
        mongo.setCollection("anger");
        for (String sentiment : AllSentiments)
            senti = new Sentiment(sentiment,resources, spark, oracle, mongo);        
    } 
    
    
}
