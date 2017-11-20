package com.example.wailiantong.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.wailiantong.CameraView.CameraSurfaceViewCopy;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.windowsView.BrowserMain;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 蔚克 on 2017/6/26.
 */

public class ReTakeCard extends AppCompatActivity {

    private ImageView takebutton;
    private CameraSurfaceViewCopy mCameraSurfaceView;
    private int nowPosition;
    private ArrayList<String> nowImages;
    private String folderUrl;
    private File deleteFile;
    private ImageView viewAll,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.zsy_camera_recardid_layout);
        mCameraSurfaceView = (CameraSurfaceViewCopy) findViewById(R.id.recameraSurfaceView);
        takebutton = (ImageView) findViewById(R.id.retakePic);
        viewAll= (ImageView) findViewById(R.id.recamera_viewall);
        cancel= (ImageView) findViewById(R.id.recamera_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        nowPosition=getIntent().getIntExtra("position",-1);
        nowImages=getIntent().getStringArrayListExtra("images");
        folderUrl=getIntent().getStringExtra("folderUrl");
        mCameraSurfaceView.setDir(folderUrl);


        takebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraSurfaceView.takePicture();
                deleteFile=new File(nowImages.get(nowPosition));
                deleteFile.delete();




            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mCameraSurfaceView.getNewImageUrl()为空


                nowImages.set(nowPosition,mCameraSurfaceView.getNewImageUrl());
                Logger.i("这是新的路径的打印",mCameraSurfaceView.getNewImageUrl()+"可能为空"+folderUrl);
                Intent reViewAllIntent = new Intent(ReTakeCard.this,BrowserMain.class);
                reViewAllIntent.putStringArrayListExtra("newImages", nowImages);
                reViewAllIntent.putExtra("type",1+"");//1表示身份证识别
                reViewAllIntent.putExtra("folderUrl",folderUrl);
                Logger.i("这是重新拍摄的打印",nowImages.toString());
                finish();
                startActivity(reViewAllIntent);
            }
        });
    }
}
