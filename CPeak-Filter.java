package com.newventuresoftware.waveformdemo;

/**
 * Created by aryalexa on 1/8/16.
 */

/**
 * FIR filter class, by Mike Perkins
 *
 * a simple C++ class for linear phase FIR filtering
 *
 * For background, see the post http://www.cardinalpeak.com/blog?p=1841
 *
 * Copyright (c) 2013, Cardinal Peak, LLC.  http://www.cardinalpeak.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1) Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2) Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 *
 * 3) Neither the name of Cardinal Peak nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * CARDINAL PEAK, LLC BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

/*
 *
 * PURPOSE:
 * This object designs digital filters and filters digital data streams
 *
 * USAGE:
 * Invoke an object of type Filter.  Two constructors are available.
 * One is used for LPF and HPF filters, one is used for BPFs.
 * The arguments to the constructors are as follows:
 *
 * 		// For LPF or HPF only
 * 		Filter(filterType filt_t, int num_taps, double Fs, double Fx);
 * 		// For BPF only
 * 		Filter(filterType filt_t, int num_taps, double Fs, double Fl, double Fu);
 *
 * filt_t: is LPF, HPF or BPF
 * num_taps: is the number of taps you want the filter to use (= order+1)
 * Fs: is the sampling frequency of the digital data being filtered
 * Fx: is the "transition" frequency for LPF and HPF filters
 * Fl, Fu: are the upper and lower transition frequencies for BPF filters
 *
 * Once the filter is created, you can start filtering data.  Here
 * is an example for 51 tap lowpass filtering of an audio stream sampled at
 * 44.1Khz (the CD sampling rate), where the goal is to create a signal
 * of "telephone" bandwidth (4Khz):
 *
 * Filter *my_filter;
 *
 * my_filter = new Filter(LPF, 51, 44.1, 4.0)
 * if( my_filter.get_error_flag() != 0 ) // abort in an appropriate manner
 *
 * while(data_to_be_filtered){
 * 	next_sample = // Get the next sample from the data stream somehow
 * 	filtered_sample = my_filter.do_sample( next_sample );
 *   .
 * 	.
 * 	.
 * }
 * delete my_filter;
 *
 * Several helper functions are provided:
 *     init(): The filter can be re-initialized with a call to this function
 *     get_taps(double *taps): returns the filter taps in the array "taps"
 *     write_taps_to_file(char *filename): writes the filter taps to a file
 *     write_freqres_to_file(char *filename): output frequency response to a file
 *
 * Finally, a get_error_flag() function is provided.  Recommended usage
 * is to check the get_error_flag() return value for a non-zero
 * value after the new Filter object is created.  If it is non-zero, print
 * out the non-zero value and look at the following table to see the
 * error:
 * -1:  Fs <= 0
 * -2:  Fx <= 0 or Fx >= Fs/2
 * -3:  num_taps <= 0 or num_taps >= MAX_NUM_FILTER_TAPS
 * -4:  memory allocation for the needed arrays failed
 * -5:  an invalid filterType was passed into a constructor
 * -10: Fs <= 0 (BPF case)
 * -11: Fl >= Fu
 * -12: Fl <= 0 || Fl >= Fs/2
 * -13: Fu <= 0 || Fu >= Fs/2
 * -14: num_taps <= 0 or num_taps >= MAX_NUM_FILTER_TAPS (BPF case)
 * -15:  memory allocation for the needed arrays failed (BPF case)
 * -16:  an invalid filterType was passed into a constructor (BPF case)
 *
 * Note that if a non-zero error code value occurs, every call to do_sample()
 * will return the value 0. write_taps_fo_file() will fail and return a -1 (it
 * also returns a -1 if it fails to open the tap file passed into it).
 * get_taps() will have no effect on the array passed in if the error_flag
 * is non-zero. write_freqres_to_file( ) returns different error codes
 * depending on the nature of the error...see the function itself for details.
 *
 * The filters are designed using the "Fourier Series Method".  This
 * means that the coefficients of a Fourier Series approximation to the
 * frequency response of an ideal filter (LPF, HPF, BPF) are used as
 * the filter taps.  The resulting filters have some ripple in the passband
 * due to the Gibbs phenomenon; the filters are linear phase.
 */
public class Filter {

    static final double M_PI = 3.1415972;
    static final int MAX_NUM_FILTER_TAPS = 1000;

    public enum FilterType {
        LPF, HPF, BPF
    }

    private FilterType m_filt_t;
    private int m_num_taps;
    private int m_error_flag;
    private double m_Fs;
    private double m_Fx;
    private double m_lambda;
    private double m_taps[]; // b_i , coeff
    private double m_sr[]; // x[i]

    // Only needed for the bandpass filter case
    private double m_Fu, m_phi;

