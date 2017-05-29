package testingAudio;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

public class Main {

	// double - trabaj con doubles!! al leer de los archivos de audio
	static String fileName = "itadakimasuA";//prueba001;itadakimasuA
	static String rawFileName = fileName + ".raw";
	static String wavFileName = fileName + ".wav";

	static ArrayList<Double> raw_data;
	
	static IIR_Filter iir_fil;
	static FIR_Filter fir_fil;
	static String filtered1Name = fileName + "-1filterA.raw";
	static String filtered2Name = fileName + "-2filterB.raw";
	static String filtered3Name = fileName + "-3filterC.raw";
	static ArrayList<Double> filter1_data;
	static ArrayList<Double> filter2_data;
	static ArrayList<Double> filter3_data;
	static ArrayList<Double> filter_data;

	
	static String transformedName = fileName + "-4fft.raw";
	static String transformedValName = fileName + "-4fft-values";
	static String susbstractedName = fileName + "-5subsnoise.raw";
	static Complex[][] fft_data ;
	
	static boolean DEBUG=true;
	
	public static void main(String[] args) {
		
		readRaw();
		 
		filter_data = new ArrayList<Double>();
        
		//filter1(); //FIX this TODO
		//filter2();
		filter3();
        fastFurierTransform();
        noiseSubstraction();
		
    }
	
	static void readRaw(){
		// read 
        raw_data = ReadWriteRaw.readDoublesfromWav(wavFileName);
        if (raw_data == null){
            System.out.println("wav - no ha leido nada!!!");
            return;
        }
        System.out.println("wav - lectura hecha .."+raw_data.size()+" samples." );
        
        // save audio file to verify
        ReadWriteRaw.writeDoublesToRaw(raw_data, "out_"+rawFileName);
		
	}
	
	static void filter1() {
		// 1 - IIR_Filter - butterworth filter
    
    	System.out.println("IIR_Filter - butterworth filter");
        // ArrayList<Double> -> double[]
        double[] data = new double[raw_data.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = raw_data.get(i);
        }
        
        System.out.println("-filter 1-");
        
        // filter (it works with doubles)
        iir_fil = new IIR_Filter(44100, 500, 3500); // order 6
        double[] data1 = iir_fil.butter_bandpass_filter(data);
        
        System.out.println("-filter 1-");
        
        // double[] >> // ArrayList<Double>
        filter1_data = new ArrayList<Double>();
        for (int i = 0; i < data1.length; i++) {
        	filter1_data.add(data1[i]);
        }
		if(DEBUG){
			for(int i=0; i<100; i++){
				System.out.println(i+" f1 - "+raw_data.get(i)
						+"     "+filter1_data.get(i));
				}
		}

        filter_data = filter1_data;

        ReadWriteRaw.writeDoublesToRaw(filter1_data, filtered1Name);
            
	}

	static void filter2() {
		// 2 - FIR_Filter -
		System.out.println("FIR_Filter - ");
        int size = raw_data.size();
        
        int order = 5;
        fir_fil = new FIR_Filter(FIR_Filter.FilterType.BPF, order+1, 2, 0.25, 0.375);
        
        // filter works with doubles, sample by sample
        filter2_data = new ArrayList<>();
        for(int i=0; i<size;i++){
            filter2_data.add(fir_fil.do_sample(raw_data.get(i)));
        }

        filter_data = filter2_data;

        ReadWriteRaw.writeDoublesToRaw(filter2_data, filtered2Name);
		
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
	
	private static void fastFurierTransform() {
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

	private static void noiseSubstraction() {
		System.out.println("Substraction");
        Complex[][] filt_fft_data = Transform.substractNoise(fft_data); // frequency domain
        ArrayList<Double> r_substracted = Transform.fft_inv(filt_fft_data); // time domain
        ReadWriteRaw.writeDoublesToRaw(r_substracted, susbstractedName);
		
	}

}
