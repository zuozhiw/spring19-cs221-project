package edu.uci.ics.cs221.analysis;

import java.util.*;

/**
 * Project 1, task 1: Implement a simple tokenizer based on punctuations and white spaces.
 *
 * For example: the text "I am Happy Today!" should be tokenized to ["happy", "today"].
 *
 * Requirements:
 *  - White spaces (space, tab, newline, etc..) and punctuations provided below should be used to tokenize the text.
 *  - White spaces and punctuations should be removed from the result tokens.
 *  - All tokens should be converted to lower case.
 *  - Stop words should be filtered out. Use the stop word list provided in `StopWords.java`
 *
 */
public class PunctuationTokenizer implements Tokenizer {

    public static Set<String> punctuations = new HashSet<>();
    static {
        punctuations.addAll(Arrays.asList(",", ".", ";", "?", "!"));
    }

    public PunctuationTokenizer() {}

    public List<String> tokenize(String text) {
        text = text.toLowerCase();
        List<String> list = new ArrayList<String>();
        for(String pun : punctuations){
            pun =  "[" + pun + "]";
            text = text.replaceAll(pun," ");
        }

        String[] words = text.split("\\s+");

        for(int n = 0; n < words.length; n++){
            list.add(words[n]);
        }

        StopWords stopWordSet = new StopWords();
        list.removeAll(stopWordSet.stopWords);

        System.out.println(list);
        return list;

        //throw new UnsupportedOperationException("Punctuation Tokenizer Unimplemented");
    }

}
