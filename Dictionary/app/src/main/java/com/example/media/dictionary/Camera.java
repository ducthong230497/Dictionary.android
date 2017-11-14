package com.example.media.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Media on 11/2/2017.
 */

public class camera extends Activity implements SurfaceHolder.Callback{
    //
    // use for camera
    //
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    android.hardware.Camera.PictureCallback jpegCallback;
    long name;

    //
    // use for rectangleView
    //
    ViewGroup _mainLayout;
    RectangleView rectangleView;
    private int _xDelta;
    private int _yDelta;
    int _xRectView = 0;
    int _yRectView = 0;
    int _widthRectView = 0;
    int _heightRectView = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new android.hardware.Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                FileOutputStream outStream = null;
                try {
                    name = System.currentTimeMillis();
                    Log.d("Name", ""+name);
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("error", "error in file not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("error", "error in IO exception");
                } finally {
                }
                Log.d("error", "before decode");
                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                //BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inJustDecodeBounds = true;
                //Bitmap bitmap = BitmapFactory.decodeFile(String.format("/sdcard/%d.jpg", name));
                Log.d("screenWidth", ""+ Resources.getSystem().getDisplayMetrics().widthPixels);
                Log.d("screenHeight", ""+ Resources.getSystem().getDisplayMetrics().heightPixels);
                //Log.d("bitmapWidth", ""+options.outWidth);
                //Log.d("bitmapHeight", ""+options.outHeight);
                Log.d("error", "after decode");
                Log.d("_xRect", ""+_xRectView);
                Log.d("_yRect", ""+_yRectView);
                Log.d("_widthRect", ""+_widthRectView);
                Log.d("_heightRect", ""+_heightRectView);
                int surfaceViewWidth = surfaceView.getWidth();
                int surfaceViewHeight = surfaceView.getHeight();
                Log.d("surfaceViewWidth", ""+surfaceViewWidth);
                Log.d("surfaceViewHeight", ""+surfaceViewHeight);
                float a = (float)480 / (float)surfaceViewHeight;
                float b = (float)640 / (float)surfaceViewWidth;
                Log.d("a", ""+a);
                Log.d("b", ""+b);
                Log.d("Newx", ""+_xRectView*b);
                Log.d("Newy", ""+_yRectView*a);
                Log.d("NewWidth", ""+_widthRectView*b);
                Log.d("NewHeight", ""+_heightRectView*b);
                Bitmap cropBitmap = Bitmap.createBitmap(bitmap, (int)(_xRectView*b), (int)(_yRectView*a), (int)(_widthRectView*b),(int)(_heightRectView*a));
                Log.d("error", "after crop");
                imageView.setImageBitmap(cropBitmap);
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                // put extra
                Intent it = new Intent();
                it.putExtra("cropImage", cropBitmap);
                it.putExtra("cropImageName",  ""+name);
                setResult(Activity.RESULT_OK, it);
                finish();
                refreshCamera();
            }
        };
        //
        // co giãn hình chữ nhật khi kéo
        //
        _mainLayout = (RelativeLayout) findViewById(R.id.container);
        rectangleView = (RectangleView) findViewById(R.id.rectView);
        rectangleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                int width= view.getLayoutParams().width;
                int height = view.getLayoutParams().height;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("leftmargin", " "+ _xRectView);
                        Log.d("Width", ""+width);
                        Log.d(">>", "x + width:" + (_xRectView + width) + " height:" + height + " x:" + x + " y:" + y);
                        break;
                }
                if((Math.abs(width - x) <= 100 && Math.abs(width - x) > 0)){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_MOVE:
                            Log.e(">>","width:"+width+" height:"+height+" x:"+x+" y:"+y);
                            view.getLayoutParams().width = x;
                            //view.getLayoutParams().height = y;
                            view.requestLayout();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }
                _mainLayout.invalidate();
                return true;
            }
        });
        //
        // cập nhập lại tọa độ và độ dài khi thay đổi hình chữ nhật
        //
        rectangleView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int[] posXY = new int[2];
                rectangleView.getLocationInWindow(posXY);
                _xRectView = posXY[0];
                _yRectView = posXY[1];
                _widthRectView = rectangleView.getWidth();
                _heightRectView = rectangleView.getHeight();
            }
        });
    }

    //
    // Lấy tọa độ ban đầu của hình chữ nhật
    // * nếu lấy tọa độ trong hàm onCreate() thì nó sẽ trả về 0 vì lúc đó nó chưa được hiển thị lên layout
    //
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int[] posXY = new int[2];
        rectangleView.getLocationInWindow(posXY);
        _xRectView = posXY[0];
        _yRectView = posXY[1];
        Log.d("X and Y Point", _xRectView + " " + _yRectView);
    }

    public void captureImage(View v) throws IOException {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        android.hardware.Camera.Parameters param;
        param = camera.getParameters();

        // modify parameter
        param.setPreviewSize(400, 400);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
