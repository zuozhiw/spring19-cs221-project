package edu.uci.ics.cs221;

import edu.uci.ics.cs221.analysis.Analyzer;
import edu.uci.ics.cs221.analysis.NaiveAnalyzer;
import edu.uci.ics.cs221.search.FullScanSearcher;
import edu.uci.ics.cs221.storage.Document;
import edu.uci.ics.cs221.storage.DocumentStore;

import java.util.List;

import static edu.uci.ics.cs221.storage.MapdbDocStore.createOrOpen;

public class HelloWord {

    public static void main(String[] args) throws Exception {
        DocumentStore documentStore = createOrOpen("./docs.db");

        documentStore.addDocument(0, new Document("UCI CS221 Information Retrieval"));
        documentStore.addDocument(1, new Document("Donald Bren School of Information and Computer Sciences"));
        documentStore.addDocument(2, new Document("UCI School of ICS "));

        documentStore.close();

        documentStore = createOrOpen("./docs.db");

        String query = "information";
        Analyzer analyzer = new NaiveAnalyzer();
        FullScanSearcher fullScanSearcher = new FullScanSearcher(documentStore, analyzer);
        List<Integer> searchResult = fullScanSearcher.search("information");

        System.out.println("query: " + query);
        System.out.println("search results: ");
        for (int docID : searchResult) {
            System.out.println(docID + ": " + documentStore.getDocument(docID));
        }

        fullScanSearcher.close();

    }

}
