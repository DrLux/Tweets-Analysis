/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentiment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sorre
 */
public class StopWords {
    public static String[] stopwords = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now","later", "noth", "instead", "insofar", "theyll", "through", "whenc", "wish", "ye", "by", "except", "get", "her", "thorough", "after", "two", "she", "happen", "gone", "wherev", "come", "that", "gotten", "welcom", "your", "appear", "sinc", "even", "twice", "sensibl", "with", "whither", "doe", "often", "give", "up", "tell", "there", "now", "among", "abov", "asid", "immedi", "hasnt", "sup", "everybodi", "becom", "thank", "meanwhil", "of", "youd", "okay", "so", "therein", "mani", "down", "whole", "unfortun", "where", "avail", "possibl", "less", "itd", "thanx", "themselv", "ignor", "wonder", "below", "nowher", "place", "been", "it", "beforehand", "sure", "correspond", "enough", "inde", "sorri", "edu", "none", "alon", "elsewher", "cours", "must", "inc", "whether", "mostli", "ought", "co", "last", "along", "greet", "necessari", "same", "way", "caus", "chang", "com", "neither", "am", "somebodi", "had", "when", "rd", "certain", "should", "just", "current", "noon", "particularli", "too", "afterward", "also", "name", "ive", "did", "brief", "a", "allow", "than", "fifth", "yet", "ha", "howev", "almost", "otherwis", "somewhat", "upon", "other", "wherebi", "actual", "still", "th", "someon", "isnt", "unless", "via", "onc", "describ", "cs", "accord", "near", "saw", "everyon", "consid", "differ", "nobodi", "definit", "itself", "least", "want", "six", "quit", "wont", "weve", "right", "think", "arent", "himself", "becam", "realli", "nor", "shall", "hereupon", "as", "went", "around", "mayb", "alway", "next", "unlik", "therefor", "outsid", "hereaft", "or", "not", "and", "these", "anyway", "exampl", "take", "said", "hadnt", "five", "im", "no", "at", "sever", "inner", "onto", "plu", "will", "novel", "befor", "werent", "ltd", "accordingli", "myself", "nevertheless", "have", "him", "need", "moreov", "whenev", "serious", "associ", "four", "herself", "between", "shouldnt", "further", "then", "herebi", "yourselv", "aint", "seriou", "what", "furthermor", "overal", "wa", "more", "never", "mere", "came", "forth", "one", "un", "me", "whi", "specifi", "appreci", "theyd", "into", "whereaft", "each", "indic", "the", "let", "you", "wouldnt", "everi", "usual", "an", "seen", "abl", "contain", "use", "i", "certainli", "although", "across", "therebi", "look", "viz", "ill", "on", "youll", "unto", "whose", "wed", "amongst", "to", "anyhow", "howbeit", "over", "thoroughli", "yourself", "herein", "such", "exactli", "again", "like", "zero", "thereupon", "wasnt", "three", "well", "itll", "follow", "wherein", "done", "formerli", "within", "off", "perhap", "help", "pleas", "those", "beyond", "in", "whom", "thereaft", "cant", "anoth", "keep", "them", "behind", "mean", "secondli", "might", "normal", "sometim", "oh", "most", "eight", "ie", "took", "we", "without", "despit", "ever", "from", "say", "selv", "vs", "few", "theyv", "may", "new", "somehow", "wherea", "their", "nd", "entir", "old", "thu", "eg", "togeth", "how", "but", "see", "appropri", "away", "taken", "us", "reason", "sent", "second", "ex", "anywher", "sub", "latter", "late", "our", "becaus", "thi", "nine", "soon", "for", "got", "littl", "everywher", "regard", "obvious", "third", "is", "ask", "re", "truli", "probabl", "ff", "particular", "et", "theyr", "would", "havent", "goe", "etc", "ani", "he", "hello", "whoever", "qv", "best", "someth", "were", "somewher", "tri", "former", "thenc", "hither", "here", "cannot", "know", "downward", "concern", "valu", "ts", "rel", "both", "they", "believ", "could", "hope", "que", "variou", "henc", "until", "besid", "aw", "doesnt", "far", "regardless", "presum", "known", "anybodi", "lest", "respect", "onli", "id", "dure", "youv", "go", "everyth", "tend", "hardli", "provid", "which", "inward", "better", "own", "toward", "inasmuch", "nearli", "ok", "rather", "seven", "els", "throughout", "cmon", "clearli", "mainli", "under", "while", "apart", "all", "if", "out", "much", "against", "either", "hi", "kept", "my", "couldnt", "though", "can", "given", "self", "who", "anyth", "per", "anyon", "are", "be", "consequ", "first", "especi", "some", "latterli", "whatev", "ourselv", "do", "didnt", "non", "whereupon", "seem", "dont", "veri", "about", "thru", "alreadi"};
    public static Set<String> stopWordSet = null;
    
    public StopWords(){ 
        stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
    }
    
    public static boolean isStopword(String word) {
        if(word.length() < 2) return true;
        if(word.charAt(0) >= '0' && word.charAt(0) <= '9') return true; //remove numbers, "25th", etc
        if(stopWordSet.contains(word)) return true;
        else return false;
    }
    /*public static String stemString(String string) {
		return new Stemmer().stem(string);
    }*/
    
    
}
