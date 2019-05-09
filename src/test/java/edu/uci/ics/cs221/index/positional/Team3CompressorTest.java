package edu.uci.ics.cs221.index.positional;

import edu.uci.ics.cs221.index.inverted.DeltaVarLenCompressor;
import edu.uci.ics.cs221.index.inverted.NaiveCompressor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Team3CompressorTest {
    private DeltaVarLenCompressor deltaVarLenCompressor;
    private NaiveCompressor naiveCompressor;
    @Before
    public void initialize() {
        deltaVarLenCompressor = new DeltaVarLenCompressor();
        naiveCompressor = new NaiveCompressor();
    }

    //Test encoding
    @Test
    public void test1(){
        List<Integer> integers1 = Arrays.asList(1,2,3,4,5,6); //after delta encoding 1,1,1,1,1,1
        byte[] expected1 = {(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01};
        assertEquals(expected1,deltaVarLenCompressor.encode(integers1));

        List<Integer> integers2 = Arrays.asList(128,384,896,1920,3968);//after delta encoding 128,256,512,1024,2048
        byte[] expected2 = {(byte)0x81,(byte)0x00,(byte)0x82,(byte)0x00,(byte)0x84,(byte)0x00,(byte)0x88,(byte)0x00,
                (byte)0x90,(byte)0x00};
        assertEquals(expected2,deltaVarLenCompressor.encode(integers2));

        List<Integer> integers3 = Arrays.asList(Integer.MAX_VALUE);
        byte[] expected3 = {(byte)0x87,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0x7f};
        assertEquals(expected3,deltaVarLenCompressor.encode(integers3));
    }
    //Test decoding
    @Test
    public void test2(){
        List<Integer> expected1 = Arrays.asList(1,2,3,4,5,6); //after delta encoding 1,1,1,1,1,1
        byte[] bytes1 = {(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01};
        assertEquals(expected1,deltaVarLenCompressor.decode(bytes1));

        List<Integer> expected2 = Arrays.asList(128,384,896,1920,3968);//after delta encoding 128,256,512,1024,2048
        byte[] bytes2 = {(byte)0x81,(byte)0x00,(byte)0x82,(byte)0x00,(byte)0x84,(byte)0x00,(byte)0x88,(byte)0x00,
                (byte)0x90,(byte)0x00};
        assertEquals(expected2,deltaVarLenCompressor.decode(bytes2));

        List<Integer> expected3 = Arrays.asList(Integer.MAX_VALUE);
        byte[] bytes3 = {(byte)0x87,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0x7f};
        assertEquals(expected3,deltaVarLenCompressor.decode(bytes3));
    }
}
