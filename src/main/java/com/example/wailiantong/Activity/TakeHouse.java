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
import com.example.wailiantong.View.imagebrowse.utils.GetImages;
import com.example.wailiantong.View.imagebrowse.windowsView.BrowserMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔚克 on 2017/6/28.
 */

public class TakeHouse extends AppCompatActivity {

    private Button takePic,viewAll;
    private CameraHouseSurfaceView mCameraSurfaceView;
    public String folderUrl;
    public List<String> newImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cardid_camera_house_layout);

        mCameraSurfaceView = (CameraHouseSurfaceView) findViewById(R.id.house_cameraSurfaceView);
        takePic = (Button) findViewById(R.id.house_takePic);
        viewAll= (Button) findViewById(R.id.house_camera_viewall);

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraSurfaceView.takePicture();
            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewAllIntent = new Intent(TakeHouse.this,BrowserMain.class);
                folderUrl=mCameraSurfaceView.getmFileUrl();
                newImages=new GetImages().getImagePathFromSD(mCameraSurfaceView.getmFileUrl());
                viewAllIntent.putExtra("type",3+"");//3表示户口本识别
                viewAllIntent.putStringArrayListExtra("newImages", (ArrayList<String>) newImages);
                viewAllIntent.putExtra("folderUrl",folderUrl);
                finish();
                startActivity(viewAllIntent);
            }
        });
    }
}