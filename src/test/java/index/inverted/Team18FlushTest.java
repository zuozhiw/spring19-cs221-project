package index.inverted;

import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.storage.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Team18FlushTest {
    public InvertedIndexManager manager;
    String folderPath = "./index/Team18FlushTest";

    @Before
    public void initialize(){
        manager = InvertedIndexManager.createOrOpen(folderPath, new ComposableAnalyzer(new PunctuationTokenizer(), new PorterStemmer()));
    }

    // test flush() when called and numSegments less than DEFAULT_FLUSH_THRESHOLD
    @Test
    public void test1(){
        Document doc1 = new Document("kitten, bunny");
        Document doc2 = new Document("bunny");
        manager.addDocument(doc1);
        manager.addDocument(doc2);
        manager.flush();
        int expectedNumSegments = 1;
        assertEquals(expectedNumSegments, manager.getNumSegments());

        InvertedIndexSegmentForTest iiTestSegment = manager.getIndexSegment(0);
        Map<String, List<Integer>> invertedLists = iiTestSegment.getInvertedLists();
        Map<Integer, Document> documents = iiTestSegment.getDocuments();
        assertEquals(2, invertedLists.size());
        assertEquals(2, documents.size());

        PorterStemmer ps = new PorterStemmer();
        List<Integer> invertedList = invertedLists.get(ps.stem("kitten"));
        List<Integer> expectedInvertedList = Arrays.asList(0);
        assertEquals(expectedInvertedList, invertedList);


    }

    // test flush() when no document is added
    @Test
    public void test2(){
        manager.flush();
    }

    // test flush() when DEFAULT_FLUSH_THRESHOLD documents is added
    @Test
    public void test3(){

    }

}
