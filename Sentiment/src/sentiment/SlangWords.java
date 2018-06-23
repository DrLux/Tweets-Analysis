/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentiment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author sorre
 */
public class SlangWords {
    static final Map<String, String> ALL_SLANG_WORDS; 
    static {
        Map<String, String> tmp = new LinkedHashMap<String, String>();
        tmp.put("afaik", "as far as i know");
        tmp.put("afk", "away from keyboard");
        tmp.put("asap", "as soon as possible");
        tmp.put("atk", "at the keyboard");
        tmp.put("atm", "at the moment");
        tmp.put("a3", "anytime, anywhere, anyplace");
        tmp.put("bak", "back at keyboard");
        tmp.put("bbl", "be back later");
        tmp.put("bbs", "be back soon");
        tmp.put("bfn/b4n", "bye for now");
        tmp.put("brb", "be right back");
        tmp.put("brt", "be right there");
        tmp.put("btw", "by the way");
        tmp.put("b4n", "bye for now");
        tmp.put("cu", "see you");
        tmp.put("cul8r", "see you later");
        tmp.put("cya", "see you");
        tmp.put("faq", "frequently asked questions");
        tmp.put("fc", "fingers crossed");
        tmp.put("fwiw", "for what it\"s worth");
        tmp.put("fyi", "for your information");
        tmp.put("gal", "get a life");
        tmp.put("gg", "good game");
        tmp.put("gmta", "great minds think alike");
        tmp.put("gr8", "great!");
        tmp.put("g9", "genius");
        tmp.put("ic", "i see");
        tmp.put("icq", "i seek you");
        tmp.put("ilu", "ilu, i love you");
        tmp.put("imho", "in my honest opinion");
        tmp.put("imo", "in my opinion");
        tmp.put("iow", "in other words");
        tmp.put("irl", "in real life");
        tmp.put("kiss", "keep it simple stupid");
        tmp.put("ldr", "long distance relationship");
        tmp.put("lmao", "laugh my ass off");
        tmp.put("lol", "laughing out loud");
        tmp.put("ltns", "long time no see");
        tmp.put("l8r", "later");
        tmp.put("mte", "my thoughts exactly");
        tmp.put("m8", "mate");
        tmp.put("nrn", "no reply necessary");
        tmp.put("oic", "oh i see");
        tmp.put("pita", "pain in the ass");
        tmp.put("prt", "party");
        tmp.put("prw", "parents are watching");
        tmp.put("qpsa?", "que pasa ?");
        tmp.put("rofl", "rolling on the floor laughing");
        tmp.put("roflol", "rolling on the floor laughing out loud");
        tmp.put("rotflmao", "rolling on the floor laughing my ass off");
        tmp.put("sk8", "skate");
        tmp.put("stats", "your sex and age");
        tmp.put("asl", "age sex location");
        tmp.put("thx", "thank you");
        tmp.put("ttfn", "ta-ta for now !");
        tmp.put("ttyl", "talk to you later");
        tmp.put("u", "you");
        tmp.put("u2", "you too");
        tmp.put("u4e", "yours for ever");
        tmp.put("wb", "welcome back");
        tmp.put("wtf", "what the fuck");
        tmp.put("wtg", "way to go!");
        tmp.put("wuf", "where are you from?");
        tmp.put("w8", "wait");
        tmp.put("7k", "sick,-d laugher");
        ALL_SLANG_WORDS = Collections.unmodifiableMap(tmp);
    }
}