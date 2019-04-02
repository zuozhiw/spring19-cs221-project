package edu.uci.ics.cs221.analysis.punctuation;

import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Team0PunctuationTokenizerTest {

    @Test
    public void test1() {
        String text = "test    different\twhitespace\ncharacters\r\n";
        List<String> expected = Arrays.asList("test", "different", "whitespace", "characters");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));
    }

    @Test
    public void test2() {
        String text = "test, punctuations'll be removed. correctly?";
        List<String> expected = Arrays.asList("test", "punctuations'll", "removed", "correctly");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));
    }

}
