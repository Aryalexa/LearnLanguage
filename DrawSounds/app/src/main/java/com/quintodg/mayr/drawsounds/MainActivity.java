package com.quintodg.mayr.drawsounds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.util.fft.FFT;

public class MainActivity extends AppCompatActivity implements PitchDetectionHandler {


    private AudioDispatcher dispatcher;
    //private Mixer currentMixer;
    //private PitchEstimationAlgorithm algo;
    private double pitch;

    private int bufferSize = 1024 * 4;

    private static RadioGroup rg_pitch_detection;
    private static RadioButton rb_yin, rb_mpm, rb_fft_yin, rb_dyn_wavelet, rb_fft_pitch, rb_amdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rg_pitch_detection = (RadioGroup)findViewById(R.id.rg_pitchDec);
        rb_yin = (RadioButton)findViewById(R.id.rb_yin);
        rb_mpm = (RadioButton)findViewById(R.id.rb_mpm);
        rb_fft_yin = (RadioButton)findViewById(R.id.rb_fft_yin);
        rb_dyn_wavelet = (RadioButton)findViewById(R.id.rb_dyn_wavelet);
        rb_fft_pitch = (RadioButton)findViewById(R.id.rb_fft_pitch);
        rb_amdf = (RadioButton)findViewById(R.id.rb_amdf);



    }

    AudioProcessor fftProcessor = new AudioProcessor(){

        FFT fft = new FFT(bufferSize);
        float[] amplitudes = new float[bufferSize/2];

        @Override
        public void processingFinished() {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean process(AudioEvent audioEvent) {
            float[] audioFloatBuffer = audioEvent.getFloatBuffer();
            float[] transformBuffer = new float[bufferSize * 2];
            System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);
            fft.forwardTransform(transformBuffer);
            fft.modulus(transformBuffer, amplitudes);
            // TODO panel.drawFFT(pitch, amplitudes,fft);
            // TODO panel.repaint();
            return true;
        }

    };

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if(pitchDetectionResult.isPitched()){
            pitch = pitchDetectionResult.getPitch();
        } else {
            pitch = -1;
        }
    }
}
