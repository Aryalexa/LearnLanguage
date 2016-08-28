package com.aryalexa.sectionsapp;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by aryalexa on 28/8/16.
 */
public class Aux {

    /**
     * ArrayList<Double> >> short[]
     * @param dlist
     * @return array of shorts
     * @throws FileNotFoundException
     */
    public static short[] doubleArrayL2shortArray(ArrayList<Double> dlist) throws FileNotFoundException {

        // ArrayList<Double> >> byte[]
        byte[] data = new byte[8*dlist.size()];
        byte[] bytes = new byte[8];
        for (int i=0; i<dlist.size();i++){
            ByteBuffer.wrap(bytes).putDouble(dlist.get(i));
            for (int j=0; j<8; j++)
                data[i*j+j] = bytes[j];
        }
        // byte[] >> short[]
        ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] s_arr = new short[sb.limit()];
        sb.get(s_arr);

        return s_arr;
    }
}
