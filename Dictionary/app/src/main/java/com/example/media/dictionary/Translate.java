package com.example.media.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    Button btnDeleteFavorite;
    FloatingActionButton btnfabFavorite;
    String[] listDefinition;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.addword);
        getSupportActionBar().setCustomView(imageView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);*/


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
        /*definition = definition.replaceAll("§", "\n");
        definition = definition.replaceAll("=", "-");
        definition = definition.replaceAll("\\+", "=");*/
        String str = word + " [" + definition;
        listDefinition = str.split("§");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layoutTranslateWord);
        for (String i : listDefinition){
            TextView txv = new TextView(this);
            txv.setTextSize(20);
            if (i.charAt(0) == '*'){
                i = i.substring(1);
                txv.setTextColor(Color.BLACK);
                txv.setTypeface(Typeface.DEFAULT_BOLD);
                SpannableString content = new SpannableString(i);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                txv.setText(i);
                linearLayout.addView(txv);
                continue;
            }
            if (i.charAt(0) == '-'){
                txv.setTextColor(Color.rgb(240,98,146));
                txv.setText("\u21AA "+i.substring(1));
                linearLayout.addView(txv);
                continue;
            }
            if (i.charAt(0) == '='){
                String[] x = i.split("\\+");
                txv.setTextColor(Color.rgb(0,176,255));
                txv.setText("     " + x[0].substring(1));
                linearLayout.addView(txv);

                txv = new TextView(this);
                txv.setTextColor(Color.BLACK);
                txv.setTextSize(20);
                txv.setText("     " + x[1]);
                linearLayout.addView(txv);
                continue;
            }
            //String[] x = i.substring(word.length());
            txv.setTextColor(Color.BLACK);
            txv.setTextSize(25);
            txv.setTypeface(Typeface.DEFAULT_BOLD);
            txv.setText("   "+i.substring(0,word.length()));
            linearLayout.addView(txv);
            txv = new TextView(this);
            txv.setTextColor(Color.rgb(69,90,100));
            txv.setTextSize(20);
            txv.setText("   " + i.substring(word.length()));
            linearLayout.addView(txv );
        }
        //txvWord = (TextView) findViewById(R.id.txvWord);
        //txvWord.setText(str);

        ImageView imvSpeak = (ImageView) findViewById(R.id.imvSpeak);
        imvSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(word, textToSpeech.QUEUE_FLUSH, null);
            }
        });

        /*btnAddToFavorite = (Button) findViewById(R.id.btnAddFavoriteWord);
        btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //favoriteWordDatabase.addWord()
                if (*//*favoriteWord.size() > 0 && *//*!favoriteWord.contains(word)){
                    favoriteWord.add(word);
                    long id = favoriteWordDatabase.addWord(word);
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

        btnDeleteFavorite = (Button) findViewById(R.id.btnDeleteFavoriteWord);
        btnDeleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (*//*favoriteWord.size() > 0 && *//*favoriteWord.contains(word)){
                    favoriteWord.remove(word);
                    long id = favoriteWordDatabase.deleteWord(word);
                    if (id > 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "successfully deleted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "delete failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "this word doesn't belong to your favorite list", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });*/

        btnfabFavorite = (FloatingActionButton) findViewById(R.id.btnfabAddWord);
        btnfabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!favoriteWord.contains(word)){
                    favoriteWord.add(word);
                    long id = favoriteWordDatabase.addWord(word);
                    if (id > 0){
                        btnfabFavorite.setImageResource(R.drawable.eraseword);
                        Toast toast = Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "added failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else if (favoriteWord.contains(word)){
                    favoriteWord.remove(word);
                    long id = favoriteWordDatabase.deleteWord(word);
                    if (id > 0){
                        btnfabFavorite.setImageResource(R.drawable.addword);
                        Toast toast = Toast.makeText(getApplicationContext(), "successfully deleted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "delete failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "this word doesn't belong to your favorite list", Toast.LENGTH_LONG);
                    toast.show();
                }
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
