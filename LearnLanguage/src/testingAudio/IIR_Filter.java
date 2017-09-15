package testingAudio;

import org.apache.commons.math3.complex.Complex;

/**
* basado en butter de MATLAB
* 
* PD: solo hasta orden 5!!
* 
* 
* Needs: commons-math3-3.6.1.jar as library
*/

public class IIR_Filter {

	final static int N = 10; //The number of images which construct a time series for each pixel
	final static double PI = Math.PI;
	final static int filterOrder = 6;
	
	//private double[] FrequencyBands = new double[2];
	private static double Lcutoff;
	private static double Ucutoff;
	private static double[] DenC;
	private static double[] NumC;
	
	final static boolean DEBUG = false;
	final static boolean DEBUG_showSamples = false;
	
	/**
	 * Create BUTTERWORTH bandpass filter
	 * @param sampleRate
	 * @param uCutoff
	 * @param lCutoff
	 */
	public IIR_Filter(double sampleRate, double lCutoff, double uCutoff){
		// FREQUENCY BANDS
		// Frequency bands is a vector of values - Lower Frequency Band and Higher Frequency Band
	    // First value is lower cutoff and second value is higher cutoff
		/*
		 * nyq = 0.5 * fs
		 * low = lowcut / nyq
		 * high = highcut / nyq
		 * */
		double nyq = sampleRate* 0.5;
		Lcutoff = lCutoff/nyq;
		Ucutoff = uCutoff/nyq;
		// these values are as a ratio of f/fs, 
	    // where fs is sampling rate and f is cutoff frequency
		// and therefore should lie in the range [0 1]
		
	    // Create the variables for the numerator and denominator coefficients
	    // DenC is A in Matlab function
		// NumC is B in Matlab function
	    DenC = ComputeDenCoeffs();
	    NumC = ComputeNumCoeffs();
	    
	    if (DEBUG){
	    	int size = 2*filterOrder+1;
		    for(int k = 0; k<size; k++)
		    {
		        System.out.printf(k+" A-DenC is: "+DenC[k]+"\n");
		    }
		    for(int k = 0; k<size; k++)
		    {
		    	System.out.printf(k+" B-NumC is: "+NumC[k]+"\n");
		    }
	    }
	}
	
	/**
	 * filter data
	 * @param input
	 * @param output (filtered input)
	 */
	public double[] butter_bandpass_filter(double[] input){
		int size = input.length;
		double[] output;
		int np = size;//number of samples
		
		output = filter(filterOrder, DenC, NumC, np, input);  
	    //filter2(DenC, NumC, input, output);    
	    
	    if (DEBUG_showSamples){
	    	int ini = 0;
		    for(int k = ini; k<ini+100 && k<size; k++)
		    	System.out.printf(k+" x is: "+input[k]+
		    			"/\t y is: "+output[k]+
		    			//" // mult: "+ input[k]*output[k]+
		    			"\n");
	    }
	    
	    return output;
	}
	
