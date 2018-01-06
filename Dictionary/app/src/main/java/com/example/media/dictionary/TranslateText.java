package com.example.media.dictionary;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                String result="";
                try{
                   result = Translator.translate("vi",input);
                }
                catch (Exception e){

                }
                if (!result.equals(""))
                {
                    txvTranslateText = (TextView) findViewById(R.id.txvTranslateText);
                    txvTranslateText.setText(result);
                }
                //new Translate().execute(input);
            }
        });
    }
    private boolean CheckConnection() {
// Bộ quản lí kết nối
        ConnectivityManager connManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
// Thông tin mạng
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(this, "Thông tin mạng không tồn tại",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Không kết nối được mạng", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Mạng không được kích hoạt",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Mạng đang hoạt động", Toast.LENGTH_LONG).show();
        return true;
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
        public InputStream OpenHttpConnection(String urlString) throws IOException
        {
            InputStream in = null;
            try{
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Something Else");
                in = urlConnection.getInputStream();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return in;
        }
        @Override
        protected String doInBackground(String... params) {

            InputStream is = null;
            BufferedReader bufferedReader = null;


            /*Document document = null;*/
           /* try {
                String translateUrl =
                        "https://translate.google.com/#en/vi/"
                                + params[0]*//*URLEncoder.encode(params[0],"UTF-8")*//*;
                Document document = Jsoup.connect(translateUrl).get();

                Elements subjectElements = document.select("span#result_box");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            String resultText="";
            try {
                String translateUrl =
                        "https://translate.google.com/#en/vi/"
                                + URLEncoder.encode(params[0],"UTF-8");
                URL url = new URL(translateUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Something Else");
                is = OpenHttpConnection(translateUrl);

                bufferedReader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    string += line;
                }
               /* Document doc=Jsoup.parse(string);
               Element subjectElements = doc.getElementById("result_box");

                   resultText=subjectElements.text();*/

                is.close();
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