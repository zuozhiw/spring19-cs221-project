package edu.uci.ics.cs221.index;

import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import edu.uci.ics.cs221.index.InvertedIndexManager;
import edu.uci.ics.cs221.index.InvertedIndexSegmentForTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import edu.uci.ics.cs221.analysis.Analyzer;
import edu.uci.ics.cs221.storage.Document;

public class Team19FlushTest {
    Analyzer anal = new ComposableAnalyzer(new PunctuationTokenizer(), new PorterStemmer());
    InvertedIndexManager iim;
    String file = "./index";

    @Before
    public void setup() throws Exception {
        iim = iim.createOrOpen(file, anal);
        iim.DEFAULT_FLUSH_THRESHOLD = 3;
    }

    @After
    public void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get(file));
        iim.DEFAULT_FLUSH_THRESHOLD = 1000;
    }

    // test flush when flush() is called by user, whether the total number of segments is correct
    // and whether the first disk segment is set correctly.
    @Test
    public void testFlush1() throws Exception {
        iim.addDocument(new Document("cat dog"));
        iim.addDocument(new Document("cat elephant"));
        iim.flush();
        iim.addDocument(new Document("cat dog"));
        iim.addDocument(new Document("wolf dog"));
        iim.flush();
        assertEquals(2, iim.getNumSegments());

        Map<String, List<Integer>> PostingList = new HashMap<>();
        List<Integer> l = new LinkedList<Integer>();
        l.add(0);
        l.add(1);
        PostingList.put("cat", l);
        List<Integer> l2 = new LinkedList<Integer>();
        l2.add(0);
        PostingList.put("dog", l2);
        List<Integer> l3 = new LinkedList<Integer>();
        l3.add(1);
        PostingList.put("elephant", l);
        Map<Integer, Document> DocStore = new HashMap<>();
        DocStore.put(0, new Document("cat dog"));
        DocStore.put(1, new Document("cat elephant"));

        InvertedIndexSegmentForTest test = iim.getIndexSegment(0);
        assertEquals(PostingList, test.getInvertedLists());
        assertEquals(DocStore, test.getDocuments());
    }

    // test flush when flush() is called automatically when number of documents equals DEFAULT_FLUSH_THRESHOLD,
    // whether the total number of segments is correct and whether documentIterator is set correctly.
    @Test
    public void testFlush2() throws Exception {
        Document d1 = new Document("Information retrieval is the activity of obtaining information system resources relevant to an information need from a collection");
        Document d2 = new Document("Searches can be based on full-text or other content-based indexing");
        Document d3 = new Document("The process may then be iterated if the user wishes to refine the query");
        Document d4 = new Document("Web search engines are the most visible IR applications");

        iim.addDocument(d1);
        iim.addDocument(d2);
        iim.addDocument(d3);
        iim.flush();
        iim.addDocument(d4);
        iim.flush();
        assertEquals(2, iim.getNumSegments());

        Set<Document> result = new HashSet<>();
        Set<Document> expected = new HashSet<>();
        expected.add(d1);
        expected.add(d2);
        expected.add(d3);
        expected.add(d4);

        while(iim.documentIterator().hasNext()) {
            result.add(iim.documentIterator().next());
        }

        assertEquals(result, expected);
    }

}
