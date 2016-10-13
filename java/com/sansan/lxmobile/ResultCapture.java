package com.sansan.lxmobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 展示图片
 * Created by HX-MG01 on 2016/10/13.
 */
public class ResultCapture extends Activity {
    private ImageView iv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("--ResultCapture", "1111111");
        setContentView(R.layout.result);
        String picPath = getIntent().getStringExtra("picPath");
        iv_result = (ImageView) findViewById(R.id.iv_result);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(picPath));
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            // 矩形设置
            Matrix matrix = new Matrix();
            matrix.setRotate(90);//设置偏转90度，保持跟照相机中的预览一样---camera.setDisplayOrientation(90);
            // 重新修改图片，保持跟我们之前CustomCamera中预览效果一样
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            iv_result.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 获取图片，展示
//        Bitmap bitmap2=BitmapFactory.decodeFile(picPath);
//        iv_result.setImageBitmap(bitmap2);

    }

    public void goBack(View v) {
        this.finish();
    }
}
