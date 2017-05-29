import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

import testingAudio.*;
import blobDetection.*;


/**
 * MAIN PRINCIPAL
 * @author aryalexa
 *
 */


public class Main {

	static String fileName = "itadakimasuA";//prueba001;itadakimasuA
	static String rawFileName = fileName + ".raw";
	static String wavFileName = fileName + ".wav";
	static ArrayList<Double> raw_data;

	static String filtered3Name = fileName + "-3filterC.raw";
	static ArrayList<Double> filter3_data;
	static ArrayList<Double> filter_data;
	
	static String transformedName = fileName + "-4fft.raw";
	static String transformedValName = fileName + "-4fft-values";
	static String susbstractedName = fileName + "-5subsnoise.raw";
	static Complex[][] fft_data ;
	
	static Picture picture;
	
	
	
	public static void main(String[] args) {
		
		// this should call a funciton from testingAudio package 
		// which do everything with its classes
		// TODO
		readRaw();
		filter3();
		fastFurierTransform();

		//width is always NUM_CHUNKS = 4096
		picture = new Picture(fft_data.length, fft_data[0].length, fft_data);
		
		// WRITE VALUES
		ReadWriteRaw.writeIntToPlain(picture.colors, "colores");
		
		
		//blob_test b_t = new blob_test();
		//b_t.setup(picture.height, picture.width, picture.colors);
		//b_t.compute();
        
    }
	
	static void readRaw(){
		// read shorts
        raw_data = ReadWriteRaw.readDoublesfromWav(wavFileName);
        if (raw_data == null){
            System.out.println("wav - no ha leido nada!!!");
            return;
        }
        System.out.println("wav - lectura hecha .."+raw_data.size()+" samples." );
        // save audio file to verify
        ReadWriteRaw.writeDoublesToRaw(raw_data, "out_"+rawFileName);
		
	}
	
	static void filter3() {
		// ButterworthBPF - Filter - butterworth filter
    
    	System.out.println("Butterworth BP F");
        
        System.out.println("-filter 3-");
        
        // filter (it works with doubles)
        ButterworthBPF bwbp = new ButterworthBPF();
        bwbp.createFilter(4, 44100, 500, 3500);
        filter3_data = bwbp.filter(raw_data);
        
        System.out.println("-filter 3-");
        
        
        filter_data = filter3_data;

        ReadWriteRaw.writeDoublesToRaw(filter3_data, filtered3Name);
		
	}
	
	static void fastFurierTransform() {
		System.out.println("FFT");
		// TRANSFORM
        fft_data = Transform.fft(filter_data); // frequency domain
        
        // WRITE VALUES
        try {
			ReadWriteRaw.writeComplexToPlain(fft_data, transformedValName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // WRITE RAW after fft_inv
        ArrayList<Double> r_transformed = Transform.fft_inv(fft_data); // time domain
        ReadWriteRaw.writeDoublesToRaw(r_transformed, transformedName);
		
	}

}
