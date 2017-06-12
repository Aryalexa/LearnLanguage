package testingAudio;

public class Constantes {
	public static int CHUNK_SIZE = 1024; // 256, 512, 4096
	
	
	public static double[] quicksort(double[] _A){
		double[] A = _A.clone();
		quicksort(A, 0, A.length - 1);
		return A;
	}
	
	private static void quicksort(double[] A, int lo, int hi){
		if (lo < hi){
			int p = partition(A, lo, hi);
	        quicksort(A, lo, p);
	        quicksort(A, p + 1, hi);
		}
        
    }

	private static int partition(double[] A, int lo, int hi) {
		double pivot = A[lo];
	    int i = lo - 1;
	    int j = hi + 1;
	    while(true){
	        do {i = i + 1;}
	        while (A[i] < pivot);

	        do {j = j - 1;}
	        while (A[j] > pivot);

	        if (i >= j) {return j;}

	        //swap A[i] with A[j]
	        double temp = A[i];
        	A[i] = A[j];
        	A[j] = temp;
	    }
		
	}
	
}
