/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author sorre
 */
public class Tweet {
    String sentiment = "";
    FilesHandler fh = null;
    StopWords sw = null;
    Stemmer stemmer = null;
    Emoji emo = null;
    
    
    public Tweet(String sentiment, FilesHandler fh) {
        this.sentiment = sentiment;
        this.fh = fh; 
        this.sw = new StopWords();
        this.stemmer = new Stemmer();
        this.emo = new Emoji();
    }
    
    public void loadTweets(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("tweets/dataset_dt_"+this.sentiment+"_60k.txt"));
            String line = "";
            try {
                while ((line = br.readLine()) != null) {
                    //System.out.println("Parsing: "+line);
                    parseTweet(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void parseTweet(String tweet){ 
        try {
            tweet = emo.filterEmoji(tweet);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] tokens = tweet.split("\\s+");
        String[] slangs = null;
        String stem_token = "";
        
        for (String t : tokens){
            if (Emoticons.ALL_EMOTICONS.containsKey(t))
                fh.db.InsertData(sentiment,t, "EMOTICON");
            else {
                if (SlangWords.ALL_SLANG_WORDS.containsKey(t)){
                    parseTweet(SlangWords.ALL_SLANG_WORDS.get(t));
                } else {
                    t = t.replaceAll("[,?!.;:\\/()& _+=<>'']", "");
                    if (t.startsWith("#")){
                        if (t.length() > 2)
                            fh.db.InsertData(sentiment,t, "HASHTAG");
                    } else {
                        if (!t.contains("URL") && !t.contains("USERNAME") && !sw.isStopword(t) && (t.length() > 2)){
                            stem_token = stemmer.stem(t.toLowerCase());
                            stem_token = stem_token.trim();
                            fh.insertWords(t,stem_token,sentiment);
                        }
                    }
                }
            }
        }
        List<String> matchedEmojiList = emo.getMatchedEmotjiList();
        for(int i=0; i < matchedEmojiList.size(); i++){
            fh.db.InsertData(sentiment,matchedEmojiList.get(i), "EMOJI");
        }
    }
}

