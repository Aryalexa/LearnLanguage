package com.aryalexa.sectionsapp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;




/**
 * Created by aryalexa on 9/8/16.
 */
public class Transform {



    int CHUNK_SIZE = 20;


    public Complex[][] fft(ByteArrayOutputStream out) {
        byte audio[] = out.toByteArray();

        final int totalSize = audio.length;

        int amountPossible = totalSize / CHUNK_SIZE;

        // When turning into frequency domain we'll need complex numbers:
        Complex[][] results = new Complex[amountPossible][];

        // For all the chunks:
        for (int times = 0; times < amountPossible; times++) {
            Complex[] complex = new Complex[CHUNK_SIZE];
            for (int i = 0; i < CHUNK_SIZE; i++) {
                // Put the time domain data into a complex number with imaginary part as 0:
                complex[i] = new Complex(audio[(times * CHUNK_SIZE) + i], 0);
            }
            // Perform FFT analysis on the chunk:
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
            results[times] = fft.transform(complex, TransformType.FORWARD);
        }
        return results;

    }

    public Complex[][] fft(byte audio[]) {

        final int totalSize = audio.length;

        int amountPossible = totalSize / CHUNK_SIZE;

        // When turning into frequency domain we'll need complex numbers:
        Complex[][] results = new Complex[amountPossible][];

        // For all the chunks:
        for (int times = 0; times < amountPossible; times++) {
            Complex[] complex = new Complex[CHUNK_SIZE];
            for (int i = 0; i < CHUNK_SIZE; i++) {
                // Put the time domain data into a complex number with imaginary part as 0:
                complex[i] = new Complex(audio[(times * CHUNK_SIZE) + i], 0);
            }
            // Perform FFT analysis on the chunk:
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
            results[times] = fft.transform(complex, TransformType.FORWARD);
        }
        return results;

    }


}
