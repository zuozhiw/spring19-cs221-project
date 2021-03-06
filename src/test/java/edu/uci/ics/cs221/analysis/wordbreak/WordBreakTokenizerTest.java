package edu.uci.ics.cs221.analysis.wordbreak;

import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import edu.uci.ics.cs221.analysis.WordBreakTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WordBreakTokenizerTest {

    @Test
    public void test1() {
        String text = "IamHappyToday";
        List<String> expected = Arrays.asList("happy", "today");
        WordBreakTokenizer tokenizer = new WordBreakTokenizer();

        assertEquals(expected, tokenizer.tokenize(text));

    }

}
