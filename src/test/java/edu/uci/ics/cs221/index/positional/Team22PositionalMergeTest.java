package edu.uci.ics.cs221.index.positional;

import com.google.common.collect.HashBasedTable;
import edu.uci.ics.cs221.analysis.*;
import edu.uci.ics.cs221.index.inverted.Compressor;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.index.inverted.PositionalIndexSegmentForTest;
import edu.uci.ics.cs221.index.inverted.NaiveCompressor;
import javafx.geometry.Pos;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import edu.uci.ics.cs221.storage.Document;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import javax.print.Doc;

import static org.junit.Assert.*;

public class Team22PositionalMergeTest {
    String folder = "./index/Team22PositionalMergeTest";

    InvertedIndexManager im;
    PositionalIndexSegmentForTest imt;
    Analyzer analyzer;
    Compressor compressor;



    @Before
    public void setup() throws Exception {

    }

    @After
    public void cleanup() throws Exception {



        try{
            File index = new File(folder);
            String[] f = index.list();
            for(String s: f){
                File currentFile = new File(index.getPath(),s);
                currentFile.delete();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Files.deleteIfExists(Paths.get(folder));

    }

    @Test
    public void test1(){
        Tokenizer tokenizer = new PunctuationTokenizer();
        Stemmer stemmer = new PorterStemmer();
        analyzer = new ComposableAnalyzer(tokenizer, stemmer); // create composable analyzer for documents tokenization
        compressor = new NaiveCompressor();
        im = InvertedIndexManager.createOrOpenPositional(folder,analyzer, compressor );


        Document doc1 = new Document("Information retrieval");
        Document doc2 = new Document("There is no easy way");

        im.addDocument(doc1);
        im.addDocument(doc2);
        im.flush();

        Document doc3 = new Document("vector space and Boolean queries");
        Document doc4 = new Document("A general theory of information retrieval");
        im.flush();

        im.mergeAllSegments();

        List<Document> seg1Docs = new ArrayList<>();
        seg1Docs.add(doc1);
        seg1Docs.add(doc2);
        seg1Docs.add(doc3);
        seg1Docs.add(doc4);

        assertEquals(1, im.getNumSegments()); // there is only one segment after merge

        imt = im.getIndexSegmentPositional(0);
        
        checkSegment(0, seg1Docs);
        checkPositional(0, seg1Docs);

    }


    /*
    create positional table
     */
    private Table<String, Integer, List<Integer>> createPositional(List<Document> docs){
        Table<String, Integer, List<Integer>> table = HashBasedTable.create();
        int docID = 0;
        for(Document d: docs){
            List<String> tokens = analyzer.analyze(d.getText());
            for (int i = 0; i < tokens.size(); i++){
                if (table.get(tokens.get(i), docID) == null){
                    table.put(tokens.get(i), docID, new ArrayList<>());
                }
                table.get(tokens.get(i), docID).add(i+1);
            }
            docID++;
        }
        return table;

    }

    private void checkPositional(int segmentNum, List<Document> docs){
        // get corresponding segment
        imt = im.getIndexSegmentPositional(segmentNum);
        Table<String, Integer, List<Integer>> ptable = imt.getPositions(), groundTruth = createPositional(docs);

        for (Cell<String, Integer, List<Integer>> cell: groundTruth.cellSet()){
            System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
            List<Integer> l = ptable.get(cell.getRowKey(), cell.getColumnKey());

            assertTrue(l != null); // check list contain in ptable
            Set<Integer> pSet = new HashSet<Integer>(l);

            for(int pos: cell.getValue()){
                assertTrue(pSet.contains(pos)); // check all ground truth position index in target list
            }

        }

    }

    /*
    This function create the ground truth only about inverted index of "one segments" of given documents lists
     */

    private HashMap<String, HashSet<Document>> createInvertedIndex2Doc(List<Document> docs){

        // to use the output,
        // just iterate through the posting list, then get the corresponding
        HashMap<String, HashSet<Document>> output = new HashMap<>();
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
        imt = im.getIndexSegmentPositional(segmentNum);

        // get inverted list and documents of the segment
        Map<String, List<Integer>> segIndex = imt.getInvertedLists();
        Map<Integer, Document> segDoc = imt.getDocuments();

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
}
