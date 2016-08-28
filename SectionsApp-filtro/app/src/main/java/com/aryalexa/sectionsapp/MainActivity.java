package com.aryalexa.sectionsapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends /*AppCompat*/Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void recordVisualizer(View view){
        Intent intent = new Intent(this, RecordWaveformActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void recordAndSave(View view){
        Intent intent = new Intent(this, RecordSaveActivity.class);

        startActivity(intent);
    }

    public void playAudio(View view){
        Intent intent = new Intent(this, PlaybackActivity.class);

        startActivity(intent);
    }

}
