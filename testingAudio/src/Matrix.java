import testingAudio.Constantes;


public class Matrix {

	double[] mat;
	int h;
	int w;
	
	public Matrix(int matrix[], int height, int width){
		assert(matrix.length == height*width);
		h = height;
		w = width;
		mat =  new double[h*w];
		for (int i=0; i<h*w; i++){
			mat[i] = matrix[i];
		}
	}
	
	/**
	 * 
	 * @param matrix
	 * @param height
	 * @param width
	 */
	public Matrix(double matrix[], int height, int width){
		assert(matrix.length == height*width);
		h = height;
		w = width;
		mat =  new double[h*w];
		for (int i=0; i<h*w; i++){
			mat[i] = matrix[i];
		}
	}
	
	
	/**
	 * 
	 * @param x1
	 * @param x2
	 */
	public void cortarEjeX(int x1, int x2){
		mat = Matrix.cortarEjeX(mat,h,w,x1,x2);
		if (x1<=x2 && x2<=w) w = x2-x1+1;
	}
	
	public double[] getCortarEjeX(int x1, int x2){
		return  Matrix.cortarEjeX(mat,h,w,x1,x2);
	}
	
	
	/**
	 * cortar eje x [x1,x2] - double
	 * @param mat
	 * @param H
	 * @param W
	 * @param x1 extremo eje x menor
	 * @param x2 extremo eje x mayor
	 * @return
	 */
	static double[] cortarEjeX(double mat[], int H, int W, int x1, int x2){
		if (!(x1<=x2 && x2<=W)) return null;

		int newW = x2-x1+1;
		double[] newMat = new double[newW*H];
		for (int j=0; j<H; j++){
			for (int i=0; i<newW; i++){
				newMat[newW*j +i] = mat[W*j +x1 +i];
			}
		}
		return newMat;
	}
	
	/**
	 * 
	 * @param y1
	 * @param y2
	 */
	public void cortarEjeY(int y1, int y2){
		mat = Matrix.cortarEjeY(mat,h,w,y1,y2);
		if (y1<=y2 && y2<=h) h = y2-y1+1;
	}
	
	public double[] getCortarEjeY(int y1, int y2){
		return Matrix.cortarEjeY(mat,h,w,y1,y2);
	}
	
	/**
	 * cortar eje Y [x1,x2] -  int
	 * @param mat
	 * @param H
	 * @param W
	 * @param y1 extremo eje y menor
	 * @param y2 extremo eje y mayor
	 * @return
	 */
	static double[] cortarEjeY(double mat[], int H, int W, int y1, int y2){
		if (!(y1<=y2 && y2<H)) return null;
		
		int newH = y2-y1+1;
		double[] newMat = new double[W*newH];
		for (int i=0; i<newH; i++){
			for (int j=0; j<W; j++){
				newMat[W*i +j] = mat[W*(i + y1) +j];
			}
		}
		return newMat;
	}
	
	/**
	 * interpolar eje x (interpolacion lineal)
	 * @param newW
	 */
	public void interpolX(int newW){
		mat = Matrix.interpolX(h, w, newW, mat);
		w = newW;
	}
	
	static double[] interpolX(int H, int W, int newW, double[] content){
		double[] newContent = new double[H*newW];
		//System.out.println(" w:"+W+" nw:"+newW);
		for(int j=0; j<H; j++){
			//System.out.println("--- row "+j);
			// for all rows
			newContent[j*newW] = content[j*W];
			int k = 1; // k: para recorrer las columnas de content original
			for(int i=1; i<newW; i++){ // i: para recorrer las columnas de newContent
				if ( ((double)i)/newW > ((double)k)/W && k!=W-1) { k++; }
				
				//System.out.println("*   i/nW:"+i+"/"+newW+"="+((double)i)/newW);
				//System.out.println(" <= k/ W:"+k+"/"+   W+"="+((double)k)/W);
				double prev = content[j*W+k-1];
				double div = 1./W;
				double mul = (double)i/newW - (double)k/W;
				newContent[j*newW + i] = prev + (content[j*W+k] - prev)*mul/div;
				
			}
			//System.out.println("");
		}
		
		return newContent;
	}
	
	/**
	 * interpolar eje y (interpolacion lineal)
	 * @param newH
	 */
	public void interpolY(int newH){
		mat = Matrix.interpolY(h, w, newH, mat);
		h = newH;
	}
	
