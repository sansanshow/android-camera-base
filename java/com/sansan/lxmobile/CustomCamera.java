package com.sansan.lxmobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomCamera extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceView mSurface;//预览窗口
    private SurfaceHolder mHolder;
    //回调函数
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 创建图片保存路径
            File tempFile = new File("/sdcard/temp.png");
            try {
                // 将data中的数据（图片文件）写入文件
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                // 开启展示拍照结果Activity
                Intent intent = new Intent(CustomCamera.this, ResultCapture.class);
                intent.putExtra("picPath", tempFile.getAbsolutePath());
                startActivity(intent);
                CustomCamera.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);
        mSurface = (SurfaceView) findViewById(R.id.preview);
        mHolder = mSurface.getHolder();
        mHolder.addCallback(this);
        // 為了更加真是的显示我们预览的图片，当用户点击的时候我们自动聚焦
        mSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }

    /**
     * 初始化相机
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 完成拍照功能
     *
     * @param v
     */
    public void capture(View v) {
        // 获取当前照相机参数
        Camera.Parameters parameters = mCamera.getParameters();
        // 设置参数
        parameters.setPictureFormat(ImageFormat.JPEG);//设置照片格式
        parameters.setPictureSize(800, 400);//设置大小
        parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);//设置对焦模式
        //对焦回调函数
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {//当对焦成功时获取图片
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            }
        });
    }

    /**
     * 获取Camera对象
     *
     * @return
     */
    private Camera getCamera() {
        //获取Camera 注意导包正确
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 开始预览相机内容
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
