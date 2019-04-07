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
        List<String> expected = Arrays.asList("go", "movie", "theatre", "co", "worker", "yesterday");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test ' - split, ignoring stop words and uppercase to lowercase transfer",
                expected, tokenizer.tokenize(text));
    }

    @Test
    public void test3() {
        String text = "";
        List<String> expected = Arrays.asList();
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test empty input",
                expected, tokenizer.tokenize(text));
    }
    @Test
    public void test4() {
        String text = "         ";
        List<String> expected = Arrays.asList();
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();
        System.out.println(tokenizer.tokenize(text));
        assertEquals("test more  one spaces",
                expected, tokenizer.tokenize(text));
    }
    @Test
    public void test5() {
        String text = "    tomorrow";
        List<String> expected = Arrays.asList("tomorrow");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();
        System.out.println(tokenizer.tokenize(text));
        assertEquals("test more than one spaces between words",
                expected, tokenizer.tokenize(text));
    }
    @Test
    public void test6() {
        String text = "!,";
        List<String> expected = Arrays.asList();
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();
        System.out.println(tokenizer.tokenize(text));
        assertEquals("test only punctuations",
                expected, tokenizer.tokenize(text));
    }

    @Test
    public void test15() {
        String text = "Dog like Cat";
        List<String> expected = Arrays.asList("dog", "like", "cat");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test no stop words",
                expected, tokenizer.tokenize(text));
    }

    @Test
    public void test16() {
        String text =
                "herselF, me Own. ourS; tHiS? her thEirs were onLY; THese. Hidden oUrselVeS again, agaInsT hAs An? " +
                        "our have, he. oN. bEing aM CAn WiTh; So THRough? Them tHoSe. few. itS! Below! was? once Do Is! By of eACh. " +
                        "hImself; hiM; such? My; whO haViNg beEN haD She during! bEcAuse; other doEs; uNDeR oveR sHoUld JUSt! MoRe fOr Be " +
                        "into dID WHich thE, MySelf. hers; wHErE? They; now veRy aBouT NO information bUt tHemSeLVEs aRe hOw? tHeir NoT, bEFOrE? ANd wHat " +
                        "yourself; We froM? nor yOuR aboVe too wHY Or! yOurSelVeS theRE. DOn! dOwN; T. I sAme hERE uP; At. furThEr To; While; wILL; " +
                        "yours! bEtween? ThAt. you OfF theN as aLL both? uNTil; aNY Doing? tHAn iTsELf, ouT! WhEn IT whom; S, Some most A if. iN hIs! after.";

        List<String> expected = Arrays.asList("hidden", "information");
        PunctuationTokenizer tokenizer = new PunctuationTokenizer();

        assertEquals("test all stop words and punctuations: ",
                expected, tokenizer.tokenize(text));
    }


}
