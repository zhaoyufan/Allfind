package com.example.mo.allfind.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.mo.allfind.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaoActivity extends AppCompatActivity implements SurfaceHolder.Callback, SensorEventListener,Camera.PictureCallback {
    private static final int START_SHAKE = 0x111;
    private static final int START_TAKEPIC=0x112;
    private CameraManager mCameraManager;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private Camera camera;
    private Vibrator mVibrator;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float[] gravity = new float[3];
    private static final String SAVENAME="img";



    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_SHAKE:
                    mVibrator.vibrate(50);
                    break;
                case START_TAKEPIC:
                    //自动拍照
                    camera.takePicture(new Camera.ShutterCallback() {
                        @Override
                        public void onShutter() {

                        }
                    }, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {

                        }
                    }, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {

                        }
                    }, SaoActivity.this);
                    Log.i("remind","自动拍照");
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sao);
        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);

        mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSurfaceView.setZOrderOnTop(false);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d("error", "设备不支持传感器");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        HandlerThread mHandlerThread = new HandlerThread("Camera2");
//        mHandlerThread.start();
//        mHandler = new Handler(mHandlerThread.getLooper());
//        try {
//            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
//            mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight()
//                    , ImageFormat.JPEG,/*maxImages*/7);
//            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mHandler);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling 什么什么权限申明的
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            mCameraManager.openCamera(mCameraId, DeviceStateCallback, mHandler);
//        }catch (CameraAccessException e){
//            Log.e("open fail","open fail"+e.getMessage());
//            e.printStackTrace();
//        }
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        camera.setDisplayOrientation(90);//旋转90度
        Camera.Parameters parameters = camera.getParameters();//获取参数
        parameters.setPreviewSize(1280, 960); // 设置大小
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
        }
        camera.release();
        camera = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = (float) 0.8;
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            String accelerometer = "加速度传感器\n" + "x:"
                    + (event.values[0] - gravity[0]) + "\n" + "y:"
                    + (event.values[1] - gravity[1]) + "\n" + "z:"
                    + (event.values[2] - gravity[2]);
            Log.d("加速传感器", accelerometer);

            if ((Math.abs(event.values[0] - gravity[0]) < 0.02 && Math.abs(event.values[1] - gravity[1]) < 0.02 && Math.abs(event.values[2] - gravity[2]) < 0.02)) {
                mSensorManager.unregisterListener(this);
//                Toast.makeText(SaoActivity.this,"111",Toast.LENGTH_SHORT).show();
                Log.d("MM", "没有晃动");
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);
                            //开始震动
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            mHandler.obtainMessage(START_TAKEPIC).sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                }
            }
        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //注册加速度传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),//传感器TYPE类型
                SensorManager.SENSOR_DELAY_UI);//采集频率
        //注册重力传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                try {
                    Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);


//                    SavetoSDcard(bitmap,SAVENAME);
//                    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//                    byte [] bytes=outputStream.toByteArray();
//                    Intent it = new Intent(SaoActivity.this, NextActivity.class);
//                    it.putExtra("bitmap",bytes);
//                    startActivity(it);

                } catch (Exception e) {
                    Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(SaoActivity.this, "数据出错啦..", Toast.LENGTH_SHORT).show();
            }

    }

    private void SavetoSDcard(Bitmap bitmap, String savename) {
        File file=new File("/sdcard/Test/"+savename+".jpg");
        FileOutputStream fOut=null;
        try {
            fOut=new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
