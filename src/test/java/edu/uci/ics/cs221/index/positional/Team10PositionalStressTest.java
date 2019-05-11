package edu.uci.ics.cs221.index.positional;

import com.google.common.collect.Table;
import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.index.inverted.NaiveCompressor;
import edu.uci.ics.cs221.storage.Document;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.PositionalIndexSegmentForTest;
import org.junit.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Team10PositionalStressTest {

    private String path = "./index/Team10PositionalStressTest";
    private String bookurl = "http://www.gutenberg.org/cache/epub/4276/pg4276.txt";
    private InvertedIndexManager invertedmanager;
    private List<String> docs;

    @Test(timeout = 1500000)
    public void init(){
        invertedmanager = InvertedIndexManager.createOrOpenPositional(path,new ComposableAnalyzer(new PunctuationTokenizer(),new PorterStemmer()),new NaiveCompressor());
        invertedmanager.DEFAULT_FLUSH_THRESHOLD = 1000;
        invertedmanager.DEFAULT_MERGE_THRESHOLD = 10;
        docs = new ArrayList<>();
        try {
            java.net.URL url = new URL(bookurl);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                docs.add(line);
            }
            bufferedReader.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        for(int i = 0; i < docs.size(); i++){
            invertedmanager.addDocument(new Document(docs.get(i)));
        }

        try{
            test1();
        }catch (Throwable e){
            System.out.println("Sorry, u failed at test1");
            e.printStackTrace();
        }

        try{
            test2();
        }catch (Throwable e){
            System.out.println("Sorry, u failed at test2");
            e.printStackTrace();
        }

        try{
            test3();
        }catch (Throwable e){
            System.out.println("Sorry, u failed at test3");
            e.printStackTrace();
        }
    }

    public void test1(){
        Iterator<Document> result = invertedmanager.searchQuery("beebo");
        assertEquals(false, result.hasNext());
    }

    public void test2(){
        PositionalIndexSegmentForTest result = invertedmanager.getIndexSegmentPositional(0);
        Map<Integer, Document> documents = result.getDocuments();
        assertEquals("The Project Gutenberg EBook of North and South, by Elizabeth Cleghorn Gaskell",documents.get(0).getText());
    }

    public void test3(){
        PositionalIndexSegmentForTest result = invertedmanager.getIndexSegmentPositional(0);
        Table<String, Integer, List<Integer>> position = result.getPositions();
        assertEquals(new ArrayList<>(Arrays.asList(3)),position.get("ebook",0));

    }

    @After
    public void clean(){
        File file = new File(path);
        String[] FileList = file.list();
        for(String f : FileList){
            File temp = new File(path, f);
            temp.delete();
        }
        file.delete();
    }

}
