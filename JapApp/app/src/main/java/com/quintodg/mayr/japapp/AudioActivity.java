package com.quintodg.mayr.japapp;


import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.android.AndroidAudioInputStream;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.writer.*;



public class AudioActivity extends AppCompatActivity {



    private TextView textPlay;
    private Button btnRecord, btnStop, btnPlay;
    private boolean recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnRecord = (Button)findViewById(R.id.btnRec);
        btnStop = (Button)findViewById(R.id.btnStopRec);
        btnPlay = (Button)findViewById(R.id.btnPlayRec);

        btnRecord.setOnClickListener(startRecOnClickListener);
        btnStop.setOnClickListener(stopRecOnClickListener);
        btnPlay.setOnClickListener(playBackOnClickListener);


        btnStop.setEnabled(false);

        //////////////
        /*
        textPlay = (TextView)findViewById(R.id.textPlay);

        try {
            processAudio2();
        } catch (Exception e) {
            e.printStackTrace(); //random access file
            textPlay.setText("MAL, exception");

        }
        textPlay.setText("don't mention it");
        */ ////////////////

    }

    View.OnClickListener startRecOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Thread recordThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    recording = true;
                    startRecord();
                }
            });
            recordThread.start();
            btnRecord.setEnabled(false);
            btnStop.setEnabled(true);

        }
    };

    private void startRecord(){
        File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

        int sampleRate = 44100;

        final String promptStartRecord =
                "startRecord()\n"
                        + file.getAbsolutePath();

        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                Toast.makeText(AudioActivity.this,
                        promptStartRecord,
                        Toast.LENGTH_LONG).show();
            }
        });

        try {
            file.createNewFile();

            // outputStream > buffersOutputStream > dataOutputStream
            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            // buffer size, data
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];

            // Recorder
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording(); // record 1

            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize); // record 2
                for(int i = 0; i < numberOfShort; i++){
                    dataOutputStream.writeShort(audioData[i]);
                }
            }

            audioRecord.stop(); // record 3
            audioRecord.release(); // record 4
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    View.OnClickListener stopRecOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recording = false;
            btnRecord.setEnabled(true);
            btnStop.setEnabled(false);
        }
    };

    View.OnClickListener playBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playRecord();
        }
    };

    private void playRecord(){
        File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

        int shortSizeInBytes = Short.SIZE/Byte.SIZE;

        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];

        try {
            // inputStream > bufferedInputStream > dataInputStream
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while(dataInputStream.available() > 0){
                audioData[i] = dataInputStream.readShort();
                i++;
            }

            dataInputStream.close();

            int sampleFreq = 44100;

            final String promptPlayRecord =
                    "PlayRecord()\n"
                            + file.getAbsolutePath();

            Toast.makeText(AudioActivity.this,
                    promptPlayRecord,
                    Toast.LENGTH_LONG).show();

            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();
            audioTrack.write(audioData, 0, bufferSizeInBytes);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(AudioActivity.this,
                    "error: file not found",
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AudioActivity.this,
                    "error: ioException",
                    Toast.LENGTH_LONG).show();
        }
    }





    public void processAudio() throws Exception {

       /********************************/


        final int sampleRate = 44100;
        final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        final int androidFormat = AudioFormat.ENCODING_PCM_16BIT;
        final int bufferOverlap = 0;

        int minAudioBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, androidFormat);


        float freq = 2150;
        float bandWidth = 3700;
        BandPass bandPass = new BandPass(freq, bandWidth, 44100);
        // float freq, float bandWidth, float sampleRate

        int bufferSize = minAudioBufferSize * 100;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        float[] f = new float[bufferSize];


        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
               AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        record.startRecording();
        record.read(buffer, bufferSize);
        buffer.asFloatBuffer().get(f);
        record.stop();
        record.release();

        TarsosDSPAudioFormat tarsosFormat = new TarsosDSPAudioFormat(44100, 16, 1, true, false);


        AudioEvent audioEvent = new AudioEvent(tarsosFormat);
        audioEvent.setFloatBuffer(f);

        bandPass.process(audioEvent);
        byte[] filteredBuffer = audioEvent.getByteBuffer();

        WriterProcessor wr = new WriterProcessor(tarsosFormat, new RandomAccessFile("output.wav", "rw"));

    }

    public void processAudio2() throws Exception{

        float freq = 2150;
        float bandWidth = 3700;
        BandPass bandPass = new BandPass(freq, bandWidth, 44100);

        TarsosDSPAudioFormat tarsosFormat = new TarsosDSPAudioFormat(44100, 16, 1, true, false);
        WriterProcessor wr = new WriterProcessor(tarsosFormat, new RandomAccessFile("output.wav", "rw"));

        /***/

        AudioDispatcher d = AudioDispatcherFactory.fromPipe("audioTest.3gpp", 44100, 3, 0);
        //final String source,final int targetSampleRate, final int audioBufferSize,final int bufferOverlap

        d.addAudioProcessor(bandPass);
        d.addAudioProcessor(wr);

    }

}

