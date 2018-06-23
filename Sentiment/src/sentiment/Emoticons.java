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
 * @author Sorrentino Luca
 */

public class Emoticons {
    static final Map<String, String> ALL_EMOTICONS;
        static {
            Map<String, String> tmp = new LinkedHashMap<String, String>();
            tmp.put(":‑'\')", "Happy face or smiley");
            tmp.put(":'\')", "Happy face or smiley");
            tmp.put(":-'\']", "Happy face or smiley");
            tmp.put(":'\']", "Happy face or smiley");
            tmp.put(":-3", "Happy face smiley");
            tmp.put(":3", "Happy face smiley");
            tmp.put(":->", "Happy face smiley");
            tmp.put(":>", "Happy face smiley");
            tmp.put("-_-", "Bored");
            tmp.put("T.T", "Crying");
            tmp.put(">.>", "unknown");
            tmp.put("( *)>", "penguin left");
            tmp.put("<(* )", "penguin left");
            tmp.put("^^", "happiness");
            tmp.put("^O^", "happiness");
            tmp.put("^.^", "happiness");
            tmp.put("T_T", "Crying");
            tmp.put(":*", "Kissing");
            tmp.put(".-.", "Kissing");
            tmp.put(":‑D", "Laughing, big grin or laugh with glasses");
            tmp.put(":D", "Laughing, big grin or laugh with glasses");
            tmp.put("8‑D", "Laughing, big grin or laugh with glasses");
            tmp.put("8D", "Laughing, big grin or laugh with glasses");
            tmp.put("X‑D", "Laughing, big grin or laugh with glasses");
            tmp.put("XD", "Laughing, big grin or laugh with glasses");
            tmp.put("=D", "Laughing, big grin or laugh with glasses");
            tmp.put("=3", "Laughing, big grin or laugh with glasses");
            tmp.put("T_T", "Crying");
            tmp.put(":‑c", "Frown, sad, andry or pouting");
            tmp.put(":c", "Frown, sad, andry or pouting");
            tmp.put(":‑<", "Frown, sad, andry or pouting");
            tmp.put(":<", "Frown, sad, andry or pouting");
            tmp.put(":@", "Frown, sad, andry or pouting");
            tmp.put("D‑':", "Horror");
            tmp.put("D:<", "Disgust");
            tmp.put("D:", "Sadness");
            tmp.put("D8", "Great dismay");
            tmp.put("D;", "Great dismay");
            tmp.put("D=", "Great dismay");
            tmp.put("DX", "Great dismay");
            tmp.put(":‑O", "Surprise");
            tmp.put(":O", "Surprise");
            tmp.put(":‑o", "Surprise");
            tmp.put(":o", "Surprise");
            tmp.put(":-0", "Shock");
            tmp.put("8‑0", "Yawn");
            tmp.put(">:O", "Yawn");
            tmp.put(":X", "Kiss");
            tmp.put(":‑,", "Wink or smirk");
            tmp.put(";D", "Wink or smirk");
            tmp.put(":‑P", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(":P", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put("X‑P", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put("XP", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(":‑Þ", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(":Þ", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(":b", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put("d:", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put("=p", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(">:P", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(":‑/", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put(":/", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put(":-[.]", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put(">:/", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put("=/", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put(":L", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put("=L", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put(":S", "Skeptical, annoyed, undecided, uneasy or hesitant");
            tmp.put(":$", "Embarrassed or blushing");
            tmp.put(":‑x", "Sealed lips or wearing braces or tongue-tied");
            tmp.put(":x", "Sealed lips or wearing braces or tongue-tied");
            tmp.put(":‑#", "Sealed lips or wearing braces or tongue-tied");
            tmp.put(":#", "Sealed lips or wearing braces or tongue-tied");
            tmp.put(":‑&", "Sealed lips or wearing braces or tongue-tied");
            tmp.put(":&", "Sealed lips or wearing braces or tongue-tied");
            tmp.put("0:‑3", "Angel, saint or innocent");
            tmp.put("0:3", "Angel, saint or innocent");
            tmp.put(":‑b", "Tongue sticking out, cheeky, playful or blowing a raspberry");
            tmp.put(":‑J", "Tongue-in-cheek");
            tmp.put(":-###..", "Being sick");
            tmp.put(":###..", "Being sick");
            tmp.put(";_;", "Sad or Crying");
            tmp.put(";-;", "Sad or Crying");
            tmp.put(";n;", "Sad or Crying");
            tmp.put(";;", "Sad or Crying");
            tmp.put("QQ", "Sad or Crying");
            tmp.put("Q_Q", "Sad or Crying");
            tmp.put(":O o_O", "Surprised");
            tmp.put("o_0", "Surprised");
            tmp.put("8==D", "Penis");
            tmp.put("oO", "Surprised");
            tmp.put("<3", "Love");
            ALL_EMOTICONS = Collections.unmodifiableMap(tmp);
        }
}