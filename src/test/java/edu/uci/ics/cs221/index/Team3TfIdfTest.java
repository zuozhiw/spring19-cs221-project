package edu.uci.ics.cs221.index;

import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import edu.uci.ics.cs221.index.inverted.DeltaVarLenCompressor;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Team3TfIdfTest {
    private  static InvertedIndexManager iim;
    private static String path = "./index/Team3TfIdfTest/";
    private  static Document[] documents;
    @BeforeClass
    public static void init(){
        iim = InvertedIndexManager.createOrOpenPositional(path,
                new ComposableAnalyzer(new PunctuationTokenizer(), new PorterStemmer()),
                new DeltaVarLenCompressor());

        documents = new Document[] {
                new Document("Winter is coming."),
                new Document("You know nothing Jon Snow."),
                new Document("A lion doesn’t concern himself with the opinions of a sheep."),
                new Document("When you play the game of thrones, you win or you die."),
                new Document("A Lannister always pays his debts."),
                new Document("All men must die, but we are not men."),
                new Document("There is only one god and his name is Death, and there is only one thing we say to Death: Not today.")
        };
    }

    @Test
    public void test(){
        for(int i=0;i<documents.length;i++){
            for(int j = 0; j<20;j++){
                iim.addDocument(documents[i]);
            }
            iim.flush();
        }

        assertEquals(iim.getDocumentFrequency(0,"winter"),20);
        assertEquals(iim.getDocumentFrequency(1,"snow"),20);
        assertEquals(iim.getDocumentFrequency(2,"lion"),20);
        assertEquals(iim.getDocumentFrequency(3,"plai"),20);
        assertEquals(iim.getDocumentFrequency(4,"pai"),20);
        assertEquals(iim.getDocumentFrequency(5,"men"),20);
        assertEquals(iim.getDocumentFrequency(6,"death"),20);

    }
    @After
    public void clean() {

        try {
            File folder = new File(path);
            String[] entries = folder.list();
            for(String s: entries) {
                File currentFile = new File(folder.getPath(),s);
                currentFile.delete();
            }

            if (folder.delete()) {
                System.out.println("Folder deleted successfully");
            } else {
                System.out.println("Failed to delete the folder");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong when deleting file");
        }
    }
}