	 private static double[] filter(int ord, double[] a, double[] b, int np,
			double[] x) {
		// denC -- a
		// numC -- b
		double[] y = new double[np];
		int i,j;
	    y[0] = b[0] * x[0];
	    for (i=1;i<ord+1;i++) // y[i],, i<=order
	    {
	        y[i] = 0.0;
	        for (j=0;j<i+1;j++)
	            y[i] += b[j]*x[i-j];
	        for (j=0;j<i;j++)
	            y[i] -= a[j+1]*y[i-j-1];
	    }
	    for (i=ord+1;i<np;i++) // y[i],, i>order
	    {
	        y[i]=0.0;
	        for (j=0;j<ord+1;j++)
	            y[i] += b[j]*x[i-j];
	        for (j=0;j<ord;j++)
	            y[i] -= a[j+1]*y[i-j-1];
	    }
	    return y;
		
	}
	private static void filter2(double[] a, double[] b, double[] x, double[] y) {
		/*Esto da mas muestras en la respuesta... no me gusta !!!! TODO */
		// denC -- a ->y   (d)
		// numC -- b ->x   (c)
		double s1; /* a sum of products of dk's and y's */
		double s2; /* a sum of products of ck's and x's */
				
		int nc = b.length; /* number of ck coeff's */
		int nd = a.length; /* number of dk coeff's */
		int nx = x.length; /* number of data points to filter */
		int ny; /* number of output points */
		ny = nx + nc;
		y = new double[ny];//(double *)calloc( ny, sizeof(double) ); /* calloc initializes to zero */
		
		for(int i=0; i<ny; ++i) {y[i]=0;}
		
		for(int i=0; i<ny; ++i){
			s1 = 0.0;
			for(int j = (i<nd ? 0 : i-nd+1); j<i; ++j)
				s1 += a[i-j] * y[j];
			
			s2 = 0.0;
			for(int j = (i < nc ? 0 : i-nc+1); j <= (i<nx ? i : nx-1); ++j )
				s2 += b[i-j] * x[j];
			
			y[i] = s2 - s1;
		    
		}
		
	}

	
	private static double[] ComputeNumCoeffs(){
		// int FilterOrder,double Lcutoff, double Ucutoff, double *DenC)
		
		double[] TCoeffs;	// double *TCoeffs;
	    double[] NumCoeffs;	// double *NumCoeffs;
	    Complex[] NormalizedKernel; // std::complex<double> *NormalizedKernel;
	    //double[] Numbers = {0,1,2,3,4,5,6,7,8,9,10}; // double Numbers[11]={0,1,2,3,4,5,6,7,8,9,10};
	    int i;
		
	    //
	    NumCoeffs = new double[2*filterOrder+1];//(double *)calloc( 2*FilterOrder+1, sizeof(double) );
	    
	    NormalizedKernel = new Complex[2*filterOrder+1];
	    		// = (complex<double> *)calloc( 2*FilterOrder+1, sizeof(complex<double>) );
	    

	    TCoeffs = ComputeHP(filterOrder);
	    if( TCoeffs == null ) return null ;

	    for( i = 0; i < filterOrder; ++i)
	    {
	        NumCoeffs[2*i] = TCoeffs[i];
	        NumCoeffs[2*i+1] = 0.0;
	    }
	    NumCoeffs[2*filterOrder] = TCoeffs[filterOrder];
	    double[] cp = new double[2];
	    double Bw, Wn;
	    cp[0] = 2*2.0*Math.tan(PI * Lcutoff/ 2.0);
	    cp[1] = 2*2.0*Math.tan(PI * Ucutoff / 2.0);

	    Bw = cp[1] - cp[0];
	    //center frequency
	    Wn = Math.sqrt(cp[0]*cp[1]);
	    Wn = 2*Math.atan2(Wn,4);
	    
	    Complex result = new Complex(-1,0);
	    //const complex<double> result = complex<double>(-1,0);
	    Complex sq = result.sqrt();
	    
	    for(int k = 0; k<filterOrder*2+1; k++)
	    {
	        //NormalizedKernel[k] = std::exp(-sqrt(result)*Wn*Numbers[k]);
	    	NormalizedKernel[k] = sq.multiply((-1)*Wn*/*numbers[k]*/k).exp();
	    }
	    double b=0;
	    double den=0;
	    for(int d = 0; d<filterOrder*2+1; d++)
	    {
	        b += (NormalizedKernel[d].multiply(NumCoeffs[d])).getReal();
	        den += (NormalizedKernel[d].multiply(DenC[d])).getReal();
	    }
	    for(int c = 0; c<filterOrder*2+1; c++)
	    {
	        NumCoeffs[c]=(NumCoeffs[c]*den)/b;
	    }

	    return NumCoeffs;
	    
	}
	
	private static double[] ComputeHP(int filterOrder) {
		double[] NumCoeffs;
	    int i;

	    NumCoeffs = ComputeLP(filterOrder);

	    for( i = 0; i <= filterOrder; ++i)
	        if( i % 2 != 0) NumCoeffs[i] = -NumCoeffs[i];

	    return NumCoeffs;
	}

	private static double[] ComputeLP(int filterOrder) {
		double[] NumCoeffs;
	    int m;
	    int i;

	    NumCoeffs = new double[filterOrder+1]; 
	    	   // = (double *)calloc( FilterOrder+1, sizeof(double) );

	    NumCoeffs[0] = 1;
	    NumCoeffs[1] = filterOrder;
	    m = filterOrder/2;
	    for( i=2; i <= m; ++i)
	    {
	        NumCoeffs[i] = (double) (filterOrder-i+1)*NumCoeffs[i-1]/i;
	        NumCoeffs[filterOrder-i]= NumCoeffs[i];
	    }
	    NumCoeffs[filterOrder-1] = filterOrder;
	    NumCoeffs[filterOrder] = 1;

	    return NumCoeffs;
	}

