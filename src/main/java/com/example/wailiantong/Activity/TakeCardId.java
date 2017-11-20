package com.example.wailiantong.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wailiantong.CameraView.CameraSurfaceView;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.utils.GetImages;
import com.example.wailiantong.View.imagebrowse.windowsView.BrowserMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔚克 on 2017/6/26.
 */

public class TakeCardId extends AppCompatActivity {

    private ImageView takePic;
    private CameraSurfaceView mCameraSurfaceView;
    public String folderUrl;
    public List<String> newImages;
    public int imgNum;
    private ImageView viewAll,cancel;
    private TextView numText,side;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    newImages=new GetImages().getImagePathFromSD(mCameraSurfaceView.getmFileUrl());
                    Logger.i("打印此时拍照的数量",newImages.size()+"      000000000000");
                    imgNum=newImages.size();
                    numText.setText(imgNum+"");
                    if ((imgNum & 1) != 1) {
                        //偶数 开头为1
                        side.setText("请拍正面");
                    } else {
                        side.setText("请拍背面");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        imgNum=0;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.zsy_camera_cardid_layout);
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        takePic = (ImageView) findViewById(R.id.takePic);
        viewAll= (ImageView) findViewById(R.id.camera_viewall);
        side= (TextView) findViewById(R.id.camera_side);
        numText= (TextView) findViewById(R.id.camera_num);
        cancel= (ImageView) findViewById(R.id.camera_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraSurfaceView.takePicture();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(500);
                            handler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewAllIntent = new Intent(TakeCardId.this,BrowserMain.class);
                folderUrl=mCameraSurfaceView.getmFileUrl();
                newImages=new GetImages().getImagePathFromSD(mCameraSurfaceView.getmFileUrl());
                viewAllIntent.putExtra("type",1+"");//1表示身份证识别
                viewAllIntent.putStringArrayListExtra("newImages", (ArrayList<String>) newImages);
                viewAllIntent.putExtra("folderUrl",folderUrl);
                finish();
                startActivity(viewAllIntent);
            }
        });
    }
}
