package edu.uci.ics.cs221.index.inverted;

import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.NaiveAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import edu.uci.ics.cs221.storage.Document;
import edu.uci.ics.cs221.storage.DocumentStore;
import edu.uci.ics.cs221.storage.MapdbDocStore;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static junit.framework.TestCase.assertTrue;

public class Team4IndexCompressionTest {
    String base = "./index";
    Document doc1 = new Document("cat dog");
    Document doc2 = new Document("cat dog bird cat cat dog dog");
    Document doc3 = new Document("cats running in the rain, birds chirping to keep away from cat.");

    @Before
    public void init(){

    }
    @Test
    public void tes1(){
        NaiveCompressor naiveCompressor = new NaiveCompressor();
        DeltaVarLenCompressor deltaVarLenCompressor = new DeltaVarLenCompressor();

        InvertedIndexManager naiveIndexManager = InvertedIndexManager.createOrOpenPositional("./index", new ComposableAnalyzer(new PunctuationTokenizer(),new PorterStemmer()),naiveCompressor);
        InvertedIndexManager dvlIndexManager = InvertedIndexManager.createOrOpenPositional("./index", new ComposableAnalyzer(new PunctuationTokenizer(),new PorterStemmer()),deltaVarLenCompressor);

        PageFileChannel.resetCounters();
        naiveIndexManager.addDocument(doc1);
        naiveIndexManager.addDocument(doc2);
        naiveIndexManager.addDocument(doc3);
        naiveIndexManager.flush();
        int naiveCount = PageFileChannel.writeCounter;

        PageFileChannel.resetCounters();
        dvlIndexManager.addDocument(doc1);
        dvlIndexManager.addDocument(doc2);
        dvlIndexManager.addDocument(doc3);
        dvlIndexManager.flush();
        int dvlCount = PageFileChannel.writeCounter;

        assertTrue(naiveCount <= dvlCount);
    }
}
