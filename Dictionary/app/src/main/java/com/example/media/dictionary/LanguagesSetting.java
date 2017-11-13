package com.example.media.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by Lirus on 20.10.2017.
 */

public class LanguagesSetting extends Activity {
    Button btnSettingEnglish;
    Button btnSettingGerman;
    Button btnSettingVietnamese;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languagesetting);

        btnSettingEnglish= (Button) findViewById(R.id.btnSettingEnglish);
        final Intent intent = new Intent();
        btnSettingEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("LanguageCode", "en");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnSettingGerman = (Button) findViewById(R.id.btnSettingGerman);
        btnSettingGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("LanguageCode", "de");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnSettingVietnamese = (Button) findViewById(R.id.btnSettingVietnamese);
        btnSettingVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("LanguageCode", "vi");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
