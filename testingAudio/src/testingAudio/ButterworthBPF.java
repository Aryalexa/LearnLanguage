package testingAudio;

import java.util.ArrayList;
/*
 *                            COPYRIGHT
 *
 *  Copyright (C) 2014 Exstrom Laboratories LLC
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  A copy of the GNU General Public License is available on the internet at:
 *  http://www.gnu.org/copyleft/gpl.html
 *
 *  or you can write to:
 *
 *  The Free Software Foundation, Inc.
 *  675 Mass Ave
 *  Cambridge, MA 02139, USA
 *
 *  Exstrom Laboratories LLC contact:
 *  stefan(AT)exstrom.com
 *
 *  Exstrom Laboratories LLC
 *  Longmont, CO 80503, USA
 *
 */

/* Modified from C to Java by Mayra
 * source: http://www.exstrom.com/journal/sigproc/bwbpf.c
 * 
 */
public class ButterworthBPF {

	// Butterworth bandpass filter
	private int n; 			//  n = filter order 4,8,12,...
	private double s ;		//  s = sampling frequency
	private double f1 ;		//  f1 = upper half power frequency
	private double f2 ;		//  f2 = lower half power frequency
	
	// Filters data using a Butterworth bandpass filter.
	// The order of the filter must be a multiple of 4.

	private double[] A =null;
	
	private double[] d1 ;
	private double[] d2 ;
	private double[] d3 ;
	private double[] d4 ;
	
	private double[] w0 ;
	private double[] w1 ;
	private double[] w2 ;
	private double[] w3 ;
	private double[] w4 ;
	
	  
	public void createFilter(int order,double sample_rate,double lowerf,double upperf){
		
		n = order;
		s = sample_rate;
		f1 = upperf;
		f2 = lowerf;
		
		if(n % 4!=0){
			System.out.println("Order must be 4,8,12,16,...\n");
			return;
		}
		
		double a = Math.cos(Math.PI*(f1+f2)/s)/Math.cos(Math.PI*(f1-f2)/s);
		double a2 = a*a;
		double b = Math.tan(Math.PI*(f1-f2)/s);
		double b2 = b*b;
		
		n = n/4;
		
		A = new double[n];// *)malloc(n*sizeof(double));
		
		d1 = new double[n];//(double *)malloc(n*sizeof(double));
		d2 = new double[n];//(double *)malloc(n*sizeof(double));
		d3 = new double[n];//(double *)malloc(n*sizeof(double));
		d4 = new double[n];//(double *)malloc(n*sizeof(double));
		
		w0 = new double[n];//(double *)calloc(n, sizeof(double)); //calloc inicializa a 0
		w1 = new double[n];//(double *)calloc(n, sizeof(double));
		w2 = new double[n];//(double *)calloc(n, sizeof(double));
		w3 = new double[n];//(double *)calloc(n, sizeof(double));
		w4 = new double[n];//(double *)calloc(n, sizeof(double));
	  
		for(int i=0; i<n; ++i){
			w0[i] = w1[i] = w2[i] = w3[i] = w4[i] = 0;
		}
  
		double r;
		for(int i=0; i<n; ++i){
			r = Math.sin(Math.PI*(2.0*i+1.0)/(4.0*n));
			s = b2 + 2.0*b*r + 1.0;
			A[i] = b2/s;
			d1[i] = 4.0*a*(1.0+b*r)/s;
			d2[i] = 2.0*(b2-2.0*a2-1.0)/s;
			d3[i] = 4.0*a*(1.0-b*r)/s;
			d4[i] = -(b2 - 2.0*b*r + 1.0)/s;
		}

	}
	
	public ArrayList<Double> filter(ArrayList<Double> input){
		
		if(A==null){
			System.out.println("Create filter first");
			return null;
		}
		
		int size = input.size();
		ArrayList<Double> output = new ArrayList<Double>();
		
		// Al filtrar solo multiplica, no hay denominador
		for(int x=0; x<size; x++){
		    for(int i=0; i<n; ++i){
				w0[i] = d1[i]*w1[i] + d2[i]*w2[i]+ d3[i]*w3[i]+ d4[i]*w4[i] + input.get(x);
				output.add(x, A[i]*(w0[i] - 2.0*w2[i] + w4[i]));
				w4[i] = w3[i];
				w3[i] = w2[i];
				w2[i] = w1[i];
				w1[i] = w0[i];
		    }
	    }
		return output;
	}

	//public static void main(String[] args) { }

}
