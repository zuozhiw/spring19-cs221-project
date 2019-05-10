package edu.uci.ics.cs221.index.positional;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import edu.uci.ics.cs221.analysis.NaiveAnalyzer;
import edu.uci.ics.cs221.index.inverted.Compressor;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.PositionalIndexSegmentForTest;
import edu.uci.ics.cs221.index.inverted.NaiveCompressor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import edu.uci.ics.cs221.analysis.Analyzer;
import edu.uci.ics.cs221.storage.Document;

public class Team19PositionalFlushTest {
    Analyzer an = new NaiveAnalyzer();
    Compressor cp = new NaiveCompressor();
    InvertedIndexManager iim;
    String file = "./index/Team19PositionalFlushTest/";

    @Before
    public void setup() throws Exception {
        iim = iim.createOrOpenPositional(file, an, cp);
        iim.DEFAULT_FLUSH_THRESHOLD = 3;
    }

    @After
    public void cleanup() throws Exception {
        try{
            File index = new File(file);
            String[] f = index.list();
            for(String s: f){
                File currentFile = new File(index.getPath(),s);
                currentFile.delete();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Files.deleteIfExists(Paths.get(file));

        iim.DEFAULT_FLUSH_THRESHOLD = 1000;
    }

    // test flush when flush() is called by user, whether the total number of segments is correct
    // and whether the first disk segment is set correctly.
    @Test
    public void testPositionalFlush1() throws Exception {
        iim.addDocument(new Document("cat dog"));
        iim.addDocument(new Document("cat elephant"));
        iim.addDocument(new Document("wolf dog dog"));
        iim.flush();
        iim.addDocument(new Document("cat dog"));
        iim.flush();
        assertEquals(2, iim.getNumSegments());

        Map<String, List<Integer>> PostingList = new HashMap<>();
        PostingList.put("cat", Arrays.asList(0, 1));
        PostingList.put("dog", Arrays.asList(0, 2));
        PostingList.put("elephant", Arrays.asList(1));
        PostingList.put("wolf", Arrays.asList(2));

        Map<Integer, Document> DocStore = new HashMap<>();
        DocStore.put(0, new Document("cat dog"));
        DocStore.put(1, new Document("cat elephant"));
        DocStore.put(2, new Document("wolf dog"));

        Table<String, Integer, List<Integer>> Positions = HashBasedTable.create();
        Positions.put("cat", 0, Arrays.asList(1));
        Positions.put("cat", 1, Arrays.asList(1));
        Positions.put("dog", 0, Arrays.asList(2));
        Positions.put("dog", 2, Arrays.asList(2, 3));
        Positions.put("elephant", 1, Arrays.asList(2));
        Positions.put("wolf", 2, Arrays.asList(1));

        PositionalIndexSegmentForTest test = iim.getIndexSegmentPositional(0);
        assertEquals(PostingList, test.getInvertedLists());
        assertEquals(DocStore, test.getDocuments());
        assertEquals(Positions, test.getPositions());
    }

    // test flush() has no operation when no document is added
    @Test
    public void testPositionalFlush2(){
        iim.flush();
        assertEquals(0, iim.getNumSegments());
        assertEquals(null, iim.getIndexSegmentPositional(0));
    }
}
