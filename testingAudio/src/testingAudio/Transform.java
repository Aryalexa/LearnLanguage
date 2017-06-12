package testingAudio;

/**
 * basado en Creating Shazam in Java
 * by Roy van Rijn 
 *
 */

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

//////compile 'org.apache.commons:commons-math3:3.6.1'
//////including as lib >> commons-math3-3.6.1.jar

public class Transform {

	static int CHUNK_SIZE = Constantes.CHUNK_SIZE; // muestras de 16bits=2bytes
	static boolean DEBUG = false;

	
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
    
    /**
     * spectrogram by chunks
     * @param audio
     * @return
     */
    public static Complex[][] fft_2(ArrayList<Double> audio) {
    	FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        
        final int totalSize = audio.size();
        int amountPossible = totalSize / Constantes.CHUNK_SIZE;
        if (DEBUG) {
        	System.out.println("original total:"+totalSize+
        		", chunkSIZE:"+CHUNK_SIZE+
        		", amountPossible:"+amountPossible+
        		", real total:"+CHUNK_SIZE*amountPossible);
        }
        
        
        
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
            results[times] = fft.transform(complex, TransformType.FORWARD);
        }
        return results;
    }
    
    public static Complex[][] fft(ArrayList<Double> audio) {
    	FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        
        final int totalSize = audio.size();
        int amountPossible = totalSize / Constantes.CHUNK_SIZE;
        if (DEBUG){
        	System.out.println("original total:"+totalSize+
        		", chunkSIZE:"+CHUNK_SIZE+
        		", amountPossible:"+amountPossible+
        		", real total:"+CHUNK_SIZE*amountPossible);
        }
        
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
            results[times] = fft.transform(complex, TransformType.FORWARD);
        }
        return results;
    }
    
    public static ArrayList<Double> fft_inv(Complex[][] freq_domain){
    	FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
    	
    	int size = freq_domain.length; // number of chunks
    	Complex[][] time_domain = new Complex[size][];
    	ArrayList<Double> results = new ArrayList<Double>();
    	
    	// For all chunks
    	for (int i = 0; i < size; i++) {
    		// Perform FFT inverse analysis on the chunk
    		time_domain[i] = fft.transform(freq_domain[i], TransformType.INVERSE);
    		for (int j = 0; j < CHUNK_SIZE; j++) {
    			// save as real numbers
    			results.add(time_domain[i][j].abs());
    		}
    	}
    	
        return results;
    }
    
    public static Complex[][] substractNoise(Complex[][] fft){
    	Complex[][] clean_fft = new Complex[fft.length][];
    	
    	// get threshold
    	
    	ArrayList<Double> average_mags = new ArrayList<Double>();
    	for (int i=0; i<fft.length; i++){ // number of chunks
    		average_mags.add(calculateAverageMag(fft[i]));
    	}
    	
    	double threshold = Collections.min(average_mags);
    	
    	// magnitude spectral subtraction
    	
    	double mag, clean_mag; 
    	Complex arg;
    	for (int i=0; i<fft.length; i++){ // number of chunks
    		clean_fft[i] = new Complex[CHUNK_SIZE];
			for (int j=0; j<fft[i].length; j++){ // CHUNK_SIZE
				mag = fft[i][j].abs();
				arg = new Complex(fft[i][j].getArgument());
				clean_mag = mag - threshold;
				if (clean_mag < 0) clean_mag = 0;
				clean_fft[i][j] = arg.exp().multiply(clean_mag);
			}
		}
    	
    	return clean_fft;
    }
    
    private static double calculateAverageMag(Complex[] list) {
    	  double sum = 0;
    	  int size = list.length;
    	  if(size != 0) {
    	    for (int i=0; i<size; i++) {
    	        sum += list[i].abs();
    	    }
    	    return sum / size;
    	  }
    	  return sum;
    	}
    
    
    public static void main(String[] args){
    	// get data from .raw
    	ArrayList<Double> ba = ReadWriteRaw.readDoublesfromRaw("jinglebells.raw");
    	
    	// FFT
    	Complex[][] fft_ = Transform.fft(ba);
    	
    	// substract noise
    	Complex[][] filt_fft = Transform.substractNoise(fft_);
    	
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
