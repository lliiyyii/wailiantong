package com.example.wailiantong.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.wailiantong.CameraView.CameraBusinessSurfaceViewCopy;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.windowsView.BrowserMain;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by weike on 2017/6/29.
 */

public class ReTakeBusiness extends AppCompatActivity{

    private ImageView takebutton;
    private CameraBusinessSurfaceViewCopy mCameraSurfaceView;
    private int nowPosition;
    private ArrayList<String> nowImages;
    private String folderUrl;
    private ImageView viewAll,cancel;
    private File deleteFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.zsy_camera_rebusiness_layout);
        mCameraSurfaceView = (CameraBusinessSurfaceViewCopy) findViewById(R.id.re_business_cameraSurfaceView);
        takebutton = (ImageView) findViewById(R.id.re_business_takePic);
        viewAll= (ImageView) findViewById(R.id.re_business_camera_viewall);

        cancel= (ImageView) findViewById(R.id.re_business_camera_cancel);
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
//                //mCameraSurfaceView.getNewImageUrl()为空
//                nowImages.set(nowPosition,mCameraSurfaceView.getNewImageUrl());
//                Logger.i("这是新的路径的打印",mCameraSurfaceView.getNewImageUrl()+"可能为空"+folderUrl);
//                Intent reViewAllIntent = new Intent(ReTakeCard.this,BrowserMain.class);
//                reViewAllIntent.putStringArrayListExtra("newImages", nowImages);
//                reViewAllIntent.putExtra("folderUrl",folderUrl);
//                Logger.i("这是重新拍摄的打印",nowImages.toString());
//                finish();
//                startActivity(reViewAllIntent);


            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nowImages.set(nowPosition,mCameraSurfaceView.getNewImageUrl());
                Logger.i("这是新的路径的打印",mCameraSurfaceView.getNewImageUrl()+"可能为空"+folderUrl);
                Intent rebusViewAllIntent = new Intent(ReTakeBusiness.this,BrowserMain.class);
                rebusViewAllIntent.putExtra("type",2+"");
                rebusViewAllIntent.putStringArrayListExtra("newImages", nowImages);
                rebusViewAllIntent.putExtra("folderUrl",folderUrl);
                Logger.i("这是重新拍摄的打印",nowImages.toString());
                finish();
                startActivity(rebusViewAllIntent);
            }
        });
    }
}