	static double[] interpolY(int H, int W, int newH, double[] content){
		double[] newContent = new double[newH*W];
		//System.out.println(" h:"+H+" new:"+newH);
		for(int j=0; j<W; j++){ // column
			//System.out.println("--- column "+j+"--------------------");
			// for all cols
			newContent[j] = content[j];
			int k = 1; // k: para recorrer las filas de content original
			for(int i=1; i<newH; i++){ // i: para recorrer las filas de newContent
				
				if ( ((double)i)/newH > ((double)k)/H && k!=H-1) { k++; }
				
				//System.out.println("*   i/nH:"+i+"/"+newH+"="+((double)i)/newH);
				//System.out.println(" <= k/ H:"+k+"/"+   H+"="+((double)k)/H);
				double prev = content[(k-1)*W+j];
				double div = 1./H;
				double mul = (double)i/newH - (double)k/H;
				newContent[i*W + j] = prev + (content[k*W+j] - prev)*mul/div;
				
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
	
	// normalizar intensidades (enteros)
	static public int[] normalizeVal(int H, int W, int[] content, int scale){
		// valores: (0 - newMax)
		int newMax = scale;
		int[] newContent = new int[H*W];
		
		int max = content[0];
		int min = content[0];
		for (int i=1; i<H*W; i++){
			max = Math.max(max, content[i]);
			min = Math.min(min, content[i]);
		}
		
		for (int i=0; i<H*W; i++){
			newContent[i] = (content[i]-min)*newMax/(max-min);
		}
		return newContent;
	}
	
	// normalizar intensidades (doubles)
		static public double[] normalizeVal(int H, int W, double[] content, int scale){
			// valores: (0 - newMax)
			int newMax = scale;
			double[] newContent = new double[H*W];
			
			double max = content[0];
			double min = content[0];
			for (int i=1; i<H*W; i++){
				max = Math.max(max, content[i]);
				min = Math.min(min, content[i]);
			}
			
			for (int i=0; i<H*W; i++){
				newContent[i] = (content[i]-min)*newMax/(max-min);
			}
			return newContent;
		}
	
	// escala logartimica, logarithmic scale
	static public double[] scaleLog(int height, int width, double[] content){
		double[] newContent = new double[height*width];
		for(int i=0;  i<height*width; i++){
			newContent[i] = 10*Math.log10(content[i]);
		}
		return newContent;
	}
	
	// transpuesta
	static public double[] transpose(int height, int width, double[] content){
		double[] newContent = new double[height*width];
		// from fila+fila >> columna+columna
		int n=0;
		for (int j=0; j<width; j++){
			for (int i=0; i<height; i++){
				newContent[n] = content[width*i + j];
				n++;
			}
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
	        normA += vectorA[i] * vectorA[i];
	        normB += vectorB[i] * vectorB[i];
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)); // = cos(a)
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
	
	// NORMA Frobenius 
	public static double normaFrobenius(int H, int W, double[] m){
		double norm = 0;
		for(int i=0; i<H*W; i++){
			norm += Math.pow(m[i], 2);
		}
		return Math.sqrt(norm);
	}
	
	/**
	 * NORMA1. máxima suma absoluta de las columnas de la matriz.
	 * @param H
	 * @param W
	 * @param m
	 * @return
	 */
	public static double norma1(int H, int W, double[] m){
		double suma;
		double norm = 0;
		
		for (int j=0; j<W; j++){
			suma = 0;
			for (int i=0; i<H; i++){
				suma += Math.abs(m[W*i + j]); //leyendo columnas
			}
			norm = Math.max(norm, suma);
		}
		return norm;
	}
	
	/**
	 * 	NORMA INFINITO. máxima suma absoluta de las filas de la matriz.
	 * @param H
	 * @param W
	 * @param m
	 * @return
	 */
	public static double normaInfinito(int H, int W, double[] m){
		double suma;
		double norm = 0;
		
		for (int i=0; i<H; i++){
			suma = 0;
			for (int j=0; j<W; j++){
				suma += Math.abs(m[W*i + j]); //leyendo filas
			}
			norm = Math.max(norm, suma);
		}
		return norm;
	}
	
	
	
	////////// distancias
	
	public static double distanciaF(int H, int W, double[] m1, double[] m2){
		double[] dif = matrixAbsDifference(H,W,m1,m2);
		return normaFrobenius(H,W,dif);
	}
	public static double distancia1(int H, int W, double[] m1, double[] m2){
		double[] dif = matrixAbsDifference(H,W,m1,m2);
		return norma1(H,W,dif);
	}
	public static double distanciaInfinito(int H, int W, double[] m1, double[] m2){
		double[] dif = matrixAbsDifference(H,W,m1,m2);
		return normaInfinito(H,W,dif);
	}
	
	
	public static double[] matrixAbsDifference(int H, int W, double[] m1, double[] m2){
		double[] dif = new double[H*W];
		for(int i=0; i<H*W; i++){
			dif[i] = Math.abs(m1[i]-m2[i]);
		}
		return dif;
	}
	
	////////////////////// similitud
	/**
	 * Comparamos las matrices calculando las distancias entre ellas.
	 * Usamos la distancia infinito, la uno y la de Frobenius.
	 * @param H altura de las matrices
	 * @param W ancho de las matrices
	 * @param m1 matriz 1, se trata de un array de doubles
	 * @param m2 matriz 2, se trata de un array de doubles
	 */
	public static void printSimilarity(int H, int W, double[] m1, double[] m2){
		double dist;
		System.out.println("DIST -");

		
		dist = distanciaInfinito(H,W,m1,m2);
		System.out.println(" inf.: "+dist);

		dist = distancia1(H,W,m1,m2);
		System.out.println(" uno.: "+dist);
		
		dist = distanciaF(H,W,m1,m2);
		System.out.println(" frb.: "+dist);
		
	}
	
	/**
	 * Calculamos el valor máximo y la media de los valores.
	 * @param v array de doubles
	 */
	public static void printStatics(double[] v){
		System.out.println("EST -");
		System.out.println(" max.  : "+getMaxVal(v));
		System.out.println(" media.: "+getMedia(v));
		//sSystem.out.println(" moda. : "+getModa(v));
		
	}
	
	public static double getMaxVal(double[] v){
		double max = -1;
		for (int i=0; i<v.length; i++){
			max = Math.max(max, v[i]);
		}
		return max;
	}
	
	public static double getMedia(double[] v){
		double media = 0;
		for (int i=0; i<v.length; i++){
			media += v[i];
		}
		return media/v.length;
	}
	
	public static double getModa(double[] v){
		double[] vOrd = Constantes.quicksort(v);
		double moda = vOrd[0];
		int maxcont = 1;
		int cont = 1;
		for (int i=1; i<vOrd.length; i++){
			if (vOrd[i]==vOrd[i-1]){
				cont++;
				if (cont > maxcont){
					maxcont = cont;
					moda = vOrd[i];
				}
			} else {
				cont = 1;
			}
		}
		//System.out.println("(((moda: "+moda + "("+maxcont+"veces))))");

		return moda;
	}
	
	///////////////////////// TESTS
	
	public static void main(String[] args) {
		//test_mat();
		//test_estadisticas();
		test_interpol();
		
	}

	static void test_interpol(){
		int hh = 5;
		int ww = 5;
		double[] m = {
			1,2,3,4,5,
			2,3,4,5,6,
			3,4,5,6,7,
			4,5,6,7,8,
			5,6,7,8,9
		};
		printMatriz(hh,ww,m);
		
		double[] m2 = interpolX(hh,ww,ww+10,m);
		printMatriz(hh,ww+10,m2);
		
		double[] m3 = interpolY(hh,ww,hh+10,m);
		printMatriz(hh+10,ww,m3);
	}
	
	static void test_mat(){
		int hh = 5;
		int ww = 5;
		double[] m = {
			1,2,3,4,5,
			2,3,4,5,6,
			3,4,5,6,7,
			4,5,6,7,8,
			5,6,7,8,9
		};
		double[] n = {
				1,2,3,4,5,
				2,3,4,5,6,
				3,0,5,6,7,
				4,5,6,0,8,
				5,6,7,8,9
			};
		
		Matrix mm = new Matrix(n,hh,ww);
		mm.cortarEjeY(1, 3);
		System.out.println("Matrix. h:"+mm.h+" w:"+mm.w);
		printMatriz(mm.h,mm.w,mm.mat);
		
		
		
		//ouble[] m2 = normalizeVal(hh,  ww, m);
		//printMatriz(hh,ww,m2);
		
		//double[] m3 = matrixL2norm(hh, ww, m);
		//printMatriz(hh,ww,m3);
		
		double[] m4 = duplicarEjeY(2,hh,ww,m);
		printMatriz(hh*2,ww,m4);
		
		double[] p = {
				1,1,1,
				1,2,1
			};
		hh=2; ww = 3;
		double[] p2 = matrixL2norm(hh,ww,p);
		double[] simN = cosineSimilarity4Matrix(hh,ww,p2,hh,ww,p2);
		printMatriz(hh,hh,simN);
		    
	}
	static void test_estadisticas(){
		double[] vec = {2,34,5,53,232,5,34,5,45,4,4,2,6,234,2,23,5};
		printStatics(vec);
		
		double[] vOrd = Constantes.quicksort(vec);
		for (int i=0; i<vOrd.length; i++){
			System.out.println(" "+vOrd[i]);

		}
		
	}
}
