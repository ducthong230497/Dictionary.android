package com.example.media.dictionary;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VietnameseEnglishActivity extends AppCompatActivity {
    ArrayList<String> arrayList = new ArrayList<String>();
    ListViewAdapter adapter;
    ListView lsv_V_E_Words;
    String WORD;
    String DEFINITION;
    TextView txv_V_E;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vietnamese_english_dic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final VietnameseEnglishDatabase vietnameseEnglishDatabase = new VietnameseEnglishDatabase(this);
        arrayList = vietnameseEnglishDatabase.getAllWords();

        adapter = new ListViewAdapter(getApplicationContext(),arrayList);

        txv_V_E = (TextView) findViewById(R.id.txv_V_E_Word);
        frameLayout = (FrameLayout) findViewById(R.id.frame_V_E_Container);

        lsv_V_E_Words = (ListView) findViewById(R.id.listView_V_E_Search);
        lsv_V_E_Words.setAdapter(adapter);
        lsv_V_E_Words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WORD = lsv_V_E_Words.getItemAtPosition(i).toString();
                Cursor c = vietnameseEnglishDatabase.getWordMatches(WORD, null);
                if (c != null){
                    DEFINITION = c.getString(1);
                    /*DEFINITION = DEFINITION.replaceAll("§", "\n");
                    DEFINITION = DEFINITION.replaceAll("=", "-");
                    DEFINITION = DEFINITION.replaceAll("\\+", "=");*/
                    String word = WORD;
                    String definition = DEFINITION;
                    String str = WORD + "§" + DEFINITION;
                    String[] listDefinition = str.split("§");

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layoutVieEng);

                    for (String it : listDefinition){
                        TextView txv = new TextView(getApplicationContext());
                        txv.setTextSize(20);
                        if (it.charAt(0) == '*'){
                            it = it.substring(1);
                            txv.setTextColor(Color.BLACK);
                            txv.setTypeface(Typeface.DEFAULT_BOLD);
                            SpannableString content = new SpannableString(it);
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            txv.setText(it);
                            linearLayout.addView(txv);
                            continue;
                        }
                        if (it.charAt(0) == '-'){
                            txv.setTextColor(Color.rgb(240,98,146));
                            txv.setText("\u21AA "+it.substring(1));
                            linearLayout.addView(txv);
                            continue;
                        }
                        if (it.charAt(0) == '='){
                            String[] x = it.split("\\+");
                            txv.setTextColor(Color.rgb(0,176,255));
                            txv.setText("     " + x[0].substring(1));
                            linearLayout.addView(txv);

                            txv = new TextView(getApplicationContext());
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
                        txv.setText(it);
                        linearLayout.addView(txv);
                    }

                    //txv_V_E.setText(str);
                    frameLayout.setVisibility(View.VISIBLE);
                    lsv_V_E_Words.setVisibility(View.INVISIBLE);
                }
            }
        });

        SearchView searchView_V_E = (SearchView) findViewById(R.id.editText_V_E_Search);
        searchView_V_E.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0){
                    lsv_V_E_Words.setVisibility(View.INVISIBLE);
                }
                else{
                    adapter.filter(s);
                    lsv_V_E_Words.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }
}
