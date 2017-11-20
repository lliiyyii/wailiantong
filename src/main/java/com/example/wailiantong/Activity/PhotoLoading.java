package com.example.wailiantong.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.windowsView.BrowserMainDetail;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by weike on 2017/7/5.
 */

public class PhotoLoading extends BaseActivity {
    private  String picFirst,picSecond;
    private ImageView back;
    private List<String> imgUrl=new ArrayList<>();
    private File mFile;
    private Bitmap bitmapFirst,bitmapSecond;
    private RotateLoading loading;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (picSecond.equals("null")){
                        loading.stop();
                        Logger.i("打印两张图片的本地路径",imgUrl.get(0)+" 00000");
                        Intent viewPicIntent =new Intent(PhotoLoading.this,BrowserMainDetail.class);
                        viewPicIntent.putStringArrayListExtra("newImages", (ArrayList<String>) imgUrl);
                        startActivity(viewPicIntent);
                        finish();
                    }else downloadPicSecond(picSecond);
                    break;
                case 2:
                    loading.stop();
                    Logger.i("打印两张图片的本地路径",imgUrl.get(0)+imgUrl.get(1)+" 00000");
                    Intent viewPicIntent =new Intent(PhotoLoading.this,BrowserMainDetail.class);
                    viewPicIntent.putStringArrayListExtra("newImages", (ArrayList<String>) imgUrl);
                    startActivity(viewPicIntent);
                    finish();
                    break;


                default:
                    break;
            }

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetailpic_layout);
        loading= (RotateLoading) findViewById(R.id.rotateloading);
        loading.start();
        makeNewDir();


//        back= (ImageView) findViewById(R.id.viewdetail_back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


        picFirst=getIntent().getStringExtra("picFirst")+"";
        picSecond=getIntent().getStringExtra("picSecond")+"";
        Logger.i("看看传过来的图片是多少呢？",picFirst+picSecond);
        downloadPicFirst(picFirst);


    }
    public void downloadPicFirst(String pic) {
        final String getInfoDetailUrl = "http://139.199.9.72:8003" + pic;
        Logger.i("打印下载图片的地址", getInfoDetailUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(getInfoDetailUrl).get().build();
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    //转化为bitmap
                    InputStream in = body.byteStream();
                    bitmapFirst = BitmapFactory.decodeStream(in);
                    imgUrl.add(0,saveBitmap(bitmapFirst));
                    mainHandler.sendEmptyMessage(1);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }

    public void downloadPicSecond(String pic) {
        final String getInfoDetailUrl = "http://139.199.9.72:8003" + pic;
        Logger.i("打印下载图片的地址", getInfoDetailUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(getInfoDetailUrl).get().build();
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    //转化为bitmap
                    InputStream in = body.byteStream();
                    bitmapSecond = BitmapFactory.decodeStream(in);
                    imgUrl.add(1,saveBitmap(bitmapSecond));
                    mainHandler.sendEmptyMessage(2);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }








    private String saveBitmap(Bitmap bitmap) throws IOException {
       // String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        File file = new File(mFile + "/" + System.currentTimeMillis() + ".jpeg");

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.toString();

    }


    private void makeNewDir() {
        // 判断SDcard状态
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            // 错误提示
        }
        // 检查SDcard空间
        File SDCardRoot = Environment.getExternalStorageDirectory();
        if (SDCardRoot.getFreeSpace() < 10000)
        {
            // 弹出对话框提示用户空间不够
            Log.e("Utils", "存储空间不够");
        }

        // 在SDcard创建文件夹及文件
        mFile = new File(SDCardRoot.getPath() + "/gongzhengtongdetail/");
        if (!mFile.exists()) {
            mFile.mkdirs();
            Logger.i("55555555555555555555", mFile.toString());
        }
    }
}
