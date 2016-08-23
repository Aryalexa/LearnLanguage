package testingAudio;

/**
 * basado en Creating Shazam in Java
 * by Roy van Rijn 
 *
 */

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

//////compile 'org.apache.commons:commons-math3:3.6.1'
//////including as lib >> commons-math3-3.6.1.jar

public class Transform {

	static int CHUNK_SIZE = 4096;

    public static Complex[][] fft(ByteArrayOutputStream out) {
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

    public static Complex[][] fft(byte audio[]) {

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
    
    public static Complex[][] fft(ArrayList<Byte> audio) {

        final int totalSize = audio.size();

        int amountPossible = totalSize / CHUNK_SIZE;
        System.out.println("original total:"+totalSize+
        		", chunkSIZE:"+CHUNK_SIZE+
        		", amountPossible:"+amountPossible+
        		", real total:"+CHUNK_SIZE*amountPossible);

        // When turning into frequency domain we'll need complex numbers:
        Complex[][] results = new Complex[amountPossible][];

        // For all the chunks:
        for (int times = 0; times < amountPossible; times++) {
            Complex[] complex = new Complex[CHUNK_SIZE];
            for (int i = 0; i < CHUNK_SIZE; i++) {
                // Put the time domain data into a complex number with imaginary part as 0:
                complex[i] = new Complex(audio.get( (times * CHUNK_SIZE) + i ), 0);
            }
            // Perform FFT analysis on the chunk:
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
            results[times] = fft.transform(complex, TransformType.FORWARD);
        }
        return results;

    }
    
    

    
    public static void main(String[] args){
    	ArrayList<Byte> ba = ReadWriteRaw.readBytesfromRaw("jinglebells.raw");
    	Complex[][] fft_ = Transform.fft(ba);
    	
    	/*
    	for (int i=0; i<fft_.length/8; i++){ // number of chunks
    		System.out.println(i);
			for (int j=0; j<fft_[i].length/32; j++) // CHUNK_SIZE
				System.out.println(j+" "+fft_[i][j]);
			System.out.println();
		}
    	*/
    }

}
