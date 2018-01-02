package com.example.media.dictionary;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Media on 1/2/2018.
 */

public class TranslateText extends AppCompatActivity {

    Button btnStartTranslateText;
    EditText edtText;
    TextView txvTranslateText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);

        edtText = (EditText) findViewById(R.id.edtTranslateText);

        btnStartTranslateText = (Button) findViewById(R.id.btnStartTranslateText);
        btnStartTranslateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), edtText.getText().toString(), Toast.LENGTH_LONG).show();
                String input = edtText.getText().toString();
                new Translate().execute(input);
            }
        });
    }


    class Translate extends AsyncTask<String,String,String> {
        String string = "";
        String toLang = "vi"; // ngôn ngữ cần dịch
        String fromLang = "en"; // để trống là sẽ ở chế chộ phát hiện ngôn ngữ
        public Translate(String toLang,String fromLang){
            this.toLang = toLang;
            this.fromLang = fromLang;
        }
        public Translate(){}
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream is = null;
            BufferedReader bufferedReader = null;

            try {
                String translateUrl =
                        "https://translate.google.com/#en/vi/"
                                + URLEncoder.encode(params[0],"UTF-8");
                URL url = new URL(translateUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Something Else");
                is = urlConnection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    string += line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return string;
        }

        @Override
        protected void onPostExecute(String s) {

            txvTranslateText = (TextView) findViewById(R.id.txvTranslateText);
            s = s.replace("[[","\n").replace("]]","\n").replace("],[","\n");

            txvTranslateText.setText(s);
            super.onPostExecute(s);
        }
    }
    public void speech(final String text, final String lang){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlSpeed = "https://translate.google.com/translate_tts?ie=UTF-8&q="
                            +URLEncoder.encode(text,"UTF-8")+"&tl="+lang+"&total=1&idx=0&textlen="
                            +text.length()+"&tk=822010|693481&client=t&prev=input";

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(TranslateText.this, Uri.parse(urlSpeed));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
