import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

import testingAudio.ButterworthBPF;
import testingAudio.Constantes;
import testingAudio.ReadWriteRaw;
import testingAudio.Transform;
import Spectrogram.Spectrogram;


public class AudioProcessing {

	private String fileName;
	private String rawFileName = fileName + ".raw";
	private String wavFileName = fileName + ".wav";
	private ArrayList<Double> raw_data;

	private String filtered3Name = fileName + "-3filterC.raw";
	private ArrayList<Double> filter3_data;
	private ArrayList<Double> filter_data;
	
	//private String transformedName = fileName + "-4fft.raw";
	//private String transformedValName = fileName + "-4fft-values";
	//private String susbstractedName = fileName + "-5subsnoise.raw";
	private Complex[][] fft_data ;	// fft 1
	private double[][] spec;		// fft 2
	private Picture picture1;		// 1
	private Picture picture2;		// 2
	private BlobProcessing blob_pr ;
	private Matrix[] bounded_pics;
	private Matrix[] bounded_vals;
	
	private int sampleRate = 44100; // samples per second
	// width is always CHUNK_SIZE (constantes Class)

	static boolean DEBUG = false;

	
	public AudioProcessing(String audioFileName){
		fileName = audioFileName;
		rawFileName = fileName + ".raw";
		wavFileName = fileName + ".wav";
		filtered3Name = fileName + "-3filterC.raw";
	}
	
	
	public void process() {
		
		readRaw();
		if (raw_data == null){
            System.out.println(fileName+".wav - no se ha leido nada!!!");
            return;
        }
		
		filter_data = raw_data;//filter3();//
		
		
		// FFT+colores: version 1 -  a mano
		//fastFurierTransform_1();
		//picture1 = new Picture(fft_data.length, fft_data[0].length, fft_data);
		// WRITE VALUE
		//ReadWriteRaw.writeIntToPlain(picture1.colors, fileName+"colores1");

		
		/// FFT+colores: version 2 - musicg
		fastFurierTransform_2();
		picture2 = new Picture(spec.length, spec[0].length, spec);	
		if (DEBUG) System.out.println("pic: h:"+picture2.height +"  w:"+  picture2.width);
		// WRITE VALUES
		ReadWriteRaw.writeIntToPlain(picture2.colors, fileName+"colores2");

		
		blob_pr = new BlobProcessing(picture2.height, picture2.width, picture2.colors);
		blob_pr.compute();
		
		bounded_pics = blob_pr.getBoundedPics();
		bounded_vals = blob_pr.getBoundedVals(spec);
    }
	
	private void readRaw(){
		// read shorts
        raw_data = ReadWriteRaw.readDoublesfromWav(wavFileName);
        if (raw_data == null){
            System.out.println("wav - no ha leido nada!!!");
            return;
        }
        System.out.println(wavFileName+" - lectura hecha .."+raw_data.size()+" samples." );
        // save audio file to verify
        ReadWriteRaw.writeDoublesToRaw(raw_data, "out_"+rawFileName);
		
	}
	
	private void filter3() {
		// ButterworthBPF - Filter - butterworth filter
    
    	System.out.println("Butterworth BP F");
        
    	if (DEBUG)System.out.println("-filter 3-");
        
        // filter (it works with doubles)
        ButterworthBPF bwbp = new ButterworthBPF();
        bwbp.createFilter(4, 44100, 500, 3500);
        filter3_data = bwbp.filter(raw_data);
        
        if (DEBUG)System.out.println("-filter 3-");
        
        
        filter_data = filter3_data;

        ReadWriteRaw.writeDoublesToRaw(filter3_data, filtered3Name);
		
	}
	
	private void fastFurierTransform_1() {
		System.out.println("FFT (1)");
		// TRANSFORM
        fft_data = Transform.fft(filter_data); // frequency domain
        //System.out.println("fft. h:"+fft_data.length+"   w:"+fft_data[0].length);
        
        
        // WRITE VALUES
        //try {
		//	ReadWriteRaw.writeComplexToPlain(fft_data, transformedValName);
		//} catch (IOException e) {
		//	// 
		//	e.printStackTrace();
		//}
        
        // WRITE RAW after fft_inv
        //ArrayList<Double> r_transformed = Transform.fft_inv(fft_data); // time domain
        //ReadWriteRaw.writeDoublesToRaw(r_transformed, transformedName);
		
	}
	private void fastFurierTransform_2() {
		System.out.println("FFT (2)");
		// TRANSFORM
        
        double[] data = new double[filter_data.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = filter_data.get(i);
        }
        if (DEBUG) System.out.println("data:"+data.length );
        int fftSampleSize = Constantes.CHUNK_SIZE;
        int overlapFactor = 1;//100;
        
        Spectrogram sp = new Spectrogram(data,sampleRate, fftSampleSize, overlapFactor);
        
        spec = sp.getNormalizedSpectrogramData();
        //ReadWriteRaw.writeDoubleToPlain(spec,fileName+"_spec-val"); // OK

        if (DEBUG) System.out.println("spec: h:"+spec.length +"  w:"+  spec[0].length);
        
        
        // WRITE VALUES
        //try {
		//	ReadWriteRaw.writeComplexToPlain(fft_data, transformedValName);
		//} catch (IOException e) {
		//	// 
		//	e.printStackTrace();
		//}
        
        // WRITE RAW after fft_inv
        //ArrayList<Double> r_transformed = Transform.fft_inv(fft_data); // time domain
        //ReadWriteRaw.writeDoublesToRaw(r_transformed, transformedName);
		
	}
	
	public Matrix[] getBoundedPics(){
		return bounded_pics;
	}
	public Matrix[] getBoundedVals(){
		return bounded_vals;
	}

}
