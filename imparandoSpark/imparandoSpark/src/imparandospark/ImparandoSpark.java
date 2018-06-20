/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imparandospark;


import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

/**
 *
 * @author sorre
 */
public class ImparandoSpark {
    
    
    

    public static void main(final String[] args) throws InterruptedException {

        String inputPath;
      String outputPath;

      SparkSession  spark = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.myCollection")
      .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.myCollection")
      .getOrCreate();

      inputPath = "res.txt";
      outputPath = "out.txt";


      JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

      JavaRDD<String> res = jsc.textFile("res.txt");
      JavaRDD<String> words = jsc.textFile("word.txt");

      JavaRDD<String> parole = jsc.textFile("anger.txt").flatMap(s ->{
          return Arrays.asList(s.split("\\s+")).iterator();
      });



      JavaPairRDD<String, WordWrapper> words_frequency = words.mapToPair(s -> {
          String input[] = s.split(" ");
          return new Tuple2<>(input[0], new WordWrapper(input[1],input[2],1));
      }).reduceByKey((w1,w2) -> {
          return new WordWrapper(w1.getSentiment(),w1.getType(),w1.getCounter()+w2.getCounter());
      });



      JavaPairRDD<String, Integer> words_score = res.mapToPair(s -> {
          String input[] = s.split("\\s+");
          return new Tuple2<>(input[0],Integer.parseInt(input[1]));
      });

      JavaPairRDD<String, WordWrapper> words_match = words_frequency.join(words_score).mapToPair(w -> {
          WordWrapper temp = w._2._1;
          int score = w._2._2;
          temp.setScore(score);
          return new Tuple2<>(w._1,temp);
      });
      JavaPairRDD<String, WordWrapper> words_not_matched = words_frequency.subtractByKey(words_match).mapToPair(w -> {
          WordWrapper temp = w._2;
          int score = 0;
          temp.setScore(score);
          return new Tuple2<>(w._1,temp);
      });

      JavaPairRDD<String, WordWrapper> words_final = words_match.union(words_not_matched);

                
       // for(String line:parole.collect()){
         //   System.out.println("* "+line.toString());
        //}
                
        JavaPairRDD<String, WordWrapper> words_soglia = words_final.filter(w -> {
                if ( w._2.getCounter() >= 2)
                   return true;
                return false;
        });
            
        for(Tuple2<String, WordWrapper> line: words_soglia.collect()){
            System.out.println(line);
        }
            
        jsc.close();
       
    }
    
    public void stampa_su_disco(int opzioni, int soglia, int prime_enne, JavaPairRDD<String, WordWrapper> words_final){
        if (opzioni == 1){
           JavaPairRDD<String, WordWrapper> words_soglia = words_final.filter(w -> {
                if ( w._2.getCounter() >= soglia)
                   return true;
                return false;
           });
        } else {
            List<Tuple2<Integer,Tuple2>> top_words = words_final.mapToPair(s -> {
                Tuple2 temp = new Tuple2<>(s._1,s._2);
                return new Tuple2<>(s._2.getCounter(),temp);
            }).sortByKey()
            .top(prime_enne);
            
            for(Tuple2<Integer,Tuple2> line:top_words){
                System.out.println("freq: "+line._1+ "parola: "+line._2._1);
            }
        }
        
    }
}
