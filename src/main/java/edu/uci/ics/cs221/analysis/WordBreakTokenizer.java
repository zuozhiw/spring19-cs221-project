package edu.uci.ics.cs221.analysis;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project 1, task 2: Implement a Dynamic-Programming based Word-Break Tokenizer.
 *
 * Word-break is a problem where given a dictionary and a string (text with all white spaces removed),
 * determine how to break the string into sequence of words.
 * For example:
 * input string "catanddog" is broken to tokens ["cat", "and", "dog"]
 *
 * We provide an English dictionary corpus with frequency information in "resources/cs221_frequency_dictionary_en.txt".
 * Use frequency statistics to choose the optimal way when there are many alternatives to break a string.
 * For example,
 * input string is "ai",
 * dictionary and probability is: "a": 0.1, "i": 0.1, and "ai": "0.05".
 *
 * Alternative 1: ["a", "i"], with probability p("a") * p("i") = 0.01
 * Alternative 2: ["ai"], with probability p("ai") = 0.05
 * Finally, ["ai"] is chosen as result because it has higher probability.
 *
 * Requirements:
 *  - Use Dynamic Programming for efficiency purposes.
 *  - Use the the given dictionary corpus and frequency statistics to determine optimal alternative.
 *      The probability is calculated as the product of each token's probability, assuming the tokens are independent.
 *  - A match in dictionary is case insensitive. Output tokens should all be in lower case.
 *  - Stop words should be removed.
 *  - If there's no possible way to break the string, throw an exception.
 *
 */
public class WordBreakTokenizer implements Tokenizer {
    Map<String, String> dict;

    public WordBreakTokenizer() {
        try {
            // load the dictionary corpus
            URL dictResource = WordBreakTokenizer.class.getClassLoader().getResource("cs221_frequency_dictionary_en.txt");
            List<String> dictLines = Files.readAllLines(Paths.get(dictResource.toURI()));
            this.dict = new HashMap<String, String>();
            for(String word : dictLines){
                this.dict.put(word.split(" ")[0], word.split(" ")[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> tokenize(String text) {
        text = text.toLowerCase();
        int len = text.length();

        // break the string based on dynamic programming
        List<String> list = new ArrayList<String>();
        Map<Integer,List<String>> substr = new HashMap<Integer, List<String>>();
        list.add("");
        substr.put(len,list);
        for(int start= len-1; start >= 0; start--){
            List<String> temp = new ArrayList<String>();
            for(int end=start+1; end<=len;end++){
                if(this.dict.containsKey(text.substring(start,end))){
                    for(String word : substr.get(end)){
                        temp.add(text.substring(start,end)+(word.isEmpty() ? "" : " ")+word);
                    }
                }
            }
            substr.put(start, temp);
        }

        // to sum up frequencies of all words in dictionary
        BigDecimal freqSum = new BigDecimal("0");
        for(String word : this.dict.keySet()){
            freqSum = freqSum.add(new BigDecimal(this.dict.get(word)));
        }

        list = substr.get(0);
        if(list.isEmpty()){
            throw new UnsupportedOperationException("There's no possible way to break the string.");
        }
        String match = ""; Double maxFreq = 0.0;
        for(int i = 0; i<list.size(); i++){
            String[] arr = list.get(i).split(" ");
            BigDecimal currFreq = new BigDecimal("0");
            for(String word : arr){
                currFreq = currFreq.add(new BigDecimal(this.dict.get(word)));
            }
            Double freq = Math.log(currFreq.divide(freqSum, 32, BigDecimal.ROUND_UP).doubleValue());
//            System.out.println(freq+":"+list.get(i));
            if(maxFreq == 0.0 || maxFreq < freq){
                maxFreq = freq;
                match = list.get(i);
            }
        }
//        System.out.println(maxFreq);
        List<String> tokerizer = new ArrayList<String>();
        String[] array = match.split(" ");
        for(String word : array){
            tokerizer.add(word);
        }
//        System.out.print(tokerizer);
        StopWords stopWordSet = new StopWords();
        tokerizer.removeAll(stopWordSet.stopWords);
        System.out.print(tokerizer);
        return tokerizer;
    }

}
