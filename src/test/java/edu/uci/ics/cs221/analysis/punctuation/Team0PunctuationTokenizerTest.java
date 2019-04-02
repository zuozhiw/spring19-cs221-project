package edu.uci.ics.cs221.analysis.punctuation;

import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Team0PunctuationTokenizerTest {

    /**
     * Tests if the tokenizer handles different white spaces correctly by having characters
     * such as \t, \n in the input text.
     */
    @Test
    public void test1() {
        String text = "test    different\twhitespace\ncharacters\r\n";
        List<String> expected = Arrays.asList("test", "different", "whitespace", "characters");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));
    }

    /**
     * Tests if the tokenizer splits the text by punctuation marks and removes them correctly.
     * Moreover, punctuation marks that is not in the list should not be considered, such as it'll
     */
    @Test
    public void test2() {
        String text = "test. it'll remove,them correctly?";
        List<String> expected = Arrays.asList("test", "it'll", "remove", "them", "correctly");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));
    }

}
