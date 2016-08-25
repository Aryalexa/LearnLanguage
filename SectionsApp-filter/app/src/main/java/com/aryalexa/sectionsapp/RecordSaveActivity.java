package com.aryalexa.sectionsapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class RecordSaveActivity extends Activity {

    private WaveformView mRealtimeWaveformView;
    private RecordingThread mRecordingThread;
    private static final int REQUEST_RECORD_AUDIO = 13;
    private String audioFileName;
    private PlaybackThread mPlaybackThread;
    private static final String LOG_TAG = "RecordSave";

    private Button play_btn = null;
    private Button rec_btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_save);

        rec_btn = (Button) findViewById(R.id.btn_rec_save);
        play_btn = (Button) findViewById(R.id.btn_play_save);
        play_btn.setEnabled(false);

        recordingPart();
        //playingPart();


    }

    public void recordingPart(){
        // ** RECORDING **
        mRealtimeWaveformView = (WaveformView) findViewById(R.id.waveformView);
        mRecordingThread = new RecordingThread(true, getApplication());//save

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


        //final Button rec_btn = (Button) findViewById(R.id.btn_rec_save );

        rec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRecordingThread.recording()) {
                    // star record!
                    rec_btn.setText("STOP");
                    play_btn.setEnabled(false);
                    startAudioRecordingSafe();
                } else {
                    rec_btn.setText("REC");
                    mRecordingThread.stopRecording();
                    play_btn.setEnabled(true);
                    Log.d(LOG_TAG, "Now you can PLAY");
                    playingPart();
                }
            }
        });
    }

    public void playingPart(){
        // ** PLAY **

        short[] samples = null;
        try {
            samples = getAudioSample();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (samples != null) {
            //final Button play_btn = (Button) findViewById(R.id.btn_play_save);

            mPlaybackThread = new PlaybackThread(samples);

            play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPlaybackThread.playing()) {
                        Log.d(LOG_TAG, "pressed PLAY");
                        play_btn.setText("STOP");
                        mPlaybackThread.startPlayback();
                    } else {
                        mPlaybackThread.stopPlayback();
                        play_btn.setText("PLAY");
                    }
                }
            });
        }
    }

    private short[] getAudioSample() throws IOException{
        //InputStream is = getResources().openRawResource(R.raw.jinglebells);
        FileInputStream fis = openFileInput(this.audioFileName);
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
        short[] samples = new short[sb.limit()];
        sb.get(samples);
        return samples;
    }

    @Override
    protected void onStop() {
        super.onStop();

        mRecordingThread.stopRecording();
        mPlaybackThread.stopPlayback();
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
                    ActivityCompat.requestPermissions(RecordSaveActivity.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(RecordSaveActivity.this, new String[]{
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

}
