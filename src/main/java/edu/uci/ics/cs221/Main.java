package edu.uci.ics.cs221;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {
    private static final String URL = "https://github.com/ZTShao/NonContactHeartRateDetect-PulseMirror/blob/master/import-summary.txt";

    public static void main(String[] args) throws IOException {
        java.net.URL url = new URL(URL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            System.out.println(s);
        }
        reader.close();
    }
}
