package com.example.media.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akexorcist.localizationactivity.LocalizationActivity;

/**
 * Created by Lirus on 20.10.2017.
 */

public class Setting extends LocalizationActivity {
    final int LANGUAGE_SETTING_RESULT = 1;
    Button btnLanguageSetting;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnLanguageSetting = (Button) findViewById(R.id.btnLanguage);
        btnLanguageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageSettingInten = new Intent(getApplicationContext(), LanguagesSetting.class);
                startActivityForResult(languageSettingInten, LANGUAGE_SETTING_RESULT);
            }
        });
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
    }
}
