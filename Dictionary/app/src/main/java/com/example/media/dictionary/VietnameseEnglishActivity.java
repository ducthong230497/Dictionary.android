package com.example.media.dictionary;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Media on 11/26/2017.
 */

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
                    DEFINITION = DEFINITION.replaceAll("§", "\n");
                    DEFINITION = DEFINITION.replaceAll("=", "-");
                    DEFINITION = DEFINITION.replaceAll("\\+", "=");
                    String str = WORD + "\n" + DEFINITION;
                    txv_V_E.setText(str);
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
