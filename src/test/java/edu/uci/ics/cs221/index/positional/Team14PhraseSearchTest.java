package edu.uci.ics.cs221.index.positional;

import edu.uci.ics.cs221.analysis.Analyzer;
import edu.uci.ics.cs221.analysis.ComposableAnalyzer;
import edu.uci.ics.cs221.analysis.PorterStemmer;
import edu.uci.ics.cs221.analysis.PunctuationTokenizer;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import edu.uci.ics.cs221.storage.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;
import java.io.File;
import java.util.*;

import static org.junit.Assert.*;


public class Team14PhraseSearchTest {




    InvertedIndexManager index;
    Analyzer analyzer = new ComposableAnalyzer(new PunctuationTokenizer(), new PorterStemmer());
    String path = "./index/Team14MergeTest/";



    Document[] documents1 = new Document[] {
            new Document("This morning I ate eggs"),
            new Document("Abstraction is often one floor above you."),
            new Document("Everyone was busy, so I went to the movie alone."),
            new Document("Please wait outside of the house."),
            new Document("Wednesday is hump day, but has anyone asked the camel if he’s happy about it?"),
            new Document("He told us a very exciting adventure story."),
            new Document("My Mom tries to be cool by saying that she likes all the same things that I do."),
            new Document("She advised him to come back at once."),
            new Document("She works two jobs to make ends meet; at least, that was her reason for not having time to join us."),
            new Document("How was the math test?"),
            new Document("Eggs come from chickens."),
            new Document("Abstraction is used in this project."),
            new Document("Everyone was busy with math homework, and so I went out alone"),
            new Document("The job we did alone at the house of my boss and we weren't happy about it."),
            new Document("Camels are the horses of the middle east. "),
            new Document("Once upon a time the egg came from the camel and became a project."),
            new Document("At the end we had a chance to meet at the movie, but weren't thinking the same thing."),
            new Document("Math is like an egg the hard it is the better it is."),
            new Document("Jobs seem like a waste of time if you aren't happy"),
            new Document("My mom has a job that is like an adventure every day."),
            new Document("The weather outside was too cool for the camel."),
            new Document("Wednesday is the day that our chicken produces a lot of eggs."),
            new Document("Two jobs to make ends meet, means we need to less eggs."),
            new Document("As a camel do you have one or two humps?") };


    @Before public void build() {
        index = InvertedIndexManager.createOrOpen(path, analyzer);
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 1;
    }

    @After public void tear() {
        File index = new File(path);
        String[] entries = index.list();
        for (String s : entries) {
            File currentFile = new File(index.getPath(), s);
            currentFile.delete();
        }
        index.delete();
        InvertedIndexManager.DEFAULT_FLUSH_THRESHOLD = 1000;
        InvertedIndexManager.DEFAULT_MERGE_THRESHOLD = 8;
    }


    @Test public void test3()
    {

        for(Document doc : documents1)
        {
            index.addDocument(doc);

        }
        index.flush();

        List<String> keyWords = Arrays.asList("never", "mind");
        Iterator<Document> it = index.searchPhraseQuery(keyWords);

        int counter = 0;
        while(it.hasNext())
        {
            counter ++;
            it.next();
        }

        assertEquals(0,counter);




    }
}
