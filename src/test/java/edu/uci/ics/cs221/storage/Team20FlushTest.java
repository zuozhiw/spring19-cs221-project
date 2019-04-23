package edu.uci.ics.cs221.storage;

import edu.uci.ics.cs221.analysis.NaiveAnalyzer;
import edu.uci.ics.cs221.index.inverted.InvertedIndexManager;
import edu.uci.ics.cs221.index.inverted.InvertedIndexSegmentForTest;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;


import static org.junit.Assert.assertEquals;

public class Team20FlushTest {

    @Test
    public void test1() {
        /* change the flush threshold and see if flush() is automatically called*/
        Document d1 = new Document("cat dog");
        Document d2 = new Document("cat elephant");
        Map<String, List<Integer>> expectedPostingList = new HashMap<>();
        expectedPostingList.put("cat", Arrays.asList(0, 1));
        expectedPostingList.put("dog", Arrays.asList(0)) ;
        expectedPostingList.put("elephant", Arrays.asList(1)) ;
        Map<Integer, Document> expectedDocuments = new HashMap<>();
        expectedDocuments.put(0, d1);
        expectedDocuments.put(1, d2);
        NaiveAnalyzer analyzer = new NaiveAnalyzer();
        InvertedIndexManager ii = InvertedIndexManager.createOrOpen("index/Team20FlushTest/", analyzer) ;
        ii.DEFAULT_FLUSH_THRESHOLD = 2;
        ii.addDocument(d1);
        ii.addDocument(d2);
        //ii.flush();
        assertEquals(1, ii.getNumSegments());
        InvertedIndexSegmentForTest segment;
        segment = ii.getIndexSegment(0);
        assertEquals(expectedPostingList, segment.getInvertedLists());
        assertEquals(expectedDocuments, segment.getDocuments());
    }

    @Test
    public void test2() {
        /* forcefully call flush(), keeping an empty buffer, flush should do nothing*/

        Map<String, List<Integer>> expectedPostingList = new HashMap<>();
        Map<Integer, Document> expectedDocuments = new HashMap<>();

        NaiveAnalyzer analyzer = new NaiveAnalyzer();
        InvertedIndexManager ii = InvertedIndexManager.createOrOpen("index/Team20FlushTest/", analyzer) ;

        ii.flush();
        assertEquals(0, ii.getNumSegments());

    }

    @AfterClass
    public static void deleteFiles() {
               String SRC_FOLDER = "index/Team20FlushTest/";

                File directory = new File(SRC_FOLDER);

                //make sure directory exists
                if(!directory.exists()){

                    System.out.println("Directory does not exist.");
                    System.exit(0);

                }else{

                    try{

                        delete(directory);

                    }catch(IOException e){
                        e.printStackTrace();
                        System.exit(0);
                    }
                }

                System.out.println("Done");
            }

            public static void delete(File file)
                    throws IOException {

                if(file.isDirectory()){

                    //directory is empty, then delete it
                    if(file.list().length==0){

                        file.delete();
                        System.out.println("Directory is deleted : "
                                + file.getAbsolutePath());

                    }else{

                        //list all the directory contents
                        String files[] = file.list();

                        for (String temp : files) {
                            //construct the file structure
                            File fileDelete = new File(file, temp);

                            //recursive delete
                            delete(fileDelete);
                        }

                        //check the directory again, if empty then delete it
                        if(file.list().length==0){
                            file.delete();
                            System.out.println("Directory is deleted : "
                                    + file.getAbsolutePath());
                        }
                    }

                }else{
                    //if file, then delete it
                    file.delete();
                    System.out.println("File is deleted : " + file.getAbsolutePath());
                }
            }
        }



