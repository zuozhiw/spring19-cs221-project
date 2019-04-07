package edu.uci.ics.cs221.analysis.wordbreak;

import edu.uci.ics.cs221.analysis.WordBreakTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WordBreakTokenizerTest {


	//test if the output is lowercased and stop words are removed
    @Test
    public void test1() {
        String text = "THISiswhATItoldyourI'llFRIendandI'llgoonlinecontactcan'tforget";
        List<String> expected = Arrays.asList("old", "i'll", "friend", "I'll","go","online","contact","can't","forget");

        WordBreakTokenizer tokenizer = new WordBreakTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));

    }

    //check if the program can handle strings like "whatevergreen" and constist of more than one frequenctly used words
    @Test
    public void test2(){
    	String text = "informationinforTHOUGHTFULLYcopyrightwhatevercontactablewhatevergreen";
    	List<String> expected = Arrays.asList("information", "thoughtfully", "copyright", "ever", "contact", "able","whatever", "green" );
    	
        WordBreakTokenizer tokenizer = new WordBreakTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));
    }


    //test if the exception part functions well and changes the string to lowercase.
    @Test
    public void test3(){
    	String test = "$reLLL(  ghn)iog*";
    	List<String> expected = Arrays.asList();
        //throw exception
    	WordBreakTokenizer tokenizer = new WordBreakTokenizer();

        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize(text));

    }

}
