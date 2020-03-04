package com.replon.www.grace_thehealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeartbeatActivity extends AppCompatActivity implements SurfaceHolder.Callback, Handler.Callback {

    public static final String TAG = "HeartbeatActivity";

    static final int MY_PERMISSIONS_REQUEST_CAMERA = 1242;
    private static final int MSG_CAMERA_OPENED = 1;
    private static final int MSG_SURFACE_READY = 2;
    private final Handler mHandler = new Handler(this);

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    CameraManager mCameraManager;
    String[] mCameraIDsList;
    CameraDevice.StateCallback mCameraStateCB;
    CameraDevice mCameraDevice;
    CameraCaptureSession mCaptureSession;
    boolean mSurfaceCreated = true;
    boolean mIsCameraConfigured = false;
    private Surface mCameraSurface = null;



    ImageView back;

    private static View image;
    TextView tv_heartbeat;

    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartbeat1);


        init();


        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);


        try {
            mCameraIDsList = this.mCameraManager.getCameraIdList();


            for (String id : mCameraIDsList) {
                Log.v(TAG, "CameraID: " + id);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        mCameraStateCB = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                Toast.makeText(getApplicationContext(), "onOpened", Toast.LENGTH_SHORT).show();

                mCameraDevice = camera;
                mHandler.sendEmptyMessage(MSG_CAMERA_OPENED);
            }

            @Override
            public void onDisconnected(CameraDevice camera) {
                Log.i(TAG,"ON DISCONNECT CALLED");
                Toast.makeText(getApplicationContext(), "onDisconnected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(CameraDevice camera, int error) {
                Toast.makeText(getApplicationContext(), "onError", Toast.LENGTH_SHORT).show();
            }
        };



    }


    private void init(){

//        image = findViewById(R.id.image);
//        tv_heartbeat = (TextView) findViewById(R.id.tv_heartbeat);
    }

//    private void getHeartbeat(byte[] data){
//
//
//        if (!processing.compareAndSet(false, true)){
//            Log.i(TAG, "CAME IN COMPARE AND SET");
//            return;
//        }
//
//        int width = 100;
//        int height = 100;
//
//
//        int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
//        // Log.i(TAG, "imgAvg="+imgAvg);
//        if (imgAvg == 0 || imgAvg == 255) {
//            processing.set(false);
//            return;
//        }
//
//        int averageArrayAvg = 0;
//        int averageArrayCnt = 0;
//        for (int i = 0; i < averageArray.length; i++) {
//            if (averageArray[i] > 0) {
//                averageArrayAvg += averageArray[i];
//                averageArrayCnt++;
//            }
//        }
//
//        int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
//        TYPE newType = currentType;
//
////            imgavgtxt.setText("image average:"+ Integer.toString(imgAvg));
////            rollavgtxt.setText("rolling average:"+ Integer.toString(rollingAverage));
//        if (imgAvg < rollingAverage) {
//            newType = TYPE.RED;
//            if (newType != currentType) {
//                beats++;
//                // Log.d(TAG, "BEAT!! beats="+beats);
//            }
//        } else if (imgAvg > rollingAverage) {
//            newType = TYPE.GREEN;
//        }
//
//        if (averageIndex == averageArraySize) averageIndex = 0;
//        averageArray[averageIndex] = imgAvg;
//        averageIndex++;
//
//        // Transitioned from one state to another to the same
//        if (newType != currentType) {
//            currentType = newType;
//            image.postInvalidate();
//        }
//
//        long endTime = System.currentTimeMillis();
//        double totalTimeInSecs = (endTime - startTime) / 1000d;
//        if (totalTimeInSecs >= 10) {
//            double bps = (beats / totalTimeInSecs);
//            int dpm = (int) (bps * 60d);
//            if (dpm < 30 || dpm > 180) {
//                startTime = System.currentTimeMillis();
//                beats = 0;
//                processing.set(false);
//                return;
//            }
//
//            // Log.d(TAG,
//            // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);
//
//            if (beatsIndex == beatsArraySize) beatsIndex = 0;
//            beatsArray[beatsIndex] = dpm;
//            beatsIndex++;
//
//            int beatsArrayAvg = 0;
//            int beatsArrayCnt = 0;
//            for (int i = 0; i < beatsArray.length; i++) {
//                if (beatsArray[i] > 0) {
//                    beatsArrayAvg += beatsArray[i];
//                    beatsArrayCnt++;
//                }
//            }
//            int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
//            tv_heartbeat.setText(String.valueOf(beatsAvg)+" bpm");
//            startTime = System.currentTimeMillis();
//            beats = 0;
//        }
//        processing.set(false);
//    }






    Bitmap fromByteBuffer(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes, 0, bytes.length);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //requesting permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(getApplicationContext(), "request permission", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "PERMISSION_ALREADY_GRANTED", Toast.LENGTH_SHORT).show();
            try {
                mCameraManager.setTorchMode(mCameraIDsList[0],true);
                mCameraManager.openCamera(mCameraIDsList[0], mCameraStateCB, new Handler());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mCaptureSession != null) {
                mCaptureSession.stopRepeating();
                mCaptureSession.close();
                mCaptureSession = null;
            }


            mIsCameraConfigured = false;
        } catch (final CameraAccessException e) {
            // Doesn't matter, closing device anyway
            e.printStackTrace();
        } catch (final IllegalStateException e2) {
            // Doesn't matter, closing device anyway
            e2.printStackTrace();
        } finally {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
                mCaptureSession = null;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CAMERA_OPENED:
            case MSG_SURFACE_READY:
                // if both surface is created and camera device is opened
                // - ready to set up preview and other things
                if (mSurfaceCreated && (mCameraDevice != null)
                        && !mIsCameraConfigured) {
                    configureCamera();
                }
                break;
        }

        return true;
    }

    private void configureCamera() {
        // prepare list of surfaces to be used in capture requests
        List<Surface> sfl = new ArrayList<Surface>();

        sfl.add(mCameraSurface); // surface for viewfinder preview

        // configure camera with all the surfaces to be ever used
        try {
            mCameraDevice.createCaptureSession(sfl,
                    new CaptureSessionListener(), null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mIsCameraConfigured = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    try {
                        mCameraManager.setTorchMode(mCameraIDsList[0],true);
                        mCameraManager.openCamera(mCameraIDsList[0], mCameraStateCB, new Handler());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCameraSurface = holder.getSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCameraSurface = holder.getSurface();
        mSurfaceCreated = true;
        mHandler.sendEmptyMessage(MSG_SURFACE_READY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceCreated = false;
    }

    private class CaptureSessionListener extends
            CameraCaptureSession.StateCallback {
        @Override
        public void onConfigureFailed(final CameraCaptureSession session) {
            Log.d(TAG, "CaptureSessionConfigure failed");
        }

        @Override
        public void onConfigured(final CameraCaptureSession session) {
            Log.d(TAG, "CaptureSessionConfigure onConfigured");
            mCaptureSession = session;

            try {
                CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);

                previewRequestBuilder.addTarget(mCameraSurface);
                mCaptureSession.setRepeatingRequest(previewRequestBuilder.build(),
                        null, null);
            } catch (CameraAccessException e) {
                Log.d(TAG, "setting up preview failed");
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