	private static double[] ComputeDenCoeffs() {
		int k;            // loop variables
	    double theta;     // PI * (Ucutoff - Lcutoff) / 2.0
	    double cp;        // cosine of phi
	    double st;        // sine of theta
	    double ct;        // cosine of theta
	    double s2t;       // sine of 2*theta
	    double c2t;       // cosine 0f 2*theta
	    double[] RCoeffs;     // z^-2 coefficients
	    double[] TCoeffs;     // z^-1 coefficients
	    double[] DenomCoeffs;     // dk coefficients
	    double PoleAngle;      // pole angle
	    double SinPoleAngle;     // sine of pole angle
	    double CosPoleAngle;     // cosine of pole angle
	    double a;         // workspace variables

	    cp = Math.cos(PI * (Ucutoff + Lcutoff) / 2.0);
	    theta = PI * (Ucutoff - Lcutoff) / 2.0;
	    st = Math.sin(theta);
	    ct = Math.cos(theta);
	    s2t = 2.0*st*ct;        // sine of 2*theta
	    c2t = 2.0*ct*ct - 1.0;  // cosine of 2*theta

	    RCoeffs = new double[2 * filterOrder];
	    TCoeffs = new double[2 * filterOrder];
	    	//  = (double *)calloc( 2 * FilterOrder, sizeof(double) );

	    for( k = 0; k < filterOrder; ++k )
	    {
	        PoleAngle = PI * (double)(2*k+1)/(double)(2*filterOrder);
	        SinPoleAngle = Math.sin(PoleAngle);
	        CosPoleAngle = Math.cos(PoleAngle);
	        a = 1.0 + s2t*SinPoleAngle;
	        RCoeffs[2*k] = c2t/a;
	        RCoeffs[2*k+1] = s2t*CosPoleAngle/a;
	        TCoeffs[2*k] = -2.0*cp*(ct+st*SinPoleAngle)/a;
	        TCoeffs[2*k+1] = -2.0*cp*st*CosPoleAngle/a;
	    }

	    DenomCoeffs = TrinomialMultiply(filterOrder, TCoeffs, RCoeffs );
	    

	    DenomCoeffs[1] = DenomCoeffs[0];
	    DenomCoeffs[0] = 1.0;
	    for( k = 3; k <= 2*filterOrder; ++k )
	        DenomCoeffs[k] = DenomCoeffs[2*k-2];


	    return DenomCoeffs;
	}

	private static double[] TrinomialMultiply
		(int filterOrder,double[] b, double[] c) {
		// b -- tCoeffs
		// c -- rCoeffs
		
		int i, j;
	    double[] retVal;

	    retVal = new double[4 * filterOrder];
	    	// = (double *)calloc( 4 * FilterOrder, sizeof(double) );
	    
	    retVal[2] = c[0];
	    retVal[3] = c[1];
	    retVal[0] = b[0];
	    retVal[1] = b[1];

	    for( i = 1; i < filterOrder; ++i )
	    {
	        retVal[2*(2*i+1)]   += c[2*i] * retVal[2*(2*i-1)]   - c[2*i+1] * retVal[2*(2*i-1)+1];
	        retVal[2*(2*i+1)+1] += c[2*i] * retVal[2*(2*i-1)+1] + c[2*i+1] * retVal[2*(2*i-1)];

	        for( j = 2*i; j > 1; --j )
	        {
	            retVal[2*j]   += b[2*i] * retVal[2*(j-1)]   - b[2*i+1] * retVal[2*(j-1)+1] +
	                c[2*i] * retVal[2*(j-2)]   - c[2*i+1] * retVal[2*(j-2)+1];
	            retVal[2*j+1] += b[2*i] * retVal[2*(j-1)+1] + b[2*i+1] * retVal[2*(j-1)] +
	                c[2*i] * retVal[2*(j-2)+1] + c[2*i+1] * retVal[2*(j-2)];
	        }

	        retVal[2] += b[2*i] * retVal[0] - b[2*i+1] * retVal[1] + c[2*i];
	        retVal[3] += b[2*i] * retVal[1] + b[2*i+1] * retVal[0] + c[2*i+1];
	        retVal[0] += b[2*i];
	        retVal[1] += b[2*i+1];
	    }

	    return retVal;
	}
	

	public static void main(String[] args) {
		
	    //IIR_Filter fil = new IIR_Filter(44.1, 0.3, 4.0);
		
		// EXAMPLE 1 ------------------------------------
		
		// salida de matlab:
		//    >> [B, A]=butter(5, [0.25,0.375])
		//    B = 0.0002, 0, -0.0008, 0, 0.0016, 0, -0.0016, 0, 0.0008, 0, -0.0002
		//    A = 1.0000, -4.9460, 13.5565, -24.7007, 32.9948, -33.1806, 25.5461, -14.8020, 6.2854, -1.7729, 0.2778
		
		// double FrequencyBands[] = {0.25,0.375};
	    IIR_Filter fil0 = new IIR_Filter(1, 0.25, 0.375);
	    double[] x0 = {1,2,3,4,5};
	    double[] y0 = fil0.butter_bandpass_filter(x0);
	    
		// YOUR EXAMPLE ------------------------------------
		
	    double sampleRate = 1.0;
	    double lCutoff = 0.25;
	    double uCutoff = 0.375;
	    IIR_Filter fil = new IIR_Filter(sampleRate,lCutoff,uCutoff);
	    double[] x = {1,2,3,4,5,6,7,8,9,10,11,12};
	    double[] y = fil.butter_bandpass_filter(x);
	    
	}


}
