package edu.uci.ics.cs221.index.Team3StressTest;

import edu.uci.ics.cs221.analysis.Analyzer;
import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.WordBreakTokenizer;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team3StressTest {
    @Test
    public void test1(){
        Map<String, List<Integer>> invertedLists = new HashMap<>();
        Map<Integer, Document> documents=new HashMap<>();
        InvertedIndexSegmentForTest test = new InvertedIndexSegmentForTest(invertedLists,documents);
    }
    @After
    public void after(){
        //test git

    }
}
