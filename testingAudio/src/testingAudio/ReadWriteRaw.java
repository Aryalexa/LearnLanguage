package testingAudio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	 * RAW >> float[]
	 * @param inputName
	 * @return array of floats
	 */
	public static float[] fromRawToFloatArray(String inputName){
		ArrayList<Float> fbuffer = new ArrayList<Float>();
		float f;
		
		try {
			FileInputStream is = new FileInputStream(inputName);
			DataInputStream dis = new DataInputStream(is);
			
			while(dis.available()>0)
			{
			   f = dis.readFloat(); //read 4 bytes as float 
			   // fallara muy probablemente si no es 4x
			   System.out.print(f + " ");
			   fbuffer.add(f);
			}
			
			dis.close();
			is.close();
		} catch (IOException e) {
			//nothing happens, there are 1,2 or 3 bytes we are going to ignore
			//e.printStackTrace();
		}
		
		float[] ff = new float[fbuffer.size()];
		for (int i=0; i<fbuffer.size(); i++){
			ff[i] = fbuffer.get(i);
		}
		
		return ff;
	}
	
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
			   System.out.print(f + " ");
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
	 * RAW -> ArrayList<Short>
	 * @param inputName
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Short> read2bytesFromRaw(String inputName){
		/* 16 bit PCM has a range -32768 to 32767. 
		 * So, multiply each of your PCM samples by (1.0f/32768.0f) 
		 * into a new array of floats, and pass that to your resample.*/
		
		ArrayList<Short> sbuffer = new ArrayList<Short>();
		short f;
		
		try {
			FileInputStream is = new FileInputStream(inputName);
			DataInputStream dis = new DataInputStream(is);
			
			while(dis.available()>0)
			{
			   f = dis.readShort(); //.readFloat(); //read 4 bytes as float 
			   // fallara muy probablemente si no es 4x
			   System.out.print(f + " ");
			   sbuffer.add(f);
			}
			
			dis.close();
			is.close();
		} catch (IOException e) {
			//nothing happens, there are 1,2 or 3 bytes we are going to ignore
			//e.printStackTrace();
		}
		
		return sbuffer;
	}
	
	
	public static void readShortsFromRAW(File file){
		
		byte[] byteInput = new byte[(int)file.length()];
	    short[] input = new short[(int)(byteInput.length / 2f)];

	    try{

	        FileInputStream fis = new FileInputStream(file);
	        fis.read(byteInput, 0, byteInput.length);
	        ByteBuffer.wrap(byteInput).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(input);

	    }catch(Exception e  ){
	        e.printStackTrace();
	    }
	    for(int i=0; i<input.length; i++){
	    	System.out.print(input[i] + " ");
	    }
	    
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
		if (fbuffer.isEmpty()) {
			System.out.println("lectura vacia");
			return null;
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
	 * ArrayList<Short> >> PLAIN
	 * @param fbuffer
	 * @param outputName
	 * @throws FileNotFoundException
	 */
	public static void writeShortsToPlain(ArrayList<Short> buffer, String outputName) throws FileNotFoundException{
		PrintWriter w = new PrintWriter(outputName);
		
		for (int i=0; i<buffer.size(); i++){
			if(buffer.get(i)<Short.MAX_VALUE) {
				w.print(buffer.get(i)+"\n");
				//System.out.println (i+" ");
			}
			else {
				w.print(Short.MAX_VALUE+"\n");
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
	
	/**
	 * float[] >> RAW
	 * @param fbuffer
	 * @param outputName
	 * @throws IOException
	 */
	public static void writeFloatArrayToRaw(float[] fbuffer, String outputName) throws IOException{
		FileOutputStream os = new FileOutputStream(outputName); 
		DataOutputStream dos = new DataOutputStream(os);
		
		for (int i=0; i<fbuffer.length; i++){
			dos.writeFloat(fbuffer[i]);
			//4-byte quantity, high byte first.
		}
		dos.close();
		os.close();
	}
	
	/**
	 * ArrayList<Double> >> RAW
	 * @param dbuffer
	 * @param outputName
	 * @throws IOException
	 */
	public static void writeDoublesToRaw(ArrayList<Double> dbuffer, String outputName) throws IOException{
		FileOutputStream os = new FileOutputStream(outputName); 
		DataOutputStream dos = new DataOutputStream(os);
		
		for (int i=0; i<dbuffer.size(); i++){
			dos.writeDouble(dbuffer.get(i));
			//4-byte quantity, high byte first.
		}
		dos.close();
		os.close();
	}
	
	
	public static void main(String[] args) {
		String name 		= "itadakimasu_A"; //"prueba001";
		String inputRaw 	= 		name+".raw";
		String outputPlain 	= "new"+name;
		String inputPlain 	= "new"+name+"2";
		String outputRaw 	= 		name+"2.raw";
		
		int step = 7;
		
		switch(step){
		case 1: // 1 - from raw
			ArrayList<Short> sss;
			try {
				sss = read2bytesFromRaw(inputRaw);
				writeShortsToPlain(sss, outputPlain);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2: // 2 - filter floats !!
			break;
		case 3: // 3 - to raw
			ArrayList<Float> ar2;
			try {
				ar2 = readFloatsfromPlain(inputPlain);
				writeFloatsToRaw(ar2, outputRaw);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		File file = new File(inputRaw);
		int len = (int)file.length();
		readShortsFromRAW(file);
		System.out.println("");
		System.out.println("len:"+len);
		System.out.println("num of shorts:"+(int)len/2f);
		
		
		System.out.println("hecho");
	}
	
	public static void test1(){
		//jinglebells
		String name = "jinglebells";
		String inputRaw = name+".raw";
		String outputPlain = "new"+name;
		//fromRaw2Plain(inputRaw, outputPlain);
		
		String inputPlain = "new"+name;
		String outputRaw = name+"2.raw";
		//fromPlain2Raw_v2(inputPlain, outputRaw);
		
	}

}
