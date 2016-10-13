package com.sansan.lxmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static int REQ_1 = 1;
    private static int REQ_2 = 2;
    Button btn_01;
    Button btn_02;
    Button custom_camera;
    ImageView iv_photo;
    private String mFilePath; // 定义sd卡路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得sd卡路径
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "temp.png";
        init();
    }

    private void init() {
        btn_01 = (Button) findViewById(R.id.open_camera1);
        btn_02 = (Button) findViewById(R.id.open_camera2);
        custom_camera = (Button) findViewById(R.id.custom_camera);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
        custom_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_camera1://启动相机
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //通过返回requestCode来写
                startActivityForResult(camera, REQ_1);
                break;
            case R.id.open_camera2://启动相机
                Intent camera2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 修改照片保存路径
                Uri photoUri = Uri.fromFile(new File(mFilePath));
                camera2.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //通过返回requestCode来读取相机照片，显示相机照片
                startActivityForResult(camera2, REQ_2);
                break;
            case R.id.custom_camera:
                Intent intent =new Intent(this,CustomCamera.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_1) {
                Bundle bundle = data.getExtras();
                // 将图片显示在ImageView中
                // 这种方式返回来的图像是缩略图
                Bitmap bitmap = (Bitmap) bundle.get("data");
                iv_photo.setImageBitmap(bitmap);
            } else if (requestCode == REQ_2) {
                FileInputStream fis = null;
                try {
                    // 将文件转换成Bitmap
                    // 通过文件修改相机照片保存路径，来保存照片，并读取照片内容
                    fis = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    iv_photo.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
