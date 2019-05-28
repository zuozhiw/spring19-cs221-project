package edu.uci.ics.cs221.index.ranking;

import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.index.inverted.DeltaVarLenCompressor;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class Team10searchTfIdfTest {
    private String path = "./index/Team10searchTfIdfTest";
    private static InvertedIndexManager iim;
    private static List<String> docs;
    @Test
    public void test1(){
        iim = InvertedIndexManager.createOrOpenPositional(path,
                new ComposableAnalyzer(new PunctuationTokenizer(), new PorterStemmer()),
                new DeltaVarLenCompressor());
        Document doc1 = new Document("test search project");
        Document doc2 = new Document("search engine is our project");
        Document doc3 = new Document("we need to finish the project");
        iim.addDocument(doc1);
        iim.addDocument(doc2);
        iim.addDocument(doc2);
        iim.addDocument(doc3);
        iim.flush();
        List<String> phrase = new ArrayList<>(Arrays.asList("search","engine","project"));
        Iterator<Document> res = iim.searchTfIdf(phrase,2);
        int count = 0;
        while(res.hasNext()){
            count += 1;
            assertEquals(res,doc2);
        }
        assertEquals(count,2);
    }

    @After
    public void clean(){
        File file = new File(path);
        String[] filelist = file.list();
        for(String f : filelist){
            File temp = new File(path, f);
            temp.delete();
        }
        file.delete();
    }
}
