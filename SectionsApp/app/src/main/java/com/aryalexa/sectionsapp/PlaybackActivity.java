package com.aryalexa.sectionsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class PlaybackActivity extends AppCompatActivity {

    private PlaybackThread mPlaybackThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        playingPart();
    }


    public void playingPart(){
        // ** PLAY **

        //final WaveformView mPlaybackView = (WaveformView) findViewById(R.id.playbackWaveformView); //no view

        short[] samples = null;
        try {
            samples = getAudioSample();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (samples != null) {
            //final FloatingActionButton playFab = (FloatingActionButton) findViewById(R.id.playFab);
            final Button play_btn = (Button) findViewById(R.id.btn_play);

            mPlaybackThread = new PlaybackThread(samples, new PlaybackListener() {
                @Override
                public void onProgress(int progress) {
                    //mPlaybackView.setMarkerPosition(progress);
                }
                @Override
                public void onCompletion() {
                    //mPlaybackView.setMarkerPosition(mPlaybackView.getAudioLength());
                    //playFab.setImageResource(android.R.drawable.ic_media_play);
                }
            });
            //mPlaybackView.setChannels(1);
            //mPlaybackView.setSampleRate(PlaybackThread.SAMPLE_RATE);
            //mPlaybackView.setSamples(samples);

            play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPlaybackThread.playing()) {
                        mPlaybackThread.startPlayback();
                        play_btn.setText("STOP");
                        //playFab.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        mPlaybackThread.stopPlayback();
                        play_btn.setText("PLAY");
                        //playFab.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
            });
        }
    }


    private short[] getAudioSample() throws IOException{
        //TODO pasarle una clave para coger el audio especificado cada vez
        InputStream is = getResources().openRawResource(R.raw.jinglebells2);
        byte[] data;
        try {
            data = IOUtils.toByteArray(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }

        ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] samples = new short[sb.limit()];
        sb.get(samples);
        return samples;
    }

    protected void onStop() {
        super.onStop();

        mPlaybackThread.stopPlayback();
    }
}
