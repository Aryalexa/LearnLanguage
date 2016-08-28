package com.aryalexa.sectionsapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.newventuresoftware.waveform.WaveformView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.complex.Complex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class MyAppActivity extends Activity {

    private WaveformView mRealtimeWaveformView;
    private RecordingThread mRecordingThread;
    private PlaybackThread mPlaybackThread;

    private static final int REQUEST_RECORD_AUDIO = 13;

    private Button btn_rec = null;
    private Button btn_play = null;
    private Button btn_p1 = null;
    private Button btn_p2 = null;
    private Button btn_p3 = null;
    private Button btn_p4 = null;

    private String audioFileName;
    private String audioFileName_fil1;
    private String audioFileName_fil2;
    private String audioFileName_ttf;
    private String audioFileName_subs;


    private static final String LOG_TAG = "Main App";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app);


        btn_play = (Button) findViewById(R.id._btn_play);

        btn_p1 = (Button) findViewById(R.id.btn_p1);
        btn_p1.setEnabled(false);
        btn_p2 = (Button) findViewById(R.id.btn_p2);
        btn_p2.setEnabled(false);
        btn_p3 = (Button) findViewById(R.id.btn_p3);
        btn_p3.setEnabled(false);
        btn_p4 = (Button) findViewById(R.id.btn_p4);
        btn_p4.setEnabled(false);

        recordingPart();
        filteringPart(); //TODO mirar cuando llamar a esto!
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecordingThread.stopRecording();
    }

    /******* F I L T E R I N G *********/
    private void filteringPart() {
        ArrayList<Double> raw_data = ReadWriteRAW.readDoublesfromRaw(this.audioFileName);

        ArrayList<Double> filter_data = new ArrayList<Double>();
        if(true){ // IIR_Filter - butterworth filter

            // ArrayList<Double> >> double[]
            double[] data = new double[raw_data.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = raw_data.get(i);
            }
            // filter
            IIR_Filter fil = new IIR_Filter(1, 0.25, 0.375); // order 5
            double[] data1 = fil.butter_bandpass_filter(data);

            // double[] >> // ArrayList<Double>
            ArrayList<Double> filter1_data = new ArrayList<Double>();
            for (int i = 0; i < data1.length; i++) {
                filter1_data.add(data1[i]);
            }

            filter_data = filter1_data;

            // play
            short[] samples_fil1 = getSamplesFromList(filter1_data);
            if (samples_fil1!=null){
                btn_p1.setEnabled(true);
                playingPart(samples_fil1, R.id.btn_p1, "1");
            }

        }
        else { // FIR_Filter -
            int size = raw_data.size();
            ArrayList<Double> filter2_data = new ArrayList<>();

            int order = 5;
            FIR_Filter fil = new FIR_Filter(FIR_Filter.FilterType.BPF, order+1, 2, 0.25, 0.375);

            for(int i=0; i<size;i++){
                filter2_data.add(fil.do_sample(raw_data.get(i)));
            }

            filter_data = filter2_data;

            // play
            short[] samples_fil2 = getSamplesFromList(filter2_data);
            if (samples_fil2!=null){
                btn_p2.setEnabled(true);
                playingPart(samples_fil2, R.id.btn_p2, "2");
            }
        }

        Complex[][] fft_data = Transform.fft(filter_data);
        ArrayList<Double> res1 = Transform.fft_inv(fft_data);
        // play
        short[] samples_3 = getSamplesFromList(res1);
        if (samples_3!=null){
            btn_p3.setEnabled(true);
            playingPart(samples_3, R.id.btn_p3, "3");
        }

        Complex[][] filt_fft_data = Transform.substractNoise(fft_data);
        ArrayList<Double> res2 = Transform.fft_inv(filt_fft_data);
        // play
        short[] samples_4 = getSamplesFromList(res2);
        if (samples_4!=null){
            btn_p4.setEnabled(true);
            playingPart(samples_4, R.id.btn_p4, "4");
        }


    }



    /***** R E C O R D I N G *****/

    public void recordingPart(){
        btn_rec = (Button) findViewById(R.id._btn_rec);
        mRealtimeWaveformView = (WaveformView) findViewById(R.id._waveformView);


        mRecordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(short[] data) {
                mRealtimeWaveformView.setSamples(data);
            }
        },true, getApplication());

        audioFileName = mRecordingThread.AUDIO_RECORDER_TEMP_FILE;//new
        FileOutputStream fos;
        try { // create
            fos = openFileOutput(audioFileName, Context.MODE_PRIVATE);
            String path = getDir(audioFileName, Context.MODE_PRIVATE).toString();
            Log.d(LOG_TAG, "create - path: "+path);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRecordingThread.recording()) {
                    btn_rec.setText("STOP");
                    //setText("Stop recording");
                    startAudioRecordingSafe();
                    Log.d(LOG_TAG, "Record in "+mRecordingThread.AUDIO_RECORDER_TEMP_FILE);// internal storage
                } else {
                    btn_rec.setText("REC");
                    mRecordingThread.stopRecording();
                }
            }
        });
    }

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.startRecording();
        } else {
            requestMicrophonePermission();
        }
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
            // Show dialog explaining why we need record audio
            Snackbar.make(mRealtimeWaveformView, "Microphone access is required in order to record audio",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MyAppActivity.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(MyAppActivity.this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.stopRecording();
        }
    }


    /******** P L A Y I N G *******/

    public void playingPart(short[] samples, int btn_id, final String btn_name){
        // ** PLAY **

        if (samples != null) {
            final Button btn = (Button) findViewById(btn_id);

            mPlaybackThread = new PlaybackThread(samples);//TODO fix this! se sobreescribe!

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mPlaybackThread.playing()) {
                        Log.d(LOG_TAG, "pressed PLAY");
                        btn.setText("-");
                        mPlaybackThread.startPlayback();
                    } else {
                        mPlaybackThread.stopPlayback();
                        btn.setText(btn_name);
                    }
                }
            });
        }
    }

    private short[] getSamplesFromFile(String audioFileName){
        short[] samples = null;
        try {
            // GET DATA FROM FILE
            //InputStream is = getResources().openRawResource(R.raw.jinglebells);
            FileInputStream fis = openFileInput(audioFileName);
            String path = getDir(audioFileName, Context.MODE_PRIVATE).toString();
            Log.d(LOG_TAG, "Get sample - path: "+path);
            byte[] data;
            try {
                data = IOUtils.toByteArray(fis);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            // from bytes to shorts ( byte[] data; )
            ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            samples = new short[sb.limit()];
            sb.get(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return samples;
    }

    private short[] getSamplesFromList(ArrayList<Double> dlist) {

        short[] samples = null;

        // GET DATA from arraylist of doubles
        try {
            samples = Aux.doubleArrayL2shortArray(dlist);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return samples;
    }
}
