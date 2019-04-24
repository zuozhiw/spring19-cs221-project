package edu.uci.ics.cs221;
import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.index.inverted.PageFileChannel;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.jvm.hotspot.debugger.Page;

import javax.print.Doc;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.*;

import static org.junit.Assert.*;

public class Team22FlushTest {
    String folder = "index";
    String file = "InvertedIndexTest.db";
    InvertedIndexManager indexManager;
    InvertedIndexSegmentForTest indexManagerTest;
    Analyzer analyzer;

    @Before
    public void setup() throws Exception {
    }

    @After
    public void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get(folder));
    }

    // function that automatic generate HashSet of Docs corresponding to each key word
    private HashMap<String, HashSet<Document>> createInvertedIndex2Doc(List<Document> docs){

        // to use the output,
        // just iterate through the posting list, then get the corresponding
        HashMap<String, HashSet<Document>> output = new HashMap<String, HashSet<Document>>();
        for(Document d: docs){
            List<String> tokens = analyzer.analyze(d.getText()); // split the text to tokens
            for (String token: tokens){
                if (!output.containsKey(token)){ // if the output map doesn't contain this token, create new entry

                    output.put(token, new HashSet<Document>());
                }
                output.get(token).add(d); // add this document to corresponding tokens

            }

        }

        return output;
    }

    // function to check if segment is read correctly
    private void checkSegment(int segmentNum, List<Document> docs){
        // get corresponding segment
        indexManagerTest = indexManager.getIndexSegment(segmentNum);

        // get inverted list and documents of the segment
        Map<String, List<Integer>> segIndex = indexManagerTest.getInvertedLists();
        Map<Integer, Document> segDoc = indexManagerTest.getDocuments();

        // create ground true inverted list
        Map<String,HashSet<Document>> segGroundTrue = createInvertedIndex2Doc(docs);

        // check if the posting list are correct
        for (Map.Entry<String, List<Integer>> entry : segIndex.entrySet()) {
            String keyword = entry.getKey();
            List<Integer> postinglist = entry.getValue();

            for (int docID: postinglist){
                Document d = segDoc.get(docID);
                assertTrue(segGroundTrue.get(keyword).contains(d));
            }
        }

        // check if the documents are correct
        for (Document d: docs){
            assertTrue(segDoc.containsValue(d));
        }

    }

    @Test
    public void addDocumentTest1() throws Exception{
        Tokenizer tokenizer = new PunctuationTokenizer();
        Stemmer stemmer = new PorterStemmer();
        analyzer = new ComposableAnalyzer(tokenizer, stemmer); // create composable analyzer for documents tokenization

        indexManager = InvertedIndexManager.createOrOpen(folder,analyzer);

        Document doc1 = new Document("cat and dogs");
        Document doc2 = new Document("cat and banana, bright, chicken");

        ArrayList<Document> docs = new ArrayList<>();
        docs.add(doc1);
        docs.add(doc2);

        indexManager.addDocument(doc1);
        indexManager.addDocument(doc2);
        indexManager.flush(); // create segment 0 in disk

        assertEquals(indexManager.getNumSegments(), 1); // should create one segment

        checkSegment(0, docs);
        indexManager.flush();
        assertEquals(indexManager.getNumSegments(), 1);
    }

    /* This test checks two aspects of the flush function.
        1. Check if flush does nothing if no document added
        2. Create an empty document, and check if flush added the document properly, with empty posting list.
     */
    @Test
    public void addDocumentTest2(){
        Tokenizer tokenizer = new WordBreakTokenizer();
        Stemmer stemmer = new PorterStemmer();
        analyzer = new ComposableAnalyzer(tokenizer, stemmer);
        indexManager = InvertedIndexManager.createOrOpen(folder,analyzer);

        // check if flush does nothing when no docs added
        indexManager.flush();
        assertEquals(indexManager.getNumSegments(), 0);

        // check if empty string document is added properly, but with empty inverted index
        Document doc1 = new Document("");
        ArrayList<Document> docs = new ArrayList<>();
        docs.add(doc1);
        indexManager.addDocument(doc1);
        indexManager.flush();
        assertEquals(indexManager.getNumSegments(), 1);
        checkSegment(0, docs);
    }


    // This testcase tests if the system automatically flush when reaching the threshold
    @Test
    public void addDocumentTest3(){
        Tokenizer tokenizer = new PunctuationTokenizer();
        Stemmer stemmer = new PorterStemmer();
        analyzer = new ComposableAnalyzer(tokenizer, stemmer); // create composable analyzer for documents tokenization
        indexManager = InvertedIndexManager.createOrOpen(folder,analyzer);

        // reset the default_flush_threshold
        int old_threshold = indexManager.DEFAULT_FLUSH_THRESHOLD;
        indexManager.DEFAULT_FLUSH_THRESHOLD = 5;
        int new_threshold = indexManager.DEFAULT_FLUSH_THRESHOLD;
        int numDocs = 23;
        ArrayList<Document> docs = new ArrayList<>(); // ground true docs
        for (int i = 0; i < numDocs; i++){
            Document doc = new Document(String.valueOf(i));
            indexManager.addDocument(doc);
            docs.add(doc);
            // reset ground true docs when reach threshold
            if (i % new_threshold == new_threshold - 1){
                checkSegment(indexManager.getNumSegments()-1, docs);
                docs = new ArrayList<>();
            }
        }
        assertEquals(indexManager.getNumSegments(), numDocs/new_threshold);
        indexManager.DEFAULT_FLUSH_THRESHOLD = old_threshold; // change back to original threshold
    }

    // check if read/writer counter updated properly
    @Test
    public void addDocumentTest4(){
        Tokenizer tokenizer = new PunctuationTokenizer();
        Stemmer stemmer = new PorterStemmer();
        analyzer = new ComposableAnalyzer(tokenizer, stemmer); // create composable analyzer for documents tokenization
        indexManager = InvertedIndexManager.createOrOpen(folder,analyzer);

        // create documents with 4096 characters
        char[] chars = new char[PageFileChannel.PAGE_SIZE];
        Arrays.fill(chars, 'a');
        String text = new String(chars);
        Document doc1 = new Document(text);

        chars = new char[PageFileChannel.PAGE_SIZE];
        Arrays.fill(chars, 'b');
        text = new String(chars);
        Document doc2 = new Document(text);

        chars = new char[PageFileChannel.PAGE_SIZE];
        Arrays.fill(chars, 'c');
        text = new String(chars);
        Document doc3 = new Document(text);

        // set up ground true documents
        ArrayList<Document> docs = new ArrayList<>();
        docs.add(doc1);
        docs.add(doc2);
        docs.add(doc3);

        // add documents
        indexManager.addDocument(doc1);
        indexManager.addDocument(doc2);
        indexManager.addDocument(doc3);
        indexManager.flush();

        // check segment number and write counter
        assertEquals(indexManager.getNumSegments(), 1);
        assertTrue(PageFileChannel.writeCounter >= 3);

        // check segment context and read counter
        checkSegment(0, docs);
        assertTrue(PageFileChannel.readCounter >= 3);
    }
}
