package edu.uci.ics.cs221.Team5PunctuationTokenizerTest;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Testcase {

    @Test
    public void test1() {
        String text = "He didn't pass The Exam.";
        List<String> expected = Arrays.asList("pass", "exam");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test ' split, ignoring stop words and uppercase to lowercase transfer",
                expected, tokenizer.tokenize(text));
    }
    @Test
    public void test2() {
        String text = "Did she go to movie theatre with her co-Worker yesterday?";
        List<String> expected = Arrays.asList("go", "movie","theatre","co","worker","yesterday");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test ' - split, ignoring stop words and uppercase to lowercase transfer",
                expected, tokenizer.tokenize(text));
    }

}
