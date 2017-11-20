package com.example.wailiantong.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.wailiantong.CameraView.CameraHouseSurfaceView;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.windowsView.BrowserMain;

import java.util.ArrayList;

/**
 * Created by 蔚克 on 2017/6/26.
 */

public class ReTakeHouse extends AppCompatActivity {

    private Button takebutton, viewAll;
    private CameraHouseSurfaceView mCameraSurfaceView;
    private int nowPosition;
    private ArrayList<String> nowImages;
    private String folderUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.rehouse_camera_layout);
        mCameraSurfaceView = (CameraHouseSurfaceView) findViewById(R.id.re_house_cameraSurfaceView);
        takebutton = (Button) findViewById(R.id.re_house_takePic);
        viewAll = (Button) findViewById(R.id.re_house_camera_viewall);


        nowPosition = getIntent().getIntExtra("position", -1);
        nowImages = getIntent().getStringArrayListExtra("images");
        folderUrl = getIntent().getStringExtra("folderUrl");

        takebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraSurfaceView.takePicture();
            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mCameraSurfaceView.getNewImageUrl()为空
                nowImages.set(nowPosition, mCameraSurfaceView.getNewImageUrl());
                Logger.i("这是新的路径的打印", mCameraSurfaceView.getNewImageUrl() + "可能为空" + folderUrl);
                Intent reViewAllIntent = new Intent(ReTakeHouse.this, BrowserMain.class);
                reViewAllIntent.putStringArrayListExtra("newImages", nowImages);
                reViewAllIntent.putExtra("type", 3 + "");//1表示身份证识别
                reViewAllIntent.putExtra("folderUrl", folderUrl);
                Logger.i("这是重新拍摄的打印", nowImages.toString());
                finish();
                startActivity(reViewAllIntent);
            }
        });
    }
}