    /* Example use on C++
        while(1){
            num_read = fread(&samp_dat, sizeof(short), 1, fd_in);
            if(num_read != 1) break;
            out_val = my_filter->do_sample( (double) samp_dat );
            samp_dat = (short) out_val;
            fwrite(&samp_dat, sizeof(short), 1, fd_out);
        }
     */

    /*For LPF and HPF*/
    public Filter(FilterType filt_t, int num_taps, double Fs, double Fx){
        m_error_flag = 0;
        m_filt_t = filt_t;
        m_num_taps = num_taps;
        m_Fs = Fs;
        m_Fx = Fx;
        m_lambda = M_PI * Fx / (Fs/2);

        if( Fs <= 0 ) ECODE(-1);
        if( Fx <= 0 || Fx >= Fs/2 ) ECODE(-2);
        if( m_num_taps <= 0 || m_num_taps > MAX_NUM_FILTER_TAPS ) ECODE(-3);

        m_taps = m_sr = null;
        m_taps = new double[m_num_taps];//(double*)malloc( m_num_taps * sizeof(double) );
        m_sr = new double[m_num_taps];//(double*)malloc( m_num_taps * sizeof(double) );
        if( m_taps == null || m_sr == null ) ECODE(-4);

        init();

        if( m_filt_t == FilterType.LPF ) designLPF();
        else if( m_filt_t == FilterType.HPF ) designHPF();
        else ECODE(-5);
      }

    /*For BPF only*/
    public Filter(FilterType filt_t, int num_taps, double Fs, double Fl, double Fu) {
        m_error_flag = 0;
        m_filt_t = filt_t;
        m_num_taps = num_taps;
        m_Fs = Fs;
        m_Fx = Fl;
        m_Fu = Fu;
        m_lambda = M_PI * Fl / (Fs/2);
        m_phi = M_PI * Fu / (Fs/2);

        if( Fs <= 0 ) ECODE(-10);
        if( Fl >= Fu ) ECODE(-11);
        if( Fl <= 0 || Fl >= Fs/2 ) ECODE(-12);
        if( Fu <= 0 || Fu >= Fs/2 ) ECODE(-13);
        if( m_num_taps <= 0 || m_num_taps > MAX_NUM_FILTER_TAPS ) ECODE(-14);

        m_taps = m_sr = null;
        m_taps = new double[m_num_taps];//(double*)malloc( m_num_taps * sizeof(double) );
        m_sr = new double[m_num_taps];//(double*)malloc( m_num_taps * sizeof(double) );
        if( m_taps == null || m_sr == null ) ECODE(-15);

        init();

        if( m_filt_t == FilterType.BPF ) designBPF();
        else ECODE(-16);
    }

    void init(){
        int i;

        if( m_error_flag != 0 )
            return;

        for(i = 0; i < m_num_taps; i++)
            m_sr[i] = 0;

    }

    double do_sample (double data_sample){
        // esto se aplica a cada muestra que es un double, hay que recorrer todas las muestras!
        int i;
        double result;

        if( m_error_flag != 0 ) return(0);

        for(i = m_num_taps - 1; i >= 1; i--){
            m_sr[i] = m_sr[i-1];
        }
        m_sr[0] = data_sample;

        result = 0;
        for(i = 0; i < m_num_taps; i++)
          result += m_sr[i] * m_taps[i];

        return result;
    }

    void designBPF(){
        int n;
        double mm;

        for(n = 0; n < m_num_taps; n++){
            mm = n - (m_num_taps - 1.0) / 2.0;
            if( mm == 0.0 ) m_taps[n] = (m_phi - m_lambda) / M_PI;
            else m_taps[n] = (   Math.sin( mm * m_phi ) -
                    Math.sin( mm * m_lambda )   ) / (mm * M_PI);
        }
    }

    void designLPF(){
        int n;
        double mm;

        for(n = 0; n < m_num_taps; n++){
            mm = n - (m_num_taps - 1.0) / 2.0;
            if( mm == 0.0 ) m_taps[n] = m_lambda / M_PI;
            else m_taps[n] = Math.sin( mm * m_lambda ) / (mm * M_PI);
        }
    }

    void designHPF(){
        int n;
        double mm;

        for(n = 0; n < m_num_taps; n++){
            mm = n - (m_num_taps - 1.0) / 2.0;
            if( mm == 0.0 ) m_taps[n] = 1.0 - m_lambda / M_PI;
            else m_taps[n] = -Math.sin( mm * m_lambda ) / (mm * M_PI);
        }
    }

    double[] get_taps(){
        double taps[] = new double[m_num_taps];
        int i;

        if( m_error_flag != 0 ) return null;

        //solo devuelve el atributo!! TODO
        for(i = 0; i < m_num_taps; i++)
            taps[i] = m_taps[i];

        return taps;
    }

    void ECODE(int x) {m_error_flag = x;}
}
