package edu.uci.ics.cs221.index.inverted;

import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static junit.framework.TestCase.assertTrue;

public class Team3StressTest {

    Analyzer analyzer1;
    List<String> allDocuments;
    InvertedIndexManager invertedIndexManager1;
    static final int TOTALNUM = 100000;

    //Construct to do some initialization like downloading the text resources
    //and objects construction.
    public Team3StressTest(){
        String URL = "http://cyy0908.com/text.txt";
        allDocuments = new ArrayList<>();
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

        //Copy the resource text 100000 times so that the total resources will be very large
        //so that we could meet the demand of a Stress Test.(Very large dataset)
        for(int i=0;i<TOTALNUM;i++){
            invertedIndexManager1.addDocument(new Document(allDocuments.get(i%allDocuments.size())));
        }

    }
    //Test searchQuery with keyword "CD" and see if it finds more than 500 results.
    //If true it means the engine didn't crash under a Stress Condition.
    @Test
    public void test1(){
        PageFileChannel.resetCounters();
        Iterator<Document> result1 = invertedIndexManager1.searchQuery("CD");
        int count = 0;
        while(result1.hasNext()){
            result1.next();
            count++;
        }
        assertTrue(PageFileChannel.writeCounter>=500);
        assertTrue(PageFileChannel.readCounter>=500);
        assertTrue(count>500?true:false);
    }

    //Test searchQuery with keyword "DVD" and see if it finds more than 1000 results.
    //If true it means the engine didn't crash under a Stress Condition.
    @Test
    public void test2(){
        PageFileChannel.resetCounters();
        Iterator<Document> result1 = invertedIndexManager1.searchQuery("DVD");
        int count = 0;
        while(result1.hasNext()){
            result1.next();
            count++;
        }
        assertTrue(PageFileChannel.writeCounter>=500);
        assertTrue(PageFileChannel.readCounter>=500);
        assertTrue(count>1000?true:false);
    }
    //Test searchAndQuery with keyword "CD" and "DVD" and see if it finds no answer.
    @Test
    public void test3(){
        Iterator<Document> result1 = invertedIndexManager1.searchAndQuery(Arrays.asList("DVD","CD"));
        assertTrue(!result1.hasNext());
    }
    //Test searchOrQuery with keyword "DVD" or "CD" and see if it finds more than 1500 results.
    //If true it means the engine didn't crash under a Stress Condition.
    @Test
    public void test4(){
        Iterator<Document> result1 = invertedIndexManager1.searchOrQuery(Arrays.asList("DVD","CD"));
        int count = 0;
        while(result1.hasNext()){
            result1.next();
            count++;
        }
        assertTrue(count>1500?true:false);
    }
    //Delete the files that are created during the process of searching.
    @After
    public void after(){
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