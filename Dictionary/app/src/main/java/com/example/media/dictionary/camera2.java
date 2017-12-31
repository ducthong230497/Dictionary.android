package com.example.media.dictionary;

/**
 * Created by Media on 11/18/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class camera2 extends AppCompatActivity {
    //region declaration
    private final String TAG = this.getClass().getSimpleName();
    private Button takePictureButton;
    private TextureView textureView;
    TextView textViewW;
    TextView textViewH;
    RectangleView rectangleView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);


        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = (Button) findViewById(R.id.btn_takepicture);
        //assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("before take picture", "sdjkahsdjkahd");
                takePicture();
            }
        });

        textViewW = (TextView) findViewById(R.id.textureW);
        textViewH = (TextView) findViewById(R.id.textureH);

        //region rectangleView
        rectangleView = (RectangleView) findViewById(R.id.rectViewCamera2);
        rectangleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int xTouch = (int) event.getRawX();
                final int yTouch = (int) event.getRawY();
                int width= view.getLayoutParams().width;
                int height = view.getLayoutParams().height;

                int xOffSet_2 = rectangleView.point2.x - xTouch;
                int yOffSet_2 = rectangleView.point2.y - yTouch;
                if(Math.abs(xOffSet_2) <= 100 && Math.abs(yOffSet_2) <= 100){
                    Log.d("ACTIONMOVE", "after cal offset");
                    switch (event.getAction()){
                        case MotionEvent.ACTION_MOVE:
                            //Log.e(">>","width:"+width+" height:"+height+" x:"+x+" y:"+y);
                            //view.getLayoutParams().width = xTouch;
                            //view.getLayoutParams().height = yTouch;
                            //if (yTouch >= Resources.getSystem().getDisplayMetrics().widthPixels)
                            //view.getLayoutParams().height = yTouch - Resources.getSystem().getDisplayMetrics().widthPixels;
                            rectangleView.point2.set(xTouch, yTouch);
                            //rectangleView.point1.set(   textureView.getWidth() - rectangleView.point2.x,
                              //                          textureView.getHeight() - rectangleView.point2.y);
                            Log.d("point1", ""+rectangleView.point2.x + " " + rectangleView.point2.y);
                            TextView txv_X = (TextView) findViewById(R.id.txv_X);
                            TextView txv_Y = (TextView) findViewById(R.id.txv_Y);
                            TextView txv_W = (TextView) findViewById(R.id.txv_W);
                            TextView txv_H = (TextView) findViewById(R.id.txv_H);
                            txv_X.setText("x: " + rectangleView.point1.x);
                            txv_Y.setText("y: " + rectangleView.point1.y);
                            txv_W.setText("width: " + Math.abs(rectangleView.point2.x - rectangleView.point1.x));
                            txv_H.setText("height: " + Math.abs(rectangleView.point2.y - rectangleView.point1.y));
                            view.invalidate();
                            view.requestLayout();

                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }
                //_mainLayout.invalidate();
                return true;
            }
        });
//endregion
    }
    //region TextureView.SurfaceTextureListener
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    //endregion

    //
    // Lấy tọa độ ban đầu của textureView
    // * nếu lấy tọa độ trong hàm onCreate() thì nó sẽ trả về 0 vì lúc đó nó chưa được hiển thị lên layout
    //
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        textViewW.setText("textureWidth: "+textureView.getWidth());
        textViewH.setText("textureHeight: "+textureView.getHeight());
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(camera2.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void takePicture() {
        if(null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final File file = new File(Environment.getExternalStorageDirectory()+"/pic.jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    Bitmap bitmap = null;
                    Bitmap cropBitmap = null;
                    long name = System.currentTimeMillis();
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes, name);
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                    float textureViewWidth = textureView.getWidth();
                    float textureViewHeight = textureView.getHeight();
                    //float a = (float)textureViewHeight / (float)2988;
                    //float b = (float)textureViewWidth / (float)5312;
                    float a = 2392 / 3264;
                    float b = 1440 / 2448;
                    Log.e("texturewidth", ""+textureViewWidth);
                    Log.e("textureheight", ""+textureViewHeight);
                    Log.e("rectangleView.Point1.x", ""+rectangleView.point1.x);
                    Log.e("rectangleView.Point1.y", ""+rectangleView.point1.y);
                    if (bitmap != null){
                        Matrix matrix = new Matrix();
                        matrix.setRotate(90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        Bitmap scaleBitmap = Bitmap.createScaledBitmap(rotatedBitmap, size.x, size.y, true);
                        float sx = size.x;
                        float sy = size.y;
                        float bmw = scaleBitmap.getWidth();
                        float bmh = scaleBitmap.getHeight();
                        /*cropBitmap = Bitmap.createBitmap(rotatedBitmap, (int)(rectangleView.point1.x*b),
                                (int)(rectangleView.point1.y*a),
                                (int)(Math.abs(rectangleView.point2.x - rectangleView.point1.x)*b),
                                (int)(Math.abs(rectangleView.point2.y - rectangleView.point1.y)*a));*/
                        Log.d("Point1_x:", ""+rectangleView.point1.x);
                        Log.d("Point1_y:", ""+rectangleView.point1.y);
                        Log.d("Point2_x:", ""+rectangleView.point2.x);
                        Log.d("Point2_y:", ""+rectangleView.point2.y);
                        int h = (int)(Math.abs(rectangleView.point2.y - rectangleView.point1.y));
                        cropBitmap = Bitmap.createBitmap(scaleBitmap, (int)(rectangleView.point1.x),
                                (int)(rectangleView.point1.y - 20),
                                (int)(Math.abs(rectangleView.point2.x - rectangleView.point1.x)),
                                (int)(Math.abs(rectangleView.point2.y - rectangleView.point1.y)));

                        // put bitmap extra
                        Intent it = new Intent();
                        it.putExtra("cropImage", cropBitmap);
                        it.putExtra("cropImageName",  ""+name);
                        setResult(Activity.RESULT_OK, it);
                        finish();
                    }

                }
                private void save(byte[] bytes, long name) throws IOException {
                    OutputStream output = null;
                    try {
                        long m = bytes.length;
                        output = new FileOutputStream(String.format("/sdcard/%d.jpg", name));
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(camera2.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(camera2.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(camera2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(camera2.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        super.onPause();
    }
}
