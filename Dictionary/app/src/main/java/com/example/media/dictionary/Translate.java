package com.example.media.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Lirus on 19.10.2017.
 */

public class Translate extends AppCompatActivity {
    TextToSpeech textToSpeech;
    String word;
    String definition;
    TextView txvWord;
    Button btnAddToFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final FavoriteWordDatabase favoriteWordDatabase = new FavoriteWordDatabase(getApplicationContext());
        final ArrayList<String> favoriteWord = favoriteWordDatabase.getFavoriteWords();
        // đọc từ
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.setPitch(1.5f);
                textToSpeech.setSpeechRate(1.0f);
            }
        });
        word = getIntent().getExtras().getString("searchWord");
        definition = getIntent().getExtras().getString("definition");
        definition = definition.replaceFirst("§", "]§");
        definition = definition.replaceAll("§", "\n");
        definition = definition.replaceAll("=", "-");
        definition = definition.replaceAll("\\+", "=");
        String str = word + " [" + definition;
        txvWord = (TextView) findViewById(R.id.txvWord);
        txvWord.setText(str);

        ImageView imvSpeak = (ImageView) findViewById(R.id.imvSpeak);
        imvSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(word, textToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnAddToFavorite = (Button) findViewById(R.id.btnAddFavoriteWord);
        btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //favoriteWordDatabase.addWord()
                if (/*favoriteWord.size() > 0 && */!favoriteWord.contains(word)){
                    favoriteWord.add(word);
                    long id = favoriteWordDatabase.addWord(word, 1);
                    if (id > 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "added failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "this word has been add to your favorite list", Toast.LENGTH_LONG);
                    toast.show();
                }
                //finish();
            }
        });
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = null;
        String parent = getIntent().getExtras().getString("parent");
        if(parent.equals("FavoriteWord")){

        }
        else {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return null;
    }


}
