package edu.uci.ics.cs221.Team5PunctuationTokenizerTest;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Testcase {

    @Test
    public void test1() {
        String text = "He did not pass The Exam, did he?";
        List<String> expected = Arrays.asList("pass", "exam");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test ,? split, ignoring stop words and uppercase to lowercase transfer",
                expected, tokenizer.tokenize(text));
    }
    @Test
    public void test2() {
        String text = "Thanks God! I found my wallet there.";
        List<String> expected = Arrays.asList("thanks", "god","found","wallet");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();
        assertEquals("test ! . split, ignoring stop words and uppercase to lowercase transfer",
                expected, tokenizer.tokenize(text));
    }

}
