/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imparandospark;

import java.io.Serializable;

/**
 *
 * @author sorre
 */
public class WordWrapper implements Serializable{
    private String sentiment;
    private String type;
    private int counter;
    private int score;
    
    public WordWrapper(String sentiment, String type, int counter){
        this.sentiment=sentiment;
        this.type=type;
        this.counter=counter;
        this.score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return sentiment+" "+type+" "+counter+" "+score; //To change body of generated methods, choose Tools | Templates.
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object obj) {
        WordWrapper w = (WordWrapper)obj;
        return type.equals(w.getType())&&sentiment.equals(w.getSentiment()); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode()) + ((sentiment == null) ? 0 : sentiment.hashCode());
        return result;
    }
    

    public String getSentiment() {
        return sentiment;
    }

    public int getCounter() {
        return counter;
    }
    
    
}
