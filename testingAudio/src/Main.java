

/**
 * MAIN PRINCIPAL
 * @author aryalexa
 *
 */


public class Main {

	static String fileName1 = "kor2";//itadakimasuA;kor1;esp1
	static String fileName2 = "kor1";//itadakimasuA;kor1;esp1
	static boolean DEBUG = false;
		
	
	public static void main(String[] args) {
		// this should call a function from testingAudio package 
		// which do everything with its classes
		// TODO
		
		AudioProcessing ap1 = new AudioProcessing(fileName1);
		ap1.process();
		Matrix[] pics1 = ap1.getBoundedVals();

		AudioProcessing ap2 = new AudioProcessing(fileName2);
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
		double r;
		r = Matrix.cosineSimilarity(m1.mat, m2.mat);
        System.out.println("cosine similarity - res por fil:"+r);
        //r = Matrix.cosineSimilarity(Matrix.transpose(m1.h, m1.w, m1.mat), Matrix.transpose(m2.h, m2.w, m2.mat));
        //System.out.println("cosine similarity - res por col:"+r);
        
        //printMatriz(m2.mat,"m2");
        
        Matrix.printSimilarity(m1.h, m1.w, m1.mat, m2.mat);
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
