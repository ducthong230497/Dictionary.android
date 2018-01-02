package com.example.media.dictionary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Media on 1/2/2018.
 */

public class TranslateText extends AppCompatActivity {

    Button btnStartTranslateText;
    EditText edtText;
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
            }
        });
    }


    
}
