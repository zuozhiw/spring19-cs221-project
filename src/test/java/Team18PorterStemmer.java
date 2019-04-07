import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.Stemmer;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

public class Team18PorterStemmer {
    public static String testStem(Stemmer stemmer, String sentence) {
        return Arrays.stream(sentence.split("\\s+"))
                .map(token -> stemmer.stem(token))
                .collect(joining(" "));
    }

    // test1 tests if -ed, -ing suffixes of a verb, -ly, -ory suffixes of a advert,
    // and -s suffixed of a noun will be removed to stem the word to their root
    @Test
    public void test1() {
        String original = "I worked hard recently because I am trying to get satisfactory grades for my finals.";
        String expected = "I work hard recent becaus I am try to get satisfactori grade for my final";

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }

    //
    @Test
    public void test2() {
        String original = "stemming is an important concept in computer science";
        String expected = "stem is an import concept in comput scienc";

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }
}
