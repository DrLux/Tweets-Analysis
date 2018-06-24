package sentiment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sorrentino Luca
 */
public class TweetParser {
    private StopWords sw = null;
    private String sentiment = null;
    private List<WordWrapper> listWordWrapper = null;
    
    public TweetParser(){
        this.sentiment = sentiment;
        this.sw = new StopWords();        
        listWordWrapper = new ArrayList<WordWrapper>();
    }    
    
    public void setSentiment(String sent){
        this.sentiment = sent;
    }
    
    public String filterEmoji(Emoji emo, String line){
        try {
            line = emo.filterEmoji(line);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TweetParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> matchedEmoji= emo.getMatchedEmotjiList();
        for(int i=0; i < matchedEmoji.size(); i++){
           this.listWordWrapper.add(new WordWrapper(matchedEmoji.get(i), this.sentiment, "EMOJI", 1 ));
        }
        return line;
    }
    
    public void processWord(String word){
        word = word.trim();
        this.listWordWrapper.add(new WordWrapper(word, this.sentiment, "WORD", 1));
    }
    
    public void processSlangWord(String slangword){
        String expanded_words = SlangWords.ALL_SLANG_WORDS.get(slangword);
        String[] words = expanded_words.split("\\s+");
        for (String word : words)
            processWord(word);
    }
    
    public void process_tweet(String tweet){
        tweet = filterEmoji(new Emoji(), tweet);                   
        String[] tokens = tweet.split("\\s+");
        
        for (String t : tokens){
            if (Emoticons.ALL_EMOTICONS.containsKey(t)){
                this.listWordWrapper.add(new WordWrapper(t, this.sentiment, "EMOTICON", 1));
            } else {
                if (SlangWords.ALL_SLANG_WORDS.containsKey(t)){
                    processSlangWord(t); 
                } else {
                    t = t.replaceAll("[,?!.;:\\/()& _+=<>'']", "");
                    if (t.startsWith("#")){
                        if (t.length() > 2){
                            this.listWordWrapper.add(new WordWrapper(t, this.sentiment, "HASHTAG", 1));
                        }
                    } else {
                        if (!t.contains("URL") && !t.contains("USERNAME") && !sw.isStopword(t) && (t.length() > 2)){
                            processWord(t);
                        }
                    }
                }
            }
        }
    }
    
    public List<WordWrapper> getListWordWrapper() {
        return listWordWrapper;
    }
}
