package edu.uci.ics.cs221.index.inverted;

import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.WordBreakTokenizer;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Team21FlushTest {


    @Test
    public void test1() {
        /*
        test documentIterator(), flush() and addDocument() functions.
         */
        ComposableAnalyzer analyzer = new ComposableAnalyzer(new WordBreakTokenizer(), new PorterStemmer());
        String indexFolder = "./index/Team21FlushTest/";
        InvertedIndexManager manager = InvertedIndexManager.createOrOpen(indexFolder, analyzer);
        String str1 = "dog cat penguin";
        String str2 = "bird cat lion";
        String str3 = "fish cat bear";
        manager.addDocument(new Document(str1));
        manager.addDocument(new Document(str2));
        manager.addDocument(new Document(str3));
        manager.flush();

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
        ComposableAnalyzer analyzer = new ComposableAnalyzer(new WordBreakTokenizer(), new PorterStemmer());
        String indexFolder = "./index/Team21FlushTest/";
        InvertedIndexManager manager = InvertedIndexManager.createOrOpen(indexFolder, analyzer);
        Document doc1 = new Document("dog cat penguin");
        Document doc2 = new Document("bird cat lion");
        Document doc3 = new Document("fish cat bear");
        manager.addDocument(doc1);
        manager.flush();
        manager.addDocument(doc2);
        manager.flush();
        manager.addDocument(doc3);
        manager.flush();

        int expected = 3;
        assertEquals(expected, manager.getNumSegments());
    }

    @Test
    public void test3() {
        /*
        test getIndexSegment(), flush() and addDocument() functions.
         */
        ComposableAnalyzer analyzer = new ComposableAnalyzer(new WordBreakTokenizer(), new PorterStemmer());
        String indexFolder = "./index/Team21FlushTest/";
        InvertedIndexManager manager = InvertedIndexManager.createOrOpen(indexFolder, analyzer);
        String str1 = "dog cat";
        String str2 = "bird cat";
        String str3 = "fish dog";
        manager.addDocument(new Document(str1));
        manager.addDocument(new Document(str2));
        manager.addDocument(new Document(str3));
        manager.flush();

        Set<String> set = new HashSet();
        set.add(str1);
        set.add(str2);
        set.add(str3);

        InvertedIndexSegmentForTest test = manager.getIndexSegment(0);

        Map<String, List<Integer>> invertedList = test.getInvertedLists();
        String[] words = {"dog", "cat", "bird", "fish"};
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

}
