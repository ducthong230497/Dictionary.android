package com.example.media.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.akexorcist.localizationactivity.LocalizationActivity;

import java.util.ArrayList;

public class MainActivity extends LocalizationActivity implements SearchView.OnQueryTextListener{

    private final String TAG = this.getClass().getSimpleName();
    final int LANGUAGE_SETTING_RESULT = 1;
    final int OCR_RESULT = 2;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    public static DictionaryDatabase dictionaryDatabase;
    SearchView SearchWord;
    ImageView imvSetting;
    ListView lsvWord;
    ListViewAdapter adapter;
    ArrayList<String> arraylist = new ArrayList<String>();
    String definition;
    final int a = 1;
    Button btnEngVieDict;
    Button btnFloatingWidget;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        dictionaryDatabase = new DictionaryDatabase(this);
        arraylist = dictionaryDatabase.getAllWords();
        lsvWord = (ListView) findViewById((R.id.listViewSearch));
        //neu ko co final thi phai khai tao listview ngoai ham onCreate
        /*String[] values = new String[] { "AndroidListView",
                "Adapterimplementation",
                "SimpleistViewInAndroid",
                "Create ListViewndroid",
                "AndroidExample",
                "ListViewSourc Code",
                "ListViewArrayAdapter",
                "AndroidExampleListView"
        };

        ArrayAdapter<String> strAdapter = new ArrayAdapter<String>( this,
                android.R.layout.simple_list_item_1,
                values);

        for (int i = 0; i < values.length; i++) {
            arraylist.add(values[i]);
        }*/

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist, DictionaryDatabase.listWord);

        lsvWord.setAdapter(adapter);

        lsvWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  itemValue    = (String) lsvWord.getItemAtPosition(i);
                //Toast.makeText(getApplicationContext(),"Position :"+i+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                //        .show();
                //Log.d(TAG, "go here");
                startTranslate(itemValue);
            }
        });



        SearchWord = (SearchView) findViewById(R.id.editTextSearch);
        SearchWord.setOnQueryTextListener(this);


        Button btnLanguages = (Button) findViewById(R.id.btnLanguages);
        btnLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageSettingInten = new Intent(getApplicationContext(), LanguagesSetting.class);
                startActivityForResult(languageSettingInten, LANGUAGE_SETTING_RESULT);
            }
        });


        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageSettingInten = new Intent(getApplicationContext(), OCR.class);
                //Intent languageSettingInten = new Intent(getApplicationContext(), camera2.class);
                startActivityForResult(languageSettingInten, OCR_RESULT);
            }
        });

        btnEngVieDict = (Button) findViewById(R.id.btnEngVieDict);
        btnEngVieDict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EngVieIntent = new Intent(getApplicationContext(), VietnameseEnglishActivity.class);
                startActivity(EngVieIntent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        findViewById(R.id.btnFloatingWidget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            }
        });
    }

    void startTranslate(String itemValue){
        Cursor c = dictionaryDatabase.getWordMatches(itemValue, null);
        if (c != null){
            definition = c.getString(1);
        }
        Intent translateLayout = new Intent(getApplicationContext(), com.example.media.dictionary.Translate.class);
        translateLayout.putExtra("searchWord", itemValue.toString());
        translateLayout.putExtra("definition", definition);
        startActivity(translateLayout);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0){
            lsvWord.setVisibility(View.INVISIBLE);
        }
        else{
            String text = newText;
            if (text.charAt(0) != ' '){
                adapter.filter(text);
                lsvWord.setVisibility(View.VISIBLE);
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LANGUAGE_SETTING_RESULT && resultCode == RESULT_OK){
            Intent intent = new Intent();
            String LanguageCode = data.getStringExtra("LanguageCode");
            setLanguage(LanguageCode);
            Log.d("Setting", LanguageCode);
            intent.putExtra("LanguageCode", LanguageCode);
            setResult(RESULT_OK, intent);
            //finish();
        }
        else if(requestCode == OCR_RESULT && resultCode == RESULT_OK){

        }
    }
}
