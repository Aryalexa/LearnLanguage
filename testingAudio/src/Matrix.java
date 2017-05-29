
public class Matrix {

	double[] mat;
	int h;
	int w;
	
	
	public Matrix(double matrix[], int height, int width){
		assert(matrix.length == height*width);
		h = height;
		w = width;
		mat =  new double[h*w];
		for (int i=0; i<h*w; i++){
			mat[i] = matrix[i];
		}
	}
	
	double[] getCortarEjeX(int x1, int x2){
		assert(0<=x1 && x1<=x2 && x2<w);
		int newW = x2-x1+1;
		double[] newMat = new double[newW*h];
		for (int j=0; j<h; j++){
			for (int i=0; i<newW; i++){
				newMat[newW*j +i] = mat[w*j +x1 +i];
			}
		}
		return newMat;
	}
	
	void cortarEjeX(int x1, int x2){
		assert(0<=x1 && x1<=x2 && x2<w);
		double[] newMat = getCortarEjeX(x1,x2);
		w = x2-x1+1; // new w
		System.arraycopy( newMat, 0, mat, 0, newMat.length );
	}
	
	/**
	 * cortar eje x [x1,x2]
	 * @param mat
	 * @param H
	 * @param W
	 * @param x1 extremo eje x menor
	 * @param x2 extremo eje x mayor
	 * @return
	 */
	static double[] cortarEjeX(double mat[], int H, int W, int x1, int x2){
		assert(x1<x2 && x2<W);
		int newW = x2-x1+1;
		double[] newMat = new double[newW*H];
		for (int j=0; j<H; j++){
			for (int i=0; i<newW; i++){
				newMat[newW*j +i] = mat[W*j +x1 +i];
			}
		}
		return newMat;
	}
	
	// interpolar eje x (interpolacion lineal)
	static double[] interpolX(int H, int W, int newW, double[] content){
		double[] newContent = new double[H*newW];
		//System.out.println(" w:"+W+" nw:"+newW);
		double next;
		for(int j=0; j<H; j++){
			//System.out.println("--- row "+j);
			// for all rows
			int k = 0; // k: para recorrer las columnas de content original
			newContent[j*newW] = content[j*W];
			for(int i=1; i<newW; i++){ // i: para recorrer las columnas de newContent
				if ( ((double)i)/newW > ((double)k+1)/W) { k++; }
				
				if (i<newW-1) next = content[j*W+k+1];
				else next = content[j*W+k];
				
				//System.out.println(" k:"+k+" i:"+i+" k:"+(k+1)+" (x2="+(j*W+k+1)+"); ");
				double div = (1/((double)W-1));
				//System.out.println(" div:"+ div + "("+(W-1)+")");
				double mul = ((double)i/(newW-1))-((double)k/(W-1));
				//System.out.println(" mul:"+ mul + "("+(newW-1)+")");
				newContent[j*newW + i] = content[j*W+k] + (next-content[j*W+k])/div*mul;
				
			}
			//System.out.println("");
		}
		
		return newContent;
	}
	
	
	
	// interpolar eje x (interpolacion lineal)
	static double[] interpolY(int H, int W, int newH, double[] content){
		double[] newContent = new double[newH*W];
		//System.out.println(" h:"+H+" new:"+newH);
		double next;
		for(int j=0; j<W; j++){ // column
			//System.out.println("--- column "+j);
			// for all cols
			int k = 0; // k: para recorrer las filas de content original
			newContent[j] = content[k*W+j];
			for(int i=1; i<newH; i++){ // i: para recorrer las filas de newContent
				if ( ((double)i)/newH > ((double)k+1)/H) { k++; }
				
				if (i<newH-1) next = content[(k+1)*W+j];
				else next = content[k*W+j];
				
				//System.out.println(" k:"+k+" i:"+i+" k:"+(k+1)+" (x2="+(j*H+k+1)+"); ");
				double div = (1/((double)H-1));
				//System.out.println(" div:"+ div + "("+(H-1)+")");
				double mul = ((double)i/(newH-1))-((double)k/(H-1));
				//System.out.println(" mul:"+ mul + "("+(newH-1)+")");
				newContent[i*W + j] = content[k*W+j] + (next-content[k*W+j])/div*mul;
				
			}
			//System.out.println("");
		}
		
		return newContent;
	}
	
	static double[] doubleFromInt(int[] content){
		double[] content_d = new double[content.length];
		for(int i=0; i<content.length;i++){
			content_d[i] = content[i];
		}
		return content_d;
	}
	
	static int[] duplicarEjeY(int veces,int H, int W, int[] content){
		assert(veces>0);
		int[] newMat = new int[H*W*veces];
		for(int i=0; i<H; i++){
			for(int k=0; k<veces; k++){
				for(int j=0; j<W; j++){
					//if(i*j*k<100)System.out.println("Imprimiendo: fila"+(i*veces+k)+" columna"+j+" ("+i+"i,"+k+"k,"+j+"j)");
					newMat[(i*veces+k)*W+j] = content[i*W+j];
				}
			}
		}
		
		return newMat;
	}
	
