package edu.uci.ics.cs221;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class Team6PunctuationTokenizerTest {
    @Test
    public void test1() {
        /*
        - Test Case 1 is to test if the tokenizer handles different
        white spaces correctly by having characters of \t, \n and white spaces in the input text.

        - Test Case 2 is to test if the tokenizer can handles splits the text by given punctuations marks
        and removes them correctly. The punctuations include ",", ".", ";", "?" and "!". Moreover,
        punctuation marks that are not on the list should not be considered, such as i'am and four-year-old.

        - Test Case 3 is to test if the tokenizer can convert all tokens into lower case.

        */
        String text1 = "\tgood example\nyes great example";

        String text2 = "good, four-year-old children. never asia come? it's china! thanks.";
        String text3 = "";

        System.out.println(text2);
        List<String> expected1 = Arrays.asList("good", "example",
               "yes", "great", "example");
        List<String> expected2 = Arrays.asList("good", "four-year-old", "children", "never", "asia",
                "come", "it's", "china", "thanks");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

//        assertEquals(expected, tokenizer.tokenize(text1));
//        assertEquals(expected, tokenizer.tokenize(text2));
    }
}
