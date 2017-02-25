import org.apache.commons.math3.complex.Complex;

import blobDetection.Blob;
import blobDetection.BlobDetection;
import blobDetection.EdgeVertex;


public class blob_test {

	public static final int ALPHA_MASK = 0xff000000;
	public static final int RED_MASK   = 0x00ff0000;
	public static final int GREEN_MASK = 0x0000ff00;
	public static final int BLUE_MASK  = 0x000000ff;
	
	
	
	
	// The width of the image in units of pixels.
	// The height of the image in units of pixels.
	/** Actual dimensions of pixels array, taking into account the 2x setting. */
	public int picWidth;
	public int picHeight;
	public int[] pic;
	
	float levels = 16;                    // number of contours/levels/detectors
	float factor = 1;                     // scale factor

	// Array of BlobDetection Instances
	BlobDetection[] theBlobDetection = new BlobDetection[(int)levels];

	
	void setup(int height, int width, int[] ints){
		picWidth = width;
		picHeight = height;
		pic = new int[picWidth*picHeight];
		for (int i=0; i<(picWidth*picHeight);i++){
			pic[i] = ints[i];
		}
	}
	
	void compute() {
	  int a;
	  for(int x=0;x<picWidth;x++)
		  for(int y=0; y<picHeight; y++){
			  a = x+picWidth*y;
			  //System.out.println("accediendo..("+a+")"+pic[a]);
		  }
		
		
	  //Computing blobdetections with different thresholds 
	  for (int i=0 ; i<levels ; i++) { 
	    theBlobDetection[i] = new BlobDetection(picWidth, picHeight);
	    theBlobDetection[i].setThreshold(i/levels); // 0, 1/16, 2/16, 3/16, ..., 15/16
	    theBlobDetection[i].computeBlobs(pic);
	  }
	  
	  // show blobdetection's number of blobs 
	  for (int l=0 ; l<levels; l++){
	    System.out.println("--blob detection "+l+": "+theBlobDetection[l].getBlobNb());
	    showMinMaxValues(l);
	  }
	  
	  int l_ = 5; // choose blob detection
	  System.out.println("We choose a level (out of "+levels+"): LVL " + l_);
	  showMinMaxValues(l_);
		  
		  
	}

	void showMinMaxValues(int bt){
		Blob b;
		//EdgeVertex eA,eB;
		for (int n=0 ; n<theBlobDetection[bt].getBlobNb() ; n++) {
		    b=theBlobDetection[bt].getBlob(n);
		    if (b!=null) { 
		    	// diagonal
		    	//b.xMin*img.width*factor,+b.yMin*img.height*factor
		    	//b.xMax*img.width*factor,+b.yMax*img.height*factor
		    	
		    	// x -> 1
		    	// i -> picLengt

		    	// | lft
		    	int lftup_a = (int) Math.floor(b.xMin*picWidth*factor);
		    	int lftup_b = (int) Math.floor(b.yMin*picHeight*factor);
		    	int lftdn_a = (int) Math.floor(b.xMin*picWidth*factor);
		    	int lftdn_b = (int) Math.floor(b.yMax*picHeight*factor);
		     
		    	// | rgt
		    	int rgtup_a = (int) Math.floor(b.xMax*picWidth*factor);
		    	int rgtup_b = (int) Math.floor(b.yMin*picHeight*factor);
		    	int rgtdn_a = (int) Math.floor(b.xMax*picWidth*factor);
		    	int rgtdn_b = (int) Math.floor(b.yMax*picHeight*factor);
		     
		    	System.out.println("  blob "+n);
		    	System.out.println(
		    			"    ("+lftup_a+","+lftup_b+")"+"       "+"("+rgtup_a+","+rgtup_b+")"
	    		);
		    	System.out.println(
		    			"    ("+lftdn_a+","+lftdn_b+")"+"       "+"("+rgtdn_a+","+rgtdn_b+")"
		    	);
		        
		      
		    }// endif
		  }
	}
	
	
	//////////////////// EXAMPLE //////////////////////////
	static int picEx[];
	public static void main(String[] args) {
		
		int pic3[] = {
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0, 80, 90, 90, 90, 80,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0, 90,100,100,100, 90,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
				0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
			};
		
		int picEx[] = {
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 100, 100, 100, 100, 100, 250, 250, 250,
				250, 100, 100, 100, 100, 100, 250, 250, 250,
				250,  25,  25,  25,  25, 25, 250, 250, 250,
				250,  25,  10,  10,  10, 25, 250, 250, 250,
				250,  25,   0,   0,   0, 25, 250, 250, 250,
				250,  25,   0,   0,   0, 25, 250, 250, 250,
				250,  25,   0,   0,   0, 25, 250, 250, 250,
				250,  25,  25,  25,  25, 25, 250, 250, 250,
				250, 100, 100, 100, 100, 100, 250, 250, 250,
				250, 100, 100, 100, 100, 100, 250, 250, 250,
				250, 100, 100, 100, 100, 100, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250
			};
		int picWidth  = 9;
		int picHeight = 22;
		
		printMatriz(picHeight,picWidth,picEx);
		
		Picture picture = new Picture();
		picture.setInteger(picHeight, picWidth, picEx);
		
		printMatriz(picHeight,picWidth,picture.colors);
		
		blob_test b_t = new blob_test();
		b_t.setup(picture.height, picture.width, picture.colors);
		b_t.compute();
		
	}
	
	
	static void printMatriz(int height, int width, int[] content){
		System.out.println("int pic[] = { ");
		System.out.print("    ");
		for (int j=0; j<(width);j++){
			System.out.print(j+"    ");
		}
		System.out.println(" ");
		for (int i=0; i<(height);i++){
			System.out.print(i+"   ");
			for (int j=0; j<(width);j++){
				System.out.print(content[i*width+j]+", ");
			}
			System.out.println(" ");
		}
		System.out.println("}");
	
	}
	
	
	//////////////////////////////////////////////////////
	public static String toHex(int n){
		String s = Integer.toHexString(n);
		//int i = Integer.valueOf(String.valueOf(n), 16);
		//toHex(11); // b
		//toHex(29); // 1d
		//toHex(32); // 20
		System.out.println(n+" hex:"+" "+s);
		return "";
	}
	
	public static void toDec(int hex){
		int i = Integer.valueOf(String.valueOf(hex), 16);
		//toDec(0xb); // b
		//toDec(32); // 20
		System.out.println(hex+" dec:"+" "+i);
	}
	
	
}
