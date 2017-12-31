package com.example.media.dictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by Media on 12/29/2017.
 */

public class FavoriteWordActivity extends Activity {
    LinearLayout linearLayoutFavorieWord;
    DictionaryDatabase dictionaryDatabase;
    int NotificationID=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_word);
        linearLayoutFavorieWord = findViewById(R.id.linearLayoutFavoriteWord);

        dictionaryDatabase = new DictionaryDatabase(getApplicationContext());
        final FavoriteWordDatabase favoriteWordDatabase = new FavoriteWordDatabase(getApplicationContext());
        final ArrayList<String> favoriteWord = favoriteWordDatabase.getFavoriteWords();
        

        for (String str : favoriteWord) {
            final Button btn = new Button(this);
            btn.setLayoutParams(new SlidingPaneLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            btn.setText(str);
            btn.setAllCaps(false);
            btn.setId(View.generateViewId());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            linearLayoutFavorieWord.addView(btn);
        }

        Button b = new Button(this);
        b.setLayoutParams(new SlidingPaneLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        b.setText("asd");
        b.setAllCaps(false);
        b.setId(View.generateViewId());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification(view, "relevant");
            }
        });
        linearLayoutFavorieWord.addView(b);

    }
    public void sendNotification(View view, String word) {
        Cursor c = dictionaryDatabase.getWordMatches(word, null);
        String definition="";
        if (c != null){
            definition = c.getString(1);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_open)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
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



