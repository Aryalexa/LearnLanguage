package com.aryalexa.sectionsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private String LOG_TAG = "PlayBackActivity";
    private boolean playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        playingPart(R.id.btn_play, R.raw.jinglebells_short);
        playingPart(R.id.btn_play_btt, R.raw.jbs_butter);
    }


    public void playingPart(int id_btn, int id_raw){
        // ** PLAY **

        //final WaveformView mPlaybackView = (WaveformView) findViewById(R.id.playbackWaveformView); //no view

        final short[] samples = getAudioSample(id_raw);


        if (samples != null) {
            //final FloatingActionButton playFab = (FloatingActionButton) findViewById(R.id.playFab);
            final Button play_btn = (Button) findViewById(id_btn);


            //mPlaybackThread = new PlaybackThread(samples, new PlaybackListener() {
              //  @Override
              //  public void onProgress(int progress) {
                    //mPlaybackView.setMarkerPosition(progress);
                //}
                //@Override
               // public void onCompletion() {
                    //mPlaybackView.setMarkerPosition(mPlaybackView.getAudioLength());
                    //playFab.setImageResource(android.R.drawable.ic_media_play);
               // }
            //});
            //mPlaybackView.setChannels(1);
            //mPlaybackView.setSampleRate(PlaybackThread.SAMPLE_RATE);
            //mPlaybackView.setSamples(samples);

            assert play_btn != null;

            play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Entra muchas veces :33!");
                    mPlaybackThread = new PlaybackThread(samples);
                    if ( !playing ) {
                        playing = false;
                        mPlaybackThread.startPlayback();
                        play_btn.setText("STOP");
                        //playFab.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        playing = true;
                        mPlaybackThread.stopPlayback();
                        play_btn.setText("PLAY");
                        //playFab.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
            });
        }
    }


    private short[] getAudioSample(int raw_id) {
        //TODO pasarle una clave para coger el audio especificado cada vez (DONE)
        InputStream is = getResources().openRawResource(raw_id);
        byte[] data = null;

        try {
            data = IOUtils.toByteArray(is);
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
