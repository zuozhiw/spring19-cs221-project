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

    Analyzer analyzer;
    List<String> allDocuments;
    InvertedIndexManager invertedIndexManager;
    private static final int TOTALNUM = 100000;
    private static final String textUrl = "http://cyy0908.com/text.txt";
    private static final String indexFolder = "./index/Team3StressTest/";


    //Construct to do some initialization like downloading the text resources
    //and objects construction.
    public Team3StressTest(){
        allDocuments = getOnlineTextFile(textUrl);
        analyzer = new ComposableAnalyzer(new PunctuationTokenizer(),new PorterStemmer());
        invertedIndexManager = InvertedIndexManager.createOrOpen(indexFolder, analyzer);
        PageFileChannel.resetCounters();
        //Copy the resource text 100000 times so that the total resources will be very large
        //so that we could meet the demand of a Stress Test.(Very large dataset)
        for(int i=0;i<TOTALNUM;i++){
            invertedIndexManager.addDocument(new Document(allDocuments.get(i%allDocuments.size())));
        }
        assertTrue(PageFileChannel.writeCounter>=TOTALNUM/invertedIndexManager.DEFAULT_FLUSH_THRESHOLD);
    }


    //Test searchQuery with keyword "CD" and see if it can get the right answer.
    //If true it means the engine didn't crash under a Stress Condition.
    @Test
    public void test1(){
        PageFileChannel.resetCounters();
        Iterator<Document> result1 = invertedIndexManager.searchQuery("CD");
        int count = 0;
        while(result1.hasNext()){
            result1.next();
            count++;
        }
        assertTrue(PageFileChannel.readCounter>=50);
        assertTrue(count==9091);
    }


    //Test searchQuery with keyword "DVD" and see if it can get the right answer.
    //If true it means the engine didn't crash under a Stress Condition.
    @Test
    public void test2(){
        PageFileChannel.resetCounters();
        Iterator<Document> result1 = invertedIndexManager.searchQuery("DVD");
        int count = 0;
        while(result1.hasNext()){
            result1.next();
            count++;
        }
        assertTrue(PageFileChannel.readCounter>=100);
        assertTrue(count==18184);
    }


    //Test searchAndQuery with keyword "CD" and "DVD" and see if it finds no answer.
    @Test
    public void test3(){
        Iterator<Document> result1 = invertedIndexManager.searchAndQuery(Arrays.asList("DVD","CD"));
        assertTrue(!result1.hasNext());
    }


    //Test searchOrQuery with keyword "DVD" or "CD" and see if it can find the right answer.
    //If true it means the engine didn't crash under a Stress Condition.
    @Test
    public void test4(){
        PageFileChannel.resetCounters();
        Iterator<Document> result1 = invertedIndexManager.searchOrQuery(Arrays.asList("DVD","CD"));
        int count = 0;
        while(result1.hasNext()){
            result1.next();
            count++;
        }
        assertTrue(count==27275);
        assertTrue(PageFileChannel.readCounter>=150);
    }


    //Delete the files that are created during the process of searching.
    @After
    public void after(){
        deleteFile(indexFolder);
    }


    //Get our large resources text file from a URL
    private List<String> getOnlineTextFile(String URL){
        List<String> result = new ArrayList<>();
        try {
            java.net.URL url = new URL(URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                result.add(s);
            }
            reader.close();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }


    //Delete the index/Team directory
    private void deleteFile(String fileDir){
        File file = new File(fileDir);
        if(!file.isDirectory()){
            System.out.println("File name is not a directory");
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (fileDir.endsWith(File.separator)) {
                temp = new File(fileDir + tempList[i]);
            } else {
                temp = new File(fileDir + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
        }
    }
}