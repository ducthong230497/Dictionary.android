package com.example.media.dictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Lirus on 20.10.2017.
 */

public class Setting extends LocalizationActivity {
    final int LANGUAGE_SETTING_RESULT = 1;
    Button btnLanguageSetting;
    Button btnSoftInfo;
    ImageView imvSoftInfo;
    boolean imvSoftInfoVisible = false;
    Switch swTheme;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnLanguageSetting = (Button) findViewById(R.id.btnLanguage);
        btnLanguageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageSettingInten = new Intent(getApplicationContext(), LanguagesSetting.class);
                startActivityForResult(languageSettingInten, LANGUAGE_SETTING_RESULT);
            }
        });
        imvSoftInfo = (ImageView) findViewById(R.id.imvSoftInfo);

        LayoutInflater inflater;
        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.transitionSetting);
        //final Button button = (Button) transitionsContainer.findViewById(R.id.button);

        btnSoftInfo = (Button) findViewById(R.id.btnSoftInfo);
        btnSoftInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));

                //text.setVisibility(visible ? View.VISIBLE : View.GONE);
                if(!imvSoftInfoVisible){
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.LEFT));
                    imvSoftInfoVisible = !imvSoftInfoVisible;
                    imvSoftInfo.setVisibility(View.VISIBLE);
                    //TransitionManager.endTransitions(transitionsContainer);
                }
                else
                {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                    imvSoftInfoVisible = !imvSoftInfoVisible;
                    imvSoftInfo.setVisibility(View.GONE);
                    //TransitionManager.endTransitions(transitionsContainer);
                }
            }
        });

        swTheme = (Switch) findViewById(R.id.switchTheme);
        swTheme.setChecked(true);
        swTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (swTheme.isChecked()){
                    getApplication().setTheme(R.style.AppThemeLight);
                }
                else {
                    getApplication().setTheme(R.style.AppThemeDark);
                }
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
