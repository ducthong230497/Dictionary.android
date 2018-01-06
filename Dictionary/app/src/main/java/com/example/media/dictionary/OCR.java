package com.example.media.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;



public class OCR extends AppCompatActivity {
    Button btnRunOCR;
    // use for Acticity result
    final int OCR_RESULT = 2;
    // use for OCR
    String mCurrentPhotoPath;

    private TessBaseAPI mTess; //Tess API reference
    String datapath = ""; //path to folder containing language data file

    ImageView imvPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        imvPicture = (ImageView) findViewById(R.id.imvOcrPicture);
        imvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent Camera = new Intent(getApplicationContext(), com.example.media.dictionary.camera.class);
                Intent Camera = new Intent(getApplicationContext(), com.example.media.dictionary.camera2.class);
                startActivityForResult(Camera, OCR_RESULT);
            }
        });
        btnRunOCR = (Button) findViewById(R.id.btnOCR);
        btnRunOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image2 = BitmapFactory.decodeFile(mCurrentPhotoPath);
                //initialize Tesseract API
                String language = "vie";
                datapath = getFilesDir()+ "/tesseract/";
                mTess = new TessBaseAPI();

                checkFile(new File(datapath + "tessdata/"), language);

                mTess.init(datapath, language);

                String OCRresult = null;
                mTess.setImage(image2);
                OCRresult = mTess.getUTF8Text();
                mTess.end();
                TextView OCRTextView = (TextView) findViewById(R.id.textview1);
                OCRTextView.setText(OCRresult);
                Cursor c = MainActivity.dictionaryDatabase.getWordMatches(OCRresult, null);
                String definition;
                if (c != null){
                    definition = c.getString(1);
                    Intent translateLayout = new Intent(getApplicationContext(), com.example.media.dictionary.Translate.class);
                    translateLayout.putExtra("searchWord", OCRresult);
                    translateLayout.putExtra("definition", definition);
                    startActivity(translateLayout);
                }
                else{
                    Toast.makeText(getApplicationContext(), "this word doesn't have in our database", Toast.LENGTH_LONG);
                }

            }
        });
    }
    private void copyFiles(String lang) {
        try {
            String filepath = datapath + "/tessdata/"+ lang +".traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/"+ lang +".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir, String lang) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles(lang);
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/"+ lang +".traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles(lang);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OCR_RESULT && resultCode == RESULT_OK && data != null){
            btnRunOCR.setEnabled(true);
            Bundle extras = data.getExtras();
            Bitmap bitmap = extras.getParcelable("cropImage");
            imvPicture.setImageBitmap(bitmap);
            String cropImageName = data.getStringExtra("cropImageName");
            //
            // save image temporarily
            //
            File f3=new File(Environment.getExternalStorageDirectory()+"/TemporaryImage/");
            if(!f3.exists())
                f3.mkdirs();
            OutputStream outStream = null;
            File file = new File(Environment.getExternalStorageDirectory() + "/TemporaryImage/"+cropImageName+".png");
            try {
                outStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
                //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCurrentPhotoPath = file.getAbsolutePath();
        }
    }
}
