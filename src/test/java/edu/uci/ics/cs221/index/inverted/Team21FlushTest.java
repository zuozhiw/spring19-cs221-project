package edu.uci.ics.cs221.index.inverted;

import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.WordBreakTokenizer;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Team21FlushTest {
    private InvertedIndexManager manager;
    String indexFolder = "./index/Team21FlushTest/";
    private ComposableAnalyzer analyzer;
    Document doc1 = new Document("dog cat penguin");
    Document doc2 = new Document("bird cat lion");
    Document doc3 = new Document("fish cat bear");

    @Before
    public void before(){
        analyzer = new ComposableAnalyzer(new WordBreakTokenizer(), new PorterStemmer());
        manager = InvertedIndexManager.createOrOpen(indexFolder, analyzer);
        manager.addDocument(doc1);
        manager.flush();
        manager.addDocument(doc2);
        manager.flush();
        manager.addDocument(doc3);
        manager.flush();
    }

    @After
    public void deleteWrittenFiles(){
        String path = "./index/Team21FlushTest/";
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            String[] fileList = file.list();
            File temp = null;
            for (int i = 0; i < fileList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + fileList[i]);
                } else {
                    temp = new File(path + File.separator + fileList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
            }
        }
    }

    @Test
    public void test1() {
        /*
        test documentIterator() functions.
         */
        String str1 = "dog cat penguin";
        String str2 = "bird cat lion";
        String str3 = "fish cat bear";

        Set<String> set = new HashSet();
        set.add(str1);
        set.add(str2);
        set.add(str3);

        Iterator<Document> managerIt = manager.documentIterator();
        while(managerIt.hasNext()){
            Document temp = managerIt.next();
            assertTrue(set.contains(temp.getText()));
            set.remove(temp);
        }
        if(!set.isEmpty()){
            assertTrue(false);
        }
    }

    @Test
    public void test2() {
        /*
        test getNumSegment(), flush() and addDocument() functions.
         */
        Document doc4 = new Document("dog cat");
        manager.addDocument(doc4);
        manager.flush();

        int expected = 4;
        assertEquals(expected, manager.getNumSegments());
    }

    @Test
    public void test3() {
        /*
        test getIndexSegment() functions.
         */
        String str1 = "dog cat penguin";
        String str2 = "bird cat lion";
        String str3 = "fish cat bear";
        String str4 = "dog cat";

        Set<String> set = new HashSet();
        set.add(str1);
        set.add(str2);
        set.add(str3);
        set.add(str4);

        InvertedIndexSegmentForTest test = manager.getIndexSegment(0);

        Map<String, List<Integer>> invertedList = test.getInvertedLists();
        String[] words = {"dog", "cat","penguin", "bird", "lion", "fish", "bear" };
        assertEquals(words.length, invertedList.size());
        for(String word : words){
            assertTrue(invertedList.containsKey(word));
        }

        Map<Integer, Document> documents = test.getDocuments();
        assertEquals(set.size(), documents.size());
        for(Document temp : documents.values()){
            assertTrue(set.contains(temp.getText()));
        }
    }


}
