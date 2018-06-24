package sentiment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Emoji {
    private String regexPattern = "";          
    private Pattern pattern = null;
    private Matcher matcher = null;
    private List<String> matchedEmojiList = null;
    
    
        public Emoji(){
            this.regexPattern = "[\\uD800\\uDC00-\\uDBFF\\uDFFF]";
            this.pattern = Pattern.compile(regexPattern);
        }
        
        public List<String> getMatchedEmotjiList(){
            return this.matchedEmojiList;
        }
        
        public String filterEmoji(String tweet) throws UnsupportedEncodingException{
                byte[] utf8 = tweet.getBytes("UTF-8");
                tweet = new String(utf8, "UTF-8");
                Matcher matcher = pattern.matcher(tweet);
                matchedEmojiList = new ArrayList<String>();
                   
                while (matcher.find()) {
                    matchedEmojiList.add(matcher.group());
                }

                return matcher.replaceAll(" ");
        }       
}