	static double[] duplicarEjeY(int veces,int H, int W, double[] content){
		assert(veces>0);
		double[] newMat = new double[H*W*veces];
		for(int i=0; i<H; i++){
			for(int k=0; k<veces; k++){
				for(int j=0; j<W; j++){
					newMat[(i*veces+k)*W+j] = content[i*W+j];
				}
			}
		}
		
		return newMat;
	}
	
	// normalizar intensidades
	static public double[] normalizeVal(int H, int W, double[] content){
		// valores: (0 - 100)
		double[] newContent = new double[H*W];
		
		double max = content[0];
		double min = content[0];
		for (int i=1; i<H*W; i++){
			max = Math.max(max, content[i]);
			min = Math.min(min, content[i]);
		}
		
		for (int i=0; i<H*W; i++){
			newContent[i] = (content[i]-min)*100/(max-min);
		}
		return newContent;
	}

	//print
	static void printMatriz(int height, int width, double[] content){
		System.out.println("int matrix[] = { ");
		System.out.print("    ");
		for (int j=0; j<width;j++){
			System.out.print(j+"    ");
		}
		System.out.println(" ");
		for (int i=0; i<height; i++){
			System.out.print(i+"   ");
			for (int j=0; j<width;j++){
				System.out.print(content[i*width+j]+", ");
			}
			System.out.println(" ");
		}
		System.out.println("}");
	}
	
	// print
	static void printMatriz(int height, int width, int[] content){
		System.out.println("int matrix[] = { ");
		System.out.print("    ");
		for (int j=0; j<width;j++){
			System.out.print(j+"    ");
		}
		System.out.println(" ");
		for (int i=0; i<height; i++){
			System.out.print(i+"   ");
			for (int j=0; j<width;j++){
				System.out.print(content[i*width+j]+", ");
			}
			System.out.println(" ");
		}
		System.out.println("}");
	}
	
	////////////////////////// cosine similarity
	
	// http://stackoverflow.com/questions/520241/how-do-i-calculate-the-cosine-similarity-of-two-vectors
	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	/**
	 * nomalizar matriz. Norma 2
	 * @param height
	 * @param width
	 * @param content
	 * @return
	 */
	public static double[] matrixL2norm(int height, int width, double[] content){
		double[] m = new double[height*width];
		
		for (int i=0; i<height; i++){ // for each row
			double norm = 0.0;
			// calculate norm
			for (int j=0; j<width;j++){
				norm += Math.pow(content[i*width+j], 2);
			}
			// normalize row
			for (int j=0; j<width;j++){
				m[i*width+j] = content[i*width+j]/Math.sqrt(norm);
			}
		}
		
		return m;
	}
	
	// matriz de similitud (coseno)
	public static double[] cosineSimilarity4Matrix(int H1, int W1, double[] m1, int H2, int W2, double[] m2){
		// W1 = W2
		// asumimos que las filas de m1 y m2 estan normalizadas
		double[] simMat = new double[H1*H2];
		for (int j=0; j<H1; j++){ // height
			for (int i=0; i<H2; i++){ // width
				simMat[j*H2 +i] = 0;
				for (int k=0; k<W1; k++){ // W1 = W2
					simMat[j*H2 +i] +=  m1[j*W1 + k]*m2[i*W1 +k];
				}
			}
		}
		return simMat;
	}
	
	//////////////////////// normas
	
	// frobenius norm
	public static double fobreniusNorm(int H, int W, double[] m){
		double norm = 0;
		for(int i=0; i<H*W; i++){
			norm += Math.pow(m[i], 2);
		}
		return Math.sqrt(norm);
	}
	
	public static double[] matrixAbsDifference(int H, int W, double[] m1, double[] m2){
		double[] dif = new double[H*W];
		for(int i=0; i<H*W; i++){
			dif[i] = Math.abs(m1[i]-m2[i]);
		}
		return dif;
	}
	
	////////////////////// similitud
	
	public static double similarityFromDistance(double dist){
		return (1- dist);
	}
	
	/////////////////////////
	
	public static void main(String[] args) {
		int hh = 5;
		int ww = 5;
		double[] m = {
			1,2,3,4,5,
			2,3,4,5,6,
			3,4,5,6,7,
			4,5,6,7,8,
			5,6,7,8,9
		};
		
		
		//double[] m2 = cortarEjeX(m, hh, ww, 1, 3);
		//printMatriz(hh,3,m2);
		
		double[] m2 = interpolX(hh,ww,ww+1,m);
		printMatriz(hh,ww+1,m2);
		double[] m3 = interpolY(hh,ww,hh+1,m);
		printMatriz(hh+1,ww,m3);
		
		//ouble[] m2 = normalizeVal(hh,  ww, m);
		//printMatriz(hh,ww,m2);
		
		//double[] m3 = matrixL2norm(hh, ww, m);
		//printMatriz(hh,ww,m3);
		
		double[] m4 = duplicarEjeY(2,hh,ww,m);
		printMatriz(hh*2,ww,m4);
		
		double[] n = {
				1,1,1,
				1,2,1
			};
		hh=2; ww = 3;
		double[] n2 = matrixL2norm(hh,ww,n);
		double[] simN = cosineSimilarity4Matrix(hh,ww,n2,hh,ww,n2);
		printMatriz(hh,hh,simN);
		    
	}
	
}
