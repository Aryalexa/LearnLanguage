package testingAudio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

/**
 * Agunos métodos necesitan org.apache.commons.io.jar
 * Se puede prescindir de esos métodos, hay otros que los pueden suplir.
 * 
 */


public class ReadWriteRaw {

	/**
	 * RAW >> byte[]
	 * @param inputName
	 * @return array of read bytes
	 */
	public byte[] fromRaw2ByteArray(String inputName){
		byte[] data = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(inputName);
			data = IOUtils.toByteArray(fis);
			if (fis != null) {
		          fis.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * RAW >> ArrayList<Byte>
	 * @param inputName
	 * @return ArrayList<Byte>
	 * @throws IOException
	 */
	public static ArrayList<Byte> readBytesfromRaw(String inputName){
		ArrayList<Byte> bbuffer = new ArrayList<Byte>();
		byte b;
		
		try {
			FileInputStream is = new FileInputStream(inputName);
			DataInputStream dis = new DataInputStream(is);
			
			while(dis.available()>0)
			{
			   b = dis.readByte(); 
			   bbuffer.add(b);
			}
			dis.close();
			is.close();
		} catch (IOException e) {
			//nothing happens, there are 1,2 or 3 bytes we are going to ignore
			//e.printStackTrace();
		}
		
		return bbuffer;
	}
	
	/*
	public static float[] convertByteArrayToFloatArray(byte[] b) {
        FloatBuffer fb = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] f = new float[fb.limit()];
        fb.get(f);
        return f;
	}
	
	public static void fromRaw2Plain(String inputName, String outputName){
		System.out.println ("1 starts");
		byte[] data;
		//inputName = "jinglebells.raw";
		//outputName = "newjinglebells";
		try {
			FileInputStream fis = new FileInputStream(inputName);
			PrintWriter w = new PrintWriter(outputName);
			
			data = IOUtils.toByteArray(fis);
			float[] fdata = convertByteArrayToFloatArray(data);
			for (int i=0; i<fdata.length; i++){
				if(fdata[i]<Float.MAX_VALUE) {
					w.print(fdata[i]+"f\n");
					//System.out.println (fdata[i]+" ");
				}
			}
			System.out.println ("floats:"+fdata.toString());
			
			
		    if (fis != null) {
		          fis.close();
		      }
		    w.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] convertFloatToByteArray(float f){
		byte[] b = new byte[4];
		ByteBuffer.wrap(b, 0, 4).order(ByteOrder.LITTLE_ENDIAN).putFloat(f);
		return b;
	}
	
	public static void fromPlain2Raw_v2(String inputName, String outputName){
		
		System.out.println("2 starts");
        
        // create file input stream
		FileInputStream is;
		try {
			is = new FileInputStream(inputName);
			
			// create new data input stream
			DataInputStream dis = new DataInputStream(is);
	        
	        // read till end of the stream
	        while(dis.available()>0)
	        {
	           // read character
	           float f = dis.readFloat();
	           byte[] b = convertFloatToByteArray(f);
	           //System.out.println("bytes:"+b.toString());
	           // print
	           //System.out.print(f + " ");
	        }
	        System.out.print( "end 2 ");
			dis.close();
			is.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
        
        
	}
	
	public static void fromPlain2Raw(String inputName, String outputName){
		System.out.println("2 starts");
		
		Scanner scan;
	    File inputFile = new File(inputName);
	    
	    FileOutputStream fos = null;
	    
	    try {
	        scan = new Scanner(inputFile);
	        fos = new FileOutputStream(outputName);
	        System.out.println ("news ok, hasfloat: "+scan.hasNextFloat()+scan.next());
	        while(scan.hasNextFloat())//hasNextDouble())
	        {	
	        	System.out.println ("has float---");
	        	byte[] b = convertFloatToByteArray(scan.nextFloat());
	        	fos.write(b);
	        	System.out.println("bytes:"+b.toString());
	        }
	        scan.close();
	        fos.close();
	       
	    } catch (FileNotFoundException e1) {
	        e1.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * RAW >> ArrayList<Float>
	 * @param inputName
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Float> readFloatsfromRaw(String inputName){
		ArrayList<Float> fbuffer = new ArrayList<Float>();
		float f;
		
		try {
			FileInputStream is = new FileInputStream(inputName);
			DataInputStream dis = new DataInputStream(is);
			
			while(dis.available()>0)
			{
			   f = dis.readFloat(); //read 4 bytes as float 
			   // fallara muy probablemente si no es 4x
			   //System.out.print(f + " ");
			   fbuffer.add(f);
			}
			
			dis.close();
			is.close();
		} catch (IOException e) {
			//nothing happens, there are 1,2 or 3 bytes we are going to ignore
			//e.printStackTrace();
		}
		
		return fbuffer;
	}
	

	/**
	 * RAW >> ArrayList<Double>
	 * @param inputName
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Double> readDoublesfromRaw(String inputName) {
		ArrayList<Double> fbuffer = new ArrayList<Double>();
		double d;
		
		try {
			FileInputStream is = new FileInputStream(inputName);
			DataInputStream dis = new DataInputStream(is);
			
			while(dis.available()>0)
			{
			   d = dis.readDouble(); //read 8 bytes as double 
			   // fallara muy probablemente si no es 4x
			   //System.out.print(f + " ");
			   fbuffer.add(d);
			}
			dis.close();
			is.close();
		} catch (IOException e) {
			//nothing happens, there are 1,2.. 7 bytes we are going to ignore
			//e.printStackTrace();
		}
		
		return fbuffer;
	}
	
	
	/**
	 * ArrayList<Float> >> PLAIN
	 * @param fbuffer
	 * @param outputName
	 * @throws FileNotFoundException
	 */
	public static void writeFloatsToPlain(ArrayList<Float> fbuffer, String outputName) throws FileNotFoundException{
		PrintWriter w = new PrintWriter(outputName);
		
		for (int i=0; i<fbuffer.size(); i++){
			if(fbuffer.get(i)<Float.MAX_VALUE) {
				w.print(fbuffer.get(i)+"\n");
				//System.out.println (i+" ");
			}
			else {
				w.print(Float.MAX_VALUE+"\n");
			}
		}
		w.close();
	}
	
	/**
	 * PLAIN >> ArrayList<Float>
	 * @param inputName
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	public static ArrayList<Float> readFloatsfromPlain(String inputName) throws FileNotFoundException{
		ArrayList<Float> fb = new ArrayList<Float>();
		
		Scanner scan;
	    File inputFile = new File(inputName);
	    scan = new Scanner(inputFile).useDelimiter("\n");
	    
	    // read floats (1)
	    /*while(scan.hasNextFloat())
        {	
        	System.out.println ("has float---");
        	float f = scan.nextFloat();
 
        }*/
        
        // read strings >> floats (2)
	    while(scan.hasNext()){
	    	String fs = scan.next();
	    	float f = Float.parseFloat(fs); // funciona?? parece que si
	    	fb.add(f);
	    }
        
        scan.close();
		return fb;
	}
	
	
	/**
	 * ArrayList<Float> >> RAW
	 * @param fbuffer
	 * @param outputName
	 * @throws IOException
	 */
	public static void writeFloatsToRaw(ArrayList<Float> fbuffer, String outputName) throws IOException{
		FileOutputStream os = new FileOutputStream(outputName); 
		DataOutputStream dos = new DataOutputStream(os);
		
		for (int i=0; i<fbuffer.size(); i++){
			dos.writeFloat(fbuffer.get(i));
			//4-byte quantity, high byte first.
		}
		dos.close();
		os.close();
	}
	
	
	public static void main(String[] args) {
		String inputRaw = "jinglebells.raw";
		String outputPlain = "newjinglebells";
		String inputPlain = "newjinglebells2";
		String outputRaw = "jinglebells2.raw";
		
		// 1 - from raw
		ArrayList<Float> floats_;
		try {
			floats_ = readFloatsfromRaw(inputRaw);
			writeFloatsToPlain(floats_, outputPlain);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 2 - filter floats !!
		
		// 3 - to raw
		ArrayList<Float> ar2;
		try {
			ar2 = readFloatsfromPlain(inputPlain);
			writeFloatsToRaw(ar2, outputRaw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void test1(){
		/*String inputRaw = "jinglebells.raw";
		String outputPlain = "newjinglebells";
		fromRaw2Plain(inputRaw, outputPlain);
		
		String inputPlain = "newjinglebells";
		String outputRaw = "jinglebells2.raw";
		fromPlain2Raw_v2(inputPlain, outputRaw);
		*/
	}

}
