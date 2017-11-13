package com.example.media.dictionary;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by Lirus on 19.10.2017.
 */

public class Translate extends Activity {
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_layout);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.setPitch(3f);
                textToSpeech.setSpeechRate(1.0f);
                if (textToSpeech.getLanguage() == Locale.US){

                }
                /*else if (textToSpeech.getLanguage() == Locale.GERMAN){
                    textToSpeech.setPitch(1.3f);
                    textToSpeech.setSpeechRate(0.9f);
                }*/
            }
        });
        final String word = getIntent().getExtras().getString("searchWord");
        Button btnSpeak = (Button) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(word, textToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}
