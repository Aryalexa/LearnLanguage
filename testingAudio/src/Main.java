

/**
 * MAIN PRINCIPAL
 * @author aryalexa
 *
 */


public class Main {

							
	static String fileName1 = "esp1"; // itadakimasu0; kor0; esp1
	static String fileName2 = "kor1";
	static String[] fileNames_jap = {"itadakimasuA","itadakimasuB","itadakimasuC"};
	static String[] fileNames_esp = {"esp2","esp3","esp4"};
	static String[] fileNames_kor = {"kor1","kor2","kor3"};
	static boolean DEBUG = false;
		
	
	public static void main(String[] args) {
		// this should call a function from testingAudio package 
		// which do everything with its classes
		// TODO
		
		// 2 files
		//execute(fileName1, fileName2);
		
		
		// several files
		String[] fileNames = fileNames_kor; //fileNames_esp; fileNames_kor; fileNames_jap
		for (int i=0; i<fileNames.length; i++){
			System.out.println("Prueba "+i+ "-> "+fileName1+" - "+fileNames[i]+" - - - - - - - - - - - ");
			execute(fileName1, fileNames[i]);
		}
		
    }
	
	public static void execute(String audiofile1, String audiofile2){
		
		AudioProcessing ap1 = new AudioProcessing(audiofile1);
		ap1.process();
		Matrix[] pics1 = ap1.getBoundedVals();

		AudioProcessing ap2 = new AudioProcessing(audiofile2);
		ap2.process();
		Matrix[] pics2 = ap2.getBoundedVals();//ap2.getBoundedPics();

		
		//printMatriz(pics1[15].mat,"pic1");
		//printMatriz(pics2[15].mat,"pic2");
		
		
		comparar(pics1,pics2);
	}
	
	
	static void comparar(Matrix[] ms1, Matrix[] ms2){
		int numMat = ms1.length;
		if (DEBUG) System.out.println("numMat "+ms1.length+" - - - - - - - - - - - ");
		int maxH,maxW;
		
		for (int i=numMat/4; i<numMat; i++){
			if (ms1[i].mat != null && ms2[i].mat != null){
				System.out.println(""+i+" - thr:"+i/(1.0f*numMat)*100+"% - - - - - - - - - - - ");
				maxH = Math.max(ms1[i].h, ms2[i].h);
				maxW = Math.max(ms1[i].w, ms2[i].w);
		       
				//System.out.print("ms"+i+" - ms1_H:"+ms1[i].h+" - ms1_W:"+ms1[i].w);
		        //System.out.println(" / ms2_H:"+ms2[i].h+" - ms2_W:"+ms2[i].w);
		        //System.out.println("ms"+i+" - ms1_H*W:"+ms1[i].mat.length+" = "+ms1[i].h*ms1[i].w);
				
		        ms1[i].interpolX(maxW);
				ms1[i].interpolY(maxH);
				ms2[i].interpolX(maxW);
				ms2[i].interpolY(maxH);
				
				comparar(ms1[i],ms2[i]);
				
			}
			else if (DEBUG) System.out.println(" nulos nivel "+i+" - - - - - - - - - - - ");
		}
	}
	
	static void comparar(Matrix m1, Matrix m2){
		double cs, sim;
		cs = Matrix.cosineSimilarity(m1.mat, m2.mat); //por filas
		sim = 1 - (2 * Math.acos(cs) / Math.PI);
        System.out.println("cosine similarity:"+cs+" -> sim: "+sim);
        
        
        // transpose para hacer la similitud por columnas --> da lo mismo ;)
        //r = Matrix.cosineSimilarity(Matrix.transpose(m1.h, m1.w, m1.mat), Matrix.transpose(m2.h, m2.w, m2.mat));
        //System.out.println("cosine similarity - res por col:"+r);
        //printMatriz(m2.mat,"m2");
        
        int H = m1.h, W = m1.w;
        
        // comentar todos los prints para usar estos
        //System.out.println(sim); 											// 1
        //System.out.println(Matrix.distanciaInfinito(H,W,m1.mat,m2.mat));	// 2
        //System.out.println(Matrix.distancia1(H,W,m1.mat,m2.mat));			// 3
        //System.out.println(Matrix.distanciaF(H,W,m1.mat,m2.mat));			// 4

        
        Matrix.printDistances(H, W, m1.mat, m2.mat);
        Matrix.printStatics(m1.mat);
        Matrix.printStatics(m2.mat);
		
	}
	
	public static void printMatriz(double[] data,String name){
		int ini=0;
		int num = Math.min(100, data.length);
		for(int i=ini; i<ini+num; i++){
			System.out.println(name+" "+data[i]);
		}
	}

}
