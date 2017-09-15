//import org.apache.commons.math3.complex.Complex;

import blobDetection.Blob;
import blobDetection.BlobDetection;
import blobDetection.EdgeVertex;


public class BlobProcessing {

	public static final int ALPHA_MASK = 0xff000000;
	public static final int RED_MASK   = 0x00ff0000;
	public static final int GREEN_MASK = 0x0000ff00;
	public static final int BLUE_MASK  = 0x000000ff;
	
	static boolean DEBUG = false;
	
	/** Actual dimensions of pixels array, taking into account the 2x setting. */
	public int picWidth; 	// The width of the image in units of pixels.
	public int picHeight; 	// The height of the image in units of pixels.
	public int[] pic;
	
	float levels = 16;      // number of contours/levels/detectors
	float factor = 1;       // scale factor

	// Array of BlobDetection Instances
	BlobDetection[] theBlobDetection = new BlobDetection[(int)levels];

	public BlobProcessing (int height, int width, int[] colors){
		setup(height, width, colors);
	}
	
	
	void setup(int height, int width, int[] colors){
		picWidth = width;
		picHeight = height;
		pic = new int[picWidth*picHeight];
		for (int i=0; i<(picWidth*picHeight);i++){
			pic[i] = colors[i];
		}
	}
	
	
	void compute() {
	  //Computing blobdetections with different thresholds 
	  for (int i=0 ; i<levels ; i++) { 
	    theBlobDetection[i] = new BlobDetection(picWidth, picHeight);
	    theBlobDetection[i].setThreshold(i/levels); // 0, 1/L, 2/L, 3/L, ..., (L-1)/L
	    theBlobDetection[i].computeBlobs(pic);
	  }
	  
	  // show blobdetection's number of blobs 
	  for (int lev=0 ; lev<levels; lev++){
	    if (DEBUG) System.out.println("--blob detection "+lev+": "+theBlobDetection[lev].getBlobNb());
	    //showMinMaxValues(lev);
	  }
	  
	  //int l_ = 5; // choose blob detection
	  //System.out.println("We choose a level (out of "+levels+"): LVL " + l_);
	  //showMinMaxValues(l_);
		  
		  
	}

	/////////
	/**
	 * Corta la matriz de colores segun los blobs de cada nivel
	 * @return
	 */
	public Matrix[] getBoundedPics(){
		Matrix[] pics = new Matrix[(int) levels];
		int[] x = new int[]{0,0};
		int[] y = new int[]{0,0};
		for (int i=0 ; i<levels ; i++) { 
			processMinMax(i,x,y);
			if (DEBUG) System.out.println("boundedPic "+i+" ejeY: "+y[0]+" < "+y[1]+" < "+picHeight);
			pics[i] = new Matrix(pic,picHeight, picWidth);
			pics[i].cortarEjeY(y[0], y[1]);
		  }
		return pics;
	}
	/**
	 * Corta la matriz _vals (del tamaño indicado) segun los blobs de cada nivel
	 * @param _vals
	 * @return
	 */
	public Matrix[] getBoundedVals(double[][] _vals){
		// prepare 1D matrix
		int vH = _vals.length;
		int vW = _vals[0].length;
		double[] vals = new double[vH*vW];
		for (int h=0; h<vH; h++){
			for (int w=0; w<vW; w++){
				vals[h*vW + w] = _vals[h][w];
			}
		}
		// actual process
		Matrix[] bVals = new Matrix[(int) levels];
		int[] x = new int[]{0,0};
		int[] y = new int[]{0,0};
		for (int i=0 ; i<levels ; i++) { 
			processMinMax(i,x,y); // processBigBlobMinMax(i,x,y); OR processMinMax(i,x,y);
			if (DEBUG) System.out.println("boundedPic "+i+" ejeY: "+y[0]+" < "+y[1]+" < "+picHeight);
		    bVals[i] = new Matrix(vals,picHeight, picWidth);
		    bVals[i].cortarEjeY(y[0], y[1]);
		  }
		return bVals;
	}
	
