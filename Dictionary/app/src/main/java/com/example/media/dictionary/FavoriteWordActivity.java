package com.example.media.dictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;



public class FavoriteWordActivity extends AppCompatActivity {
    LinearLayout linearLayoutFavorieWord;
    DictionaryDatabase dictionaryDatabase;
    int NotificationID=0;
    boolean isSorted=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_word);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        linearLayoutFavorieWord = (LinearLayout) findViewById(R.id.linearLayoutFavoriteWord);

        dictionaryDatabase = new DictionaryDatabase(getApplicationContext());
        final FavoriteWordDatabase favoriteWordDatabase = new FavoriteWordDatabase(getApplicationContext());
        final ArrayList<String> favoriteWord = favoriteWordDatabase.getFavoriteWords();
        
        final ArrayList<String> id = new ArrayList<String>();
        for (String str : favoriteWord) {
            final Button btn = new Button(this, null, R.style.PrimaryFlatButton);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
            params.setMargins(20, 10, 0, 0);
            btn.setLayoutParams(params);
            btn.setText(str);
            btn.setAllCaps(false);
            btn.setId(View.generateViewId());
            id.add(Integer.toString(btn.getId()));
            btn.setTextColor(getResources().getColor(R.color.colorPrimary));
            btn.setGravity(Gravity.LEFT);
            btn.setTextSize(20);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendNotification(view, btn.getText().toString());
                }
            });
            linearLayoutFavorieWord.addView(btn);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sapxep);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(linearLayoutFavorieWord);
                Collections.shuffle(id);
                linearLayoutFavorieWord.removeAllViews();
                if (!isSorted){
                    Collections.sort(favoriteWord);
                    isSorted=true;
                }
                else
                {
                    Collections.reverse(favoriteWord);
                }

                for (String str : favoriteWord) {
                    final Button btn = new Button(getApplicationContext(), null, R.style.PrimaryFlatButton);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
                    params.setMargins(20, 10, 0, 0);
                    btn.setLayoutParams(params);
                    btn.setText(str);
                    btn.setAllCaps(false);
                    btn.setId(View.generateViewId());
                    btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn.setGravity(Gravity.LEFT);
                    btn.setTextSize(20);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendNotification(view, btn.getText().toString());
                        }
                    });
                    linearLayoutFavorieWord.addView(btn);

                }
            }
        });
    }

    public void sendNotification(View view, String word) {
        Cursor c = dictionaryDatabase.getWordMatches(word, null);
        String definition="";
        if (c != null){
            definition = c.getString(1);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(word)
                        .setContentText(definition);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Translate.class);
        resultIntent.putExtra("searchWord", word.toString());
        resultIntent.putExtra("definition", definition);
        resultIntent.putExtra("parent", "FavoriteWord");
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Translate.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NotificationID, mBuilder.build());
        NotificationID++;
    }

    void startTranslate(String itemValue){
        Cursor c = dictionaryDatabase.getWordMatches(itemValue, null);
        String definition;
        if (c != null){
            definition = c.getString(1);
            Intent translateLayout = new Intent(getApplicationContext(), com.example.media.dictionary.Translate.class);
            translateLayout.putExtra("searchWord", itemValue.toString());
            translateLayout.putExtra("definition", definition);
            startActivity(translateLayout);
        }
    }
}



