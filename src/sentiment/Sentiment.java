/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentiment;
import java.io.IOException;
/**
 *
 * @author sorre
 */
public class Sentiment {

    /**
     * https://www.wordclouds.com/
     */
    public static void main(String[] args) throws IOException {
        Database db = new Database("system","masera");
        MongoDB mongo = new MongoDB();
        //FilesHandler fh = new FilesHandler(db, false, mongo);
        
        //mongo.provaMap();

        
        //mongo.inserWordInSentiment("abuse", "abuse", "anger", fh.getListFiles());
        //mongo.inserWordInSentiment("abuse", "abuse", "anger", fh.getListFiles());
        //mongo.inserWordInSentiment("luigi", "luigi", "anger", fh.getListFiles());
        //mongo.inserWordInSentiment("stir", "stir", "anger", fh.getListFiles());
        /*String[] all_sentiments = {"anger", "anticipation", "joy", "trust", "fear", "surprise", "sadness", "disgust"};
        Tweet tweets = null;
        System.out.println("Inizializzati tutti i DB");
        for (String sentiment : all_sentiments){
            fh.splitTweets(sentiment);
            fh.createSentiment(sentiment);
            System.out.println("Avvio parsing di: "+sentiment);
            tweets = new Tweet(sentiment, fh);
            tweets.loadTweets();
            System.out.println("Fine caricamente tweet "+ sentiment);
            //fh.print_statistics(sentiment, "WORD", 0);
            //fh.print_statistics(sentiment, "HASHTAG", 0);
            //fh.print_statistics(sentiment, "EMOTICON", 0);
            //fh.print_statistics(sentiment, "EMOJI", 0);
        }
        //db.dropDB("anger", fh.getListFiles());
        */
    }
}
   
