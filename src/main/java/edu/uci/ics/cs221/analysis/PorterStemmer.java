package edu.uci.ics.cs221.analysis;

/**
 * Project 1, task 3: Incorporate a porter stemmer.
 *
 * Stemming is the process of reducing a word into its "stem" ("root") form.
 *
 * Porter stemming is a classic and popular algorithm that uses a set of rules and steps to process a token.
 * Implementing porter stemmer is not technically interesting - it just consists a bunch of if-else statements,
 * therefore we ask you to simply incorporate an existing porter stemmer implementation into this project.
 *
 * https://github.com/apache/lucene-solr/blob/master/lucene/analysis/common/src/java/org/apache/lucene/analysis/en/PorterStemmer.java
 *
 */
public class PorterStemmer implements Stemmer {

    public PorterStemmer() {}

    public String stem(String token) {
        throw new UnsupportedOperationException("Porter Stemmer Unimplemented");
    }

}