	/**
	 * calculate xmin,xmax,ymin,ymax from all the blobs detected at especified level
	 * @param level
	 * @param x array where x[0]=xmin, x[1]=xmax
	 * @param y array where y[0]=ymin, y[1]=ymax
	 */
	void processMinMax(int level, int[] x, int[] y){
		x[0] = picWidth;   // xmin & xmax
		x[1] = 0;
		y[0] = picHeight;  // ymin & ymax
		y[1] = 0;
		Blob b; int numBlobs = 0;
		for (int n=0 ; n<theBlobDetection[level].getBlobNb() ; n++) {
		    b=theBlobDetection[level].getBlob(n);
		    if (b!=null) { 
		    	x[0] = Math.min(x[0], (int) Math.floor(b.xMin*picWidth*factor));
		    	x[1] = Math.max(x[1], (int) Math.floor(b.xMax*picWidth*factor));
		    	y[0] = Math.min(y[0], (int) Math.floor(b.yMin*picHeight*factor));
		    	y[1] = Math.max(y[1], (int) Math.floor(b.yMax*picHeight*factor));
		    	numBlobs++;
		    }
		}
		if (DEBUG) System.out.println("BlobProcess-getminmax - level"+level+". numblob:"+numBlobs);

	}
	/**
	 * calculate xmin,xmax,yin,ymax of the biggest blob at level specified
	 * @param level
	 * @param x
	 * @param y
	 */
	void processBigBlobMinMax(int level, int[] x, int[] y){
		x[0] = picWidth;   // xmin & xmax
		x[1] = 0;
		y[0] = picHeight;  // ymin & ymax
		y[1] = 0;
		Blob b; 
		int n = 0; //number of blobs
		float size, maxsize=0;
		for (n=0 ; n<theBlobDetection[level].getBlobNb() ; n++) {
		    b=theBlobDetection[level].getBlob(n);
		    if (b!=null) { 
		    	size = (b.xMax - b.xMin) * (b.yMax - b.yMin);
		    	maxsize = Math.max(maxsize, size);
		    	if(size > maxsize){
		    		maxsize = size;
		    		x[0] = (int) Math.floor(b.xMin*picWidth*factor);
		    		x[1] = (int) Math.floor(b.xMax*picWidth*factor);
			    	y[0] = (int) Math.floor(b.yMin*picHeight*factor);
			    	y[1] = (int) Math.floor(b.yMax*picHeight*factor);
		    	}
		    	
		    }
		}
		if (DEBUG) System.out.println("BlobProcess-getminmax - level"+level+". numblob:"+n+". maxSize:"+maxsize);

		
		
	}
	//////
	
	
	void showMinMaxValues(int bt){
		Blob b;
		//EdgeVertex eA,eB;
		for (int n=0 ; n<theBlobDetection[bt].getBlobNb() ; n++) {
		    b=theBlobDetection[bt].getBlob(n);
		    if (b!=null) { 
		    	// diagonal
		    	//b.xMin*img.width*factor,+b.yMin*img.height*factor
		    	//b.xMax*img.width*factor,+b.yMax*img.height*factor

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
	
	int[] getEdges(int level){
		int x,y;
		int i = level; // number of the chosen blob detection object
		int n; // number of the chosen detected blob
		int[] edges = new int[picHeight*picWidth];
		for(int j =0; j<edges.length; j++){
			edges[j] = 0;
		}
		EdgeVertex eA,eB;
		Blob b;

		for (n=0; n<theBlobDetection[i].blobNumber; n++){
			b = theBlobDetection[i].getBlob(n);
			if (b!=null) {
			    //stroke((i/levels*colorRange)+colorStart,100,100);
			    for (int m=0; m<b.getEdgeNb(); m++) {
				    eA = b.getEdgeVertexA(m);
				    eB = b.getEdgeVertexB(m);
				    if (eA !=null && eB !=null){
				    	System.out.print("("+i+","+n+"):");
				    	x = (int) (eA.x*picWidth*factor);
				    	y = (int) (eA.y*picHeight*factor);
						System.out.print(" A=("+x+","+y+")");
				    	edges[x+y*picWidth] = 3;
				    	x = (int) (eB.x*picWidth*factor);
				    	y = (int) (eB.y*picHeight*factor);
				    	edges[x+y*picWidth] = 3;
						System.out.println(" / B=("+x+","+y+")");
				    }
				    
				}
			}
		}
		return edges;
	}
	
	//////////////////// EXAMPLE //////////////////////////
	
	public static void main(String[] args) {
		test_1();
		
		
	}
	
	public static void test_1(){
		int picEx[] = {
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 250, 250, 250,
				250, 250, 250, 250, 250, 250, 50, 50, 250,
				250, 250, 250, 250, 250, 250, 50, 100, 250,
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
		
		Matrix.printMatriz(picHeight,picWidth,picEx);
		
		Picture picture = new Picture(picHeight, picWidth, picEx);
		Matrix.printMatriz(picHeight,picWidth,picture.colors);
		
		BlobProcessing b_t = new BlobProcessing(picture.height, picture.width, picture.colors);
		b_t.compute();
		int[] edges = b_t.getEdges(5);
		Matrix.printMatriz(picHeight,picWidth,edges);
	}
	
	
	
	
	
	//////////////////////////////////////////////////////
	public static void toHex(int n){
		String s = Integer.toHexString(n);
		//int i = Integer.valueOf(String.valueOf(n), 16);
		//toHex(11); // b
		//toHex(29); // 1d
		//toHex(32); // 20
		System.out.println(n+" hex:"+" "+s);
	}
	
	public static void toDec(int hex){
		int i = Integer.valueOf(String.valueOf(hex), 16);
		//toDec(0xb); // b
		//toDec(32); // 20
		System.out.println(hex+" dec:"+" "+i);
	}
	
	public void ejemplosDeMatrices(){
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
		
	}
	
}
