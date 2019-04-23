package edu.uci.ics.cs221.index.Team3StressTest;

import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class Team3StressTest {

    Analyzer analyzer1;
    List<String> allDocuments;
    InvertedIndexManager invertedIndexManager1;

    @BeforeClass
    public void beforeClass(){
        analyzer1 = new ComposableAnalyzer(new PunctuationTokenizer(),new PorterStemmer());
        invertedIndexManager1 = InvertedIndexManager.createOrOpen("./index/Team3StressTest/", analyzer1);
        try {
            URL dictResource = InvertedIndexManager.class.getClassLoader().getResource("text.txt"); //
            allDocuments = Files.readAllLines(Paths.get(dictResource.toURI()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //test addDocument()
        for (int i = 0; i < allDocuments.size(); i++) {
            invertedIndexManager1.addDocument(new Document(allDocuments.get(i)));
        }
    }

    @Test
    public void test1(){


        Iterator<Document> result1 = invertedIndexManager1.searchQuery("CD");

        Document docCD1 = new Document(allDocuments.get(0));
        Document docCD2 = new Document(allDocuments.get(12));
        Document docCD3 = new Document(allDocuments.get(22));
        Document docCD4 = new Document(allDocuments.get(34));


        List<Document> expected = Arrays.asList(docCD1,docCD2,docCD3,docCD4);
        Comparator<Document> docCompare = new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                return o1.hashCode()-o2.hashCode();
            }
        };

        Collections.sort(expected,docCompare);
        int i = 0;
        boolean allRight = true;
        while(result1.hasNext()){
            if(!expected.get(i).equals(result1.next())) allRight = false;
            i++;
        }
        assertTrue(allRight);
    }

    @Test
    public void test2(){

        Iterator<Document> result1 = invertedIndexManager1.searchQuery("DVD");

        Document docCD1 = new Document(allDocuments.get(4));
        Document docCD2 = new Document(allDocuments.get(5));
        Document docCD3 = new Document(allDocuments.get(6));
        Document docCD4 = new Document(allDocuments.get(7));
        Document docCD5 = new Document(allDocuments.get(26));
        Document docCD6 = new Document(allDocuments.get(27));
        Document docCD7 = new Document(allDocuments.get(28));
        Document docCD8 = new Document(allDocuments.get(29));


        List<Document> expected = Arrays.asList(docCD1,docCD2,docCD3,docCD4,docCD5,docCD6, docCD7,docCD8);
        Comparator<Document> docCompare = new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                return o1.hashCode()-o2.hashCode();
            }
        };

        Collections.sort(expected,docCompare);
        int i = 0;
        boolean allRight = true;
        while(result1.hasNext()){
            if(!expected.get(i).equals(result1.next())) allRight = false;
            i++;
        }
        assertTrue(allRight);
    }

    @Test
    public void test3(){
        Iterator<Document> result1 = invertedIndexManager1.searchAndQuery(Arrays.asList("DVD","CD"));
        assertTrue(!result1.hasNext());
    }

    @Test
    public void test4(){

        Iterator<Document> result1 = invertedIndexManager1.searchOrQuery(Arrays.asList("DVD","CD"));

        Document docCD1 = new Document(allDocuments.get(4));
        Document docCD2 = new Document(allDocuments.get(5));
        Document docCD3 = new Document(allDocuments.get(6));
        Document docCD4 = new Document(allDocuments.get(7));
        Document docCD5 = new Document(allDocuments.get(26));
        Document docCD6 = new Document(allDocuments.get(27));
        Document docCD7 = new Document(allDocuments.get(28));
        Document docCD8 = new Document(allDocuments.get(29));
        Document docCD9 = new Document(allDocuments.get(0));
        Document docCD10 = new Document(allDocuments.get(12));
        Document docCD11 = new Document(allDocuments.get(22));
        Document docCD12 = new Document(allDocuments.get(34));


        List<Document> expected = Arrays.asList(docCD1,docCD2,docCD3,docCD4,docCD5,docCD6, docCD7,docCD8,docCD9,docCD10,docCD11,docCD12);
        Comparator<Document> docCompare = new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                return o1.hashCode()-o2.hashCode();
            }
        };

        Collections.sort(expected,docCompare);
        int i = 0;
        boolean allRight = true;
        while(result1.hasNext()){
            if(!expected.get(i).equals(result1.next())) allRight = false;
            i++;
        }
        assertTrue(allRight);
    }
    @AfterClass
    public void afterClass(){
        //delete files
        String path = "./index/Team3StressTest/";
        File file = new File(path);
        if(!file.isDirectory()){
            System.out.println("File name is not a directory");
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
        }

    }
}
