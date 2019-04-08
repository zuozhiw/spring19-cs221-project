package edu.uci.ics.cs221.analysis.stemmer;

import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.Stemmer;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

public class Team23PorterStemmerTest {

    public static String testStem(Stemmer stemmer, String sentence) {
        return Arrays.stream(sentence.split("\\s+"))
                .map(token -> stemmer.stem(token))
                .collect(joining(" "));
    }

    @Test
    public void test1() {
        String originalString1a = "caresses ponies caress cats";
        String expectedString1a = "caress poni caress cat";

        String originalString1b = "agreed feed plastered bled motoring sing";
        String expectedString1b = "agree feed plaster bled motor sing";

        String originalString1bCleanup = "conflated troubled sized hopping fizzed failing filing";
        String expectedString1bCleanup = "conflate trouble size hop fizz fail file";

        String originalString1c = "happy sky";
        String expectedString1c = "happi sky";

        String original = originalString1a + originalString1b + originalString1bCleanup + originalString1c;
        String expected = expectedString1a + expectedString1b + expectedString1bCleanup + expectedString1c;

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }

    @Test
    public void test2() {
        String original = "relational conditional rational valenci hesitanci digitizer conformabli radicalli " +
                "differentli vileli analogousli vietnamization predication operator feudalism decisiveness " +
                "hopefulness callousness formaliti sensitiviti sensibiliti";
        String expected = "relate condition rational valence hesitance digitize conformable radical different vile " +
                "analogous vietnamize predicate operate feudal decisive hopeful callous formal sensitive sensible";

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }

    @Test
    public void test3() {
        String original = "triplicate formative formalize electriciti electrical hopeful goodness";
        String expected = "triplic form formal electric electric hope good";

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }

    @Test
    public void test4() {
        String original = "revival  allowance inference airliner gyroscopic adjustable defensible irritant replacement " +
                "adjustment dependent adoption homologou communism activate angulariti homologous effective bowdlerize";
        String expected = "reviv allow infer airlin gyroscop adjust defens irrit replac adjust depend adopt homolog " +
                "commun activ angular homolog effect bowdler";

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }

    @Test
    public void test5() {
        String original = "probate rate cease controll roll";
        String expected = "probat rate ceas control roll";

        PorterStemmer porterStemmer = new PorterStemmer();
        assertEquals(expected, testStem(porterStemmer, original));
    }
}
