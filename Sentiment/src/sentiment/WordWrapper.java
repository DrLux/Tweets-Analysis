package sentiment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Sorrentino Luca
 */
public class WordWrapper implements Serializable{
    private String sentiment;
    private String type;
    private String word;
    private int frequency;
    Stemmer stemmer; 
    private Set<String> resources;
    private Map<String, Float> resorcesWithScore;

      
    public WordWrapper(String word, String sentiment, String type, int counter){
        this.word = word;
        this.sentiment=sentiment;
        this.type=type;
        this.frequency=counter;
        this.stemmer = new Stemmer();
        this.resources = new HashSet<String>();
        this.resorcesWithScore = new HashMap<String, Float>();
    }
    
    public void addResources(String res, float score){
        resorcesWithScore.put(res, score);
        addResources(res);
    }
    
    public void addResources(String res){
        this.resources.add(res);
    }
   

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Word: "+this.word+"\n Sentiment: "+sentiment+"\n Type: "+type+"\n Frequency: "+frequency; //To change body of generated methods, choose Tools | Templates.
    }

    public void setFrequency(int counter) {
        this.frequency = counter;
    }
    
    public int getFrequency(){
        return this.frequency;
    }

    @Override
    public boolean equals(Object obj) {
        WordWrapper w = (WordWrapper)obj;
        return getStemmedWord().equals(w.getStemmedWord())&&type.equals(w.getType())&&sentiment.equals(w.getSentiment()); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode()) + ((sentiment == null) ? 0 : sentiment.hashCode()) +((getStemmedWord() == null) ? 0 : getStemmedWord().hashCode());
        return result;
    }
    

    public String getSentiment() {
        return sentiment;
    }


    public String getWord() {
        return word;
    }
    
    public String getStemmedWord() {
        if (this.type.equals("WORD"))
            return this.stemmer.stem(this.word);
        return this.word;
    }
    
    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public void setResorcesWithScore(Map<String, Float> resorcesWithScore) {
        this.resorcesWithScore = resorcesWithScore;
    }

    public Map<String, Float> getResorcesWithScore() {
        return resorcesWithScore;
    }

    public Set<String> getResources() {
        return resources;
    }
    
    
}