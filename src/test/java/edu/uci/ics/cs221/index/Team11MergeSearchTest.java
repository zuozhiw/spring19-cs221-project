package edu.uci.ics.cs221.index;

import edu.uci.ics.cs221.analysis.Analyzer;
import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.io.File;

public class Team11MergeSearchTest {
    String indexPath = "index_path";

    // Back to December by Taylor Swift
    Document[] documents = new Document[] {
            new Document("import edu uci ics cs221 analysis  Analyzer"),
            new Document("import edu uci ics cs221 analysis  ComposableAnalyzer"),
            new Document("import edu uci ics cs221 analysis  PorterStemmer"),
            new Document("import edu uci ics cs221 analysis  PunctuationTokenizer"),
            new Document("import edu uci ics cs221 index     inverted            InvertedIndexManager"),
            new Document("import edu uci ics cs221 storage   Document")
    };

    Analyzer analyzer;
    InvertedIndexManager index;

    @Before
    public void before() {
        analyzer = new ComposableAnalyzer(new PunctuationTokenizer(), new PorterStemmer());
        index = InvertedIndexManager.createOrOpen(indexPath, analyzer);
    }

    @After
    public void clean() {
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 1000;
        InvertedIndexManager.DEFAULT_MERGE_THRESHOLD = 8;

        try {
            File file = new File(indexPath);

            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong when deleting file");
        }
    }
    
    @Test
    public void mergeSearchTest1() {
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 1;
        InvertedIndexManager.DEFAULT_MERGE_THRESHOLD = 4;

        for (Document doc : documents) {
            index.addDocument(doc);
            assert index.getNumSegments() <= InvertedIndexManager.DEFAULT_MERGE_THRESHOLD;
        }
    }

    /* Test an easy case of flush threshold=1 and merge threshold=2,
     * after we add 2 documents, the number of segments after merging
     * should be 1
     */
    @Test
    public void mergeSearchTest2() {
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 1;
        InvertedIndexManager.DEFAULT_MERGE_THRESHOLD = 2;

        index.addDocument(documents[0]);
        index.addDocument(documents[1]);
        int expectedNumSegments = 1;
        assertEquals(expectedNumSegments, index.getNumSegments());
    }

    /* Test when the number of segments is 3 and we forcefully use
     * mergeAllSegments() and then the result of merging should still be 3
     */
    @Test
    public void mergeSearchTest3() {
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 2;

        for (int i = 0; i < 6 ; i++){
            index.addDocument(documents[i]);
        }
        index.mergeAllSegments();
        int expectedNumSegments = 3;
        assertEquals(expectedNumSegments, index.getNumSegments());
    }
    /* Test when the flush and merge thresholds are 1 and 4 respectively,
     * after merging the number of segments should be 3 and we also check
     * the detailed docIDs of the keywords
     */
    @Test
    public void mergeSearchTest4() {
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 1;
        InvertedIndexManager.DEFAULT_MERGE_THRESHOLD = 4;

        for (Document doc : documents) {
            index.addDocument(doc);
        }
        int expectedNumSegments = 3;
        assertEquals(expectedNumSegments, index.getNumSegments());
        InvertedIndexSegmentForTest it = index.getIndexSegment(0);
        Map<String, List<Integer>> invertedLists = it.getInvertedLists();
        List<Integer> docIds = invertedLists.get("import");
        List<Integer> expectedDocIds = Arrays.asList(0, 1, 2, 3);
        assertEquals(expectedDocIds, docIds);
        List<Integer> docIds1 = invertedLists.get("composableanalyz");
        List<Integer> expectedDocIds1 = Arrays.asList(1);
        assertEquals(expectedDocIds1, docIds1);
    }
}
