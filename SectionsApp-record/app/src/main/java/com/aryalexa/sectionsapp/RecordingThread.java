/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.aryalexa.sectionsapp;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class RecordingThread {
    private static final String LOG_TAG = RecordingThread.class.getSimpleName();
    private static final int SAMPLE_RATE = 44100;
    private boolean mShouldContinue;
    private Thread mThread;

    boolean WAVEFORM;
    private AudioDataReceivedListener mListener;

    private boolean SAVE = false;
    public static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    public static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";//2,3
    //private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    //public static final String AUDIO_TEMP_FILE = "record_temp2.raw";//3
    Context context = null;

    // BUILDERS
    public RecordingThread(AudioDataReceivedListener listener) {
        mListener = listener;
        WAVEFORM = true;
    }
    public RecordingThread(AudioDataReceivedListener listener, boolean save, Context ctx) {
        mListener = listener;
        WAVEFORM = true;
        SAVE = save;
        context = ctx;
    }
    public RecordingThread(boolean save, Context ctx) {
        WAVEFORM = false;
        SAVE = save;
        context = ctx;
    }

    // METHODS
    public boolean recording() {
        return mThread != null;
    }

    public void startRecording() {
        if (mThread != null)
            return;

        mShouldContinue = true;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                record();
            }
        });
        mThread.start();
    }


    public void stopRecording() {
        if (mThread == null){
            Log.d(LOG_TAG, "recording null!!");
            return;
        }

        mShouldContinue = false;
        mThread = null;
    }

    private void record() {

        Log.d(LOG_TAG, "Start");
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        // buffer size in bytes
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2;
        }



        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!");
            return;
        }

        ////////////////////////////////////////////////////////////////////
        // NEW - write

        //OutputStream out = new ByteArrayOutputStream(); //1
        //String filename = "je";//getTempFilename(); //2
        //FileOutputStream os = null;
        String audioFileName = AUDIO_RECORDER_TEMP_FILE;//3
        FileOutputStream fos = null;

        if(SAVE) {
            try {

                //os = new FileOutputStream(filename);//2
                fos = context.openFileOutput(audioFileName, Context.MODE_PRIVATE);//3
                String path = context.getDir(audioFileName, Context.MODE_PRIVATE).toString();
                Log.d(LOG_TAG, "recording - path: "+path);


            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG, "FileOutputStream doesn't work!");
                e.printStackTrace();
            }
        }
        //////////////////////////////////////////////////////////////////

        //short[] audioBuffer = new short[bufferSize / 2];
        byte[] audioBufferOfBytes = new byte[bufferSize];

        record.startRecording();
        Log.d(LOG_TAG, "Start recording");

        long shortsRead = 0;
        try {
            while (mShouldContinue) {
                int numberOfBytes = record.read(audioBufferOfBytes, 0, bufferSize);
                shortsRead += numberOfBytes;

                // from bytes to shorts ( byte[] audioBufferOfBytes; )
                ShortBuffer sb = ByteBuffer.wrap(audioBufferOfBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
                short[] audioBuffer = new short[sb.limit()];
                sb.get(audioBuffer);
                // notify
                if (WAVEFORM)// Notify waveform
                    mListener.onAudioDataReceived(audioBuffer);

                if (SAVE && null != fos && AudioRecord.ERROR_INVALID_OPERATION != numberOfBytes){// save data //NEW
                    fos.write(audioBufferOfBytes);//3
                }
                /*
                // read
                int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);
                shortsRead += numberOfShort;

                // notify
                if (WAVEFORM)// Notify waveform
                    mListener.onAudioDataReceived(audioBuffer);

                // save
                if (SAVE && null != fos && AudioRecord.ERROR_INVALID_OPERATION != numberOfShort){// save data //NEW
                    audioBufferOfBytes = castShort2Byte(audioBuffer);
                    //out.write(audioBufferOfBytes, 0, numberOfShort*2);//1
                    //os.write(audioBufferOfBytes);//2
                    fos.write(audioBufferOfBytes);//3
                }
                */
            }
            if (SAVE){
                //out.close();//1
                //os.close();//2
                Log.v(LOG_TAG, "SAMPLE: "+fos.toString());
                fos.close();//3
            }

        } catch (IOException e) {
            System.err.println("I/O problems: " + e);
            e.printStackTrace();

            Log.e(LOG_TAG, "Error saving");
        }

        record.stop();
        record.release();

        Log.d(LOG_TAG, "Recorded: "+shortsRead*2+" bytes");
        // PROCESS
        if (SAVE){
            //copyWaveFile(mRecordingThread.AUDIO_RECORDER_TEMP_FILE, getFilename());
            //deleteTempFile();
            ////add the file to the media content provider
            //MediaScannerConnection.scanFile(this, new String[]{getFilename()}, null, null);
            ///
        }

        //TODO hacer todo el proceso ahora (limpiar comparar, etc)
        //Log.v(LOG_TAG, String.format("Recording stopped. Samples read: %d", shortsRead));
    }

    byte[] castShort2Byte(short[] buffer){
        byte[] byteBuffer = new byte[buffer.length * 2];

        //byte[] converted = new byte[values.length * 2];
        ByteBuffer buff = ByteBuffer.wrap(byteBuffer);
        for (short in : buffer) {
            buff.putShort(in);
        }
        /*
        for (int i = 0; i<buffer.length; i++) {
            byteBuffer[2*i] = (byte)(buffer[i] & 0xff);
            byteBuffer[2*i+1] = (byte)((buffer[i] >> 8) & 0xff);
        }*/
        return byteBuffer;
    }



    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);

        if (tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }
}
