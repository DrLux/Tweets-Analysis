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
        FilesHandler fh = new FilesHandler(db, true);
        //FilesHandler fh = new FilesHandler(db, false);
        String[] all_sentiments = {"anger", "anticipation", "joy", "trust", "fear", "surprise", "sadness", "disgust"};
        Tweet tweets = null;
        for (String sentiment : all_sentiments){
            fh.createSentiment(sentiment);
            tweets = new Tweet(sentiment, fh);
            tweets.loadTweets();
            fh.print_statistics(sentiment, "WORD", 0);
            fh.print_statistics(sentiment, "HASHTAG", 0);
            fh.print_statistics(sentiment, "EMOTICON", 0);
            fh.print_statistics(sentiment, "EMOJI", 0);
        }
        //db.dropDB("anger", fh.getListFiles());
    }
}
   
