package com.quintodg.mayr.japapp;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.io.File;


public class AudioActivity extends AppCompatActivity {


    private Button btnRecord, btnPback, btnPlay;
    private TextView textRecord;
    private MediaRecorder recorder;
    private String FILE;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // PLAYING
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPlay.getText().toString().equals("Play")) {
                    btnRecord.setEnabled(false);
                    btnPback.setEnabled(false);
                    if (player != null) {
                        player.stop();
                        player.release();
                    }
                    player = MediaPlayer.create(AudioActivity.this, R.raw.zico);
                    player.start();
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// when sound is done
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            player.release(); // release resources
                            player = null;
                            btnRecord.setEnabled(true);
                            // simular click en STOP
                        }
                    });
                    btnPlay.setText("Stop");
                } else if (btnPlay.getText().toString().equals("Stop")) {
                    stopPlayback();
                    btnRecord.setEnabled(true);
                    btnPlay.setText("Play");
                }

            }
        });


        // RECORD AND PLAYBACK

        FILE = Environment.getExternalStorageDirectory() + "/audioTest.3gpp";
        textRecord = (TextView)findViewById(R.id.textRecord);
        btnRecord = (Button)findViewById(R.id.btnRecord);
        btnPback = (Button)findViewById(R.id.btnPback);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnRecord.getText().toString().equals("Record"))
                {
                    btnPlay.setEnabled(false);
                    btnPback.setEnabled(false);
                    try {
                        startRecord();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textRecord.setText("Recording...");
                    btnRecord.setText("End");
                } else if (btnRecord.getText().toString().equals("End"))
                {
                    endRecord();
                    textRecord.setText("");
                    btnRecord.setText("Record");
                    btnPback.setEnabled(true);
                    btnPlay.setEnabled(true);
                }
            }
        });

        //clickListener of playback button
        btnPback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPback.getText().toString().equals("Playback"))
                {
                    btnPlay.setEnabled(false);
                    try {
                        startPlayback();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textRecord.setText("Playing...");
                    btnPback.setText("Stop");
                } else if (btnPback.getText().toString().equals("Stop"))
                {
                    stopPlayback();
                    textRecord.setText("");
                    btnPback.setText("Playback");
                    btnPlay.setEnabled(true);
                }
            }
        });


    }

    public void startRecord() throws Exception
    {
        File fileOut = new File(FILE);
        if (fileOut != null)
        {
            fileOut.delete(); // overwrite any existing file
        }

        if (recorder != null) {
            recorder.release();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(FILE);

        recorder.prepare();
        recorder.start();
    }
    public void endRecord()
    {
        if (recorder != null)
        {
            recorder.stop();
            recorder.release();
        }
    }
    public void startPlayback() throws Exception
    {
        if (player != null)
        {
            player.stop();
            player.release();
        }
        player = new MediaPlayer();
        player.setDataSource(FILE);
        player.prepare();
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // when sound ends
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release(); // release resources
                player = null;
            }
        });
    }
    public void stopPlayback()
    {
        if (player != null)
        {
            player.stop();
            player.release();
        }
    }
}
