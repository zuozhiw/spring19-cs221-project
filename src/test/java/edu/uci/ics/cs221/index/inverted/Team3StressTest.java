package edu.uci.ics.cs221.index.inverted;

import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.storage.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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
    static final int NUM = 10000000;

    @BeforeClass
    public void beforeClass(){
        String URL = "http://cyy0908.com/text.txt";
        try {
            java.net.URL url = new URL(URL);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                allDocuments.add(s);
            }
            reader.close();
        }
        catch (Exception e){
            new RuntimeException(e);
        }

        analyzer1 = new ComposableAnalyzer(new PunctuationTokenizer(),new PorterStemmer());
        invertedIndexManager1 = InvertedIndexManager.createOrOpen("./index/Team3StressTest/", analyzer1);

        for(int i=0;i<NUM;i++){
            invertedIndexManager1.addDocument(new Document(allDocuments.get(i%allDocuments.size())));
        }
    }

    @Test
    public void test1(){
        Iterator<Document> result1 = invertedIndexManager1.searchQuery("CD");
        int count = 0;
        while(result1.hasNext()){
            count++;
        }
        assertTrue(count>50000?true:false);
    }

    @Test
    public void test2(){
        Iterator<Document> result1 = invertedIndexManager1.searchQuery("DVD");
        int count = 0;
        while(result1.hasNext()){
            count++;
        }
        assertTrue(count>100000?true:false);
    }

    @Test
    public void test3(){
        Iterator<Document> result1 = invertedIndexManager1.searchAndQuery(Arrays.asList("DVD","CD"));
        assertTrue(!result1.hasNext());
    }

    @Test
    public void test4(){
        Iterator<Document> result1 = invertedIndexManager1.searchOrQuery(Arrays.asList("DVD","CD"));
        int count = 0;
        while(result1.hasNext()){
            count++;
        }
        assertTrue(count>150000?true:false);
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