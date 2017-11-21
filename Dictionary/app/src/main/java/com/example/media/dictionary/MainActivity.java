package com.example.media.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;

public class MainActivity extends LocalizationActivity {

    private final String TAG = this.getClass().getSimpleName();
    final int LANGUAGE_SETTING_RESULT = 1;
    final int OCR_RESULT = 2;

    EditText edtTextSearch;
    ImageView imvSetting;
    final int a = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");


        final ListView lsv = (ListView) findViewById((R.id.listViewSearch));
        //neu ko co final thi phai khai tao listview ngoai ham onCreate
        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        ArrayAdapter<String> strAdapter = new ArrayAdapter<String>( this,
                android.R.layout.simple_list_item_1,
                values);

        lsv.setAdapter(strAdapter);

        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  itemValue    = (String) lsv.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(),"Position :"+i+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
                Log.d(TAG, "go here");
                startTranslate();
            }
        });



        edtTextSearch = (EditText) findViewById(R.id.editTextSearch);
        edtTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int editTextLength = edtTextSearch.getText().length();
                if(editTextLength != 0 && lsv.getVisibility() == View.INVISIBLE){
                    lsv.setVisibility(View.VISIBLE);
                }
                else if(editTextLength == 0){
                    Log.d(TAG, "it goes here!!!!!");
                    lsv.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
    }
    void startTranslate(){
        Intent translateLayout = new Intent(getApplicationContext(), com.example.media.dictionary.Translate.class);
        translateLayout.putExtra("searchWord", edtTextSearch.getText().toString());
        startActivity(translateLayout);
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
