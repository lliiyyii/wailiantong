package com.example.wailiantong.Activity;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.HttpUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.OcridCared;
import com.example.wailiantong.Utills.ToastUtils;



import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by weike on 2017/6/13.
 */

public class LivePicUrl extends BaseActivity {
    OcridCared ocridCared12;
    String PortraitImg="";
    String RealFilePath="";
    String delta="";
    public static int IDCard = 101;
    public static int LIVE = 102;
    public static int IDCard2 = 103;
    public static int LIVE2 = 104;
    public static int IDCard3 = 105;
    public static int LIVE3 = 106;
    RelativeLayout livingTestPic, livingTestVideo, idCardIden, licenseIden, houseIden, moreIden;
    private AlertDialog pic_dialog;

    private ImageView back, picOne, picTwo;
    private Button commit;
    private File mFile;
    private String header, position;
    private String picFirst, picSecond, picUrlFirst, picUrlSecond;
    private Bitmap bitmap;

    private CacheUtils cacheUtils;


    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Logger.i("打印两张图片的网络地址", picFirst + "    00000       " + picFirst + "    00000");
                    Glide.with(LivePicUrl.this).load("http://139.199.9.72:8003" + picFirst).into(picOne);
                    Glide.with(LivePicUrl.this).load("http://139.199.9.72:8003" + picSecond).into(picTwo);

                    downloadPic(picFirst);


                    break;
                case 2:
                    try {
                        picUrlFirst = saveBitmap(bitmap);
                        ToastUtils.makeToast("下载完成");
                        guestDialog.dismiss();
                        Logger.i("打印两张图片的本地地址", picUrlFirst + picUrlSecond + "    00000");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    Logger.i("打印两张图片的本地地址", picUrlFirst + picUrlSecond + "    00000");
                    break;


                default:
                    break;
            }

        }
    };

    AlertDialog guestDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("正在下载中...");
        guestDialog = builder.create();
        guestDialog.show();

        makeNewDir();
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        position = getIntent().getStringExtra("position");
        getListDetail();
        setContentView(R.layout.live_search_view_pic);
        back = (ImageView) findViewById(R.id.live_viewdetail_back);
        picOne = (ImageView) findViewById(R.id.live_view_pic1);
        picTwo = (ImageView) findViewById(R.id.live_view_pic2);
        commit = (Button) findViewById(R.id.live_view_commit);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("打印两张图片的本地地址", picUrlFirst + picUrlSecond + "    00000");
                /**
                 * picUrlFirst和picUrlSecond分别对应了本地存储的两张图片的地址
                 * 修改searchUrlIntent的跳转目的活动传值即可
                 */
//                Intent searchUrlIntent = new Intent(getActivity(), com.example.gongzhengtong.Activity.LivePicUrl.class);
//                searchUrlIntent.putExtra("pic_1", picUrlFirst);
//                searchUrlIntent.putExtra("pic_2", picUrlSecond);
//                startActivity(searchUrlIntent);
                Intent IdIdenIntent = new Intent(getActivity(), com.megvii.livenessproject.LoadingActivity.class);
                startActivityForResult(IdIdenIntent,LIVE2);
            }
        });

    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String dirName = "gongzhengtonglive";
        File appDir = new File(file, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getListDetail() {
        final String getInfoDetailUrl = ComParameter.URL + ComParameter.GETLISTDETAIL + position;
        Logger.i("打印请求业务详情的地址", getInfoDetailUrl);
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
                    Request request = new Request.Builder().url(getInfoDetailUrl).addHeader("Cookie", header).get().build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("打印每次详情返回的数据", responseData);
                    if (responseData.equals("{\"detail\":\"Not found.\"}")) {
                        makeToast("请求数据失败");
                    } else {
                        JSONObject object = JSONObject.fromObject(responseData);

                        picFirst = object.getJSONObject("data").getString("pic_1") + "";
                        picSecond = object.getJSONObject("data").getString("pic_2") + "";

                        mainHandler.sendEmptyMessage(1);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }

    public void downloadPic(String pic) {
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
                    Request request = new Request.Builder().url(getInfoDetailUrl).addHeader("Cookie", header).get().build();
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    //转化为bitmap
                    InputStream in = body.byteStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    mainHandler.sendEmptyMessage(2);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }

    private String saveBitmap(Bitmap bitmap) throws IOException {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        File file = new File(mFile + "/" + System.currentTimeMillis() + ".png");

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
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
        mFile = new File(SDCardRoot.getPath() + "/gongzhengtonglive/");
//        bitmapFile.getParentFile().mkdirs();// 创建文件夹
//
//        String path = "/sdcard/gongzhengtonglive/" + System.currentTimeMillis();
//        mFile = new File(path);
        if (!mFile.exists()) {
            mFile.mkdirs();
            Logger.i("55555555555555555555", mFile.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIVE2) {
            Logger.i("99999999999999999", data.getStringExtra("RealFilePath"));
            RealFilePath = data.getStringExtra("RealFilePath");
            delta = data.getStringExtra("delta");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("请稍候...");
            guestDialog = builder.create();
            guestDialog.show();
            verify();
//            Intent IdIdenIntent = new Intent(getActivity(), com.example.livenessproject.LoadingActivity.class);
//            startActivityForResult(IdIdenIntent,LIVE);
        }
    }


    private void verify() {
        PortraitImg = picUrlFirst;
        if (ComParameter.useStr(PortraitImg) && ComParameter.useStr(RealFilePath)) {
            File PortraitImgFile = new File(PortraitImg);
            File RealFilePathFile = new File(RealFilePath);
            if (PortraitImgFile == null || RealFilePathFile == null) {
                Logger.e("kkkkkkkkkkkkkkkkkkkkkkkkkkk", "文件为空");
                return;
            }
            String url = ComParameter.FACEID_URL+ComParameter.FACE_VERIFYL ;
            //构造上传请求，类似web表单
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("api_key", getResources().getString(R.string.api_key))
                    .addFormDataPart("api_secret", getResources().getString(R.string.api_secret))
                    .addFormDataPart("comparison_type", "0")//无源比对
                    .addFormDataPart("face_image_type", "meglive")
                    .addFormDataPart("uuid", ComParameter.getUUID())
//                    .addFormDataPart("idcard_number", idcard_number)
                    .addFormDataPart("image_ref1", PortraitImgFile.getName(), RequestBody.create(
                            null, PortraitImgFile))
                    .addFormDataPart("delta",delta)
                    .addFormDataPart("image_best", RealFilePathFile.getName(), RequestBody.create(
                            null, RealFilePathFile))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";" +
//                            "filename=\"another.dex\""), RequestBody.create
//                            (MediaType.parse("application/octet-stream"), file))
                    .build();
            Logger.i("打印最佳图片的地址",RealFilePathFile.getName()+"   00000");
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(requestBody).build();
            Callback callback = new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload","加载失败");
                    Logger.e("okhttp加载",e.toString());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    try {
                        String res = response.body().string();
                        Logger.i("身份证人脸对比", res);
                        Intent intent = new Intent(getContext(), TemporaryActivity.class);
                        intent.putExtra("best_mImg", picUrlFirst);
                        intent.putExtra("json", res);
//                        String name = data.getStringExtra("name");
//                        String idcard_number = data.getStringExtra("idcard_number");
                        intent.putExtra("name", getIntent().getStringExtra("name"));
                        intent.putExtra("idcard_number", getIntent().getStringExtra("idcard_number"));
                        intent.putExtra("best_img",RealFilePath);
                        intent.putExtra("type","活体检测+照片比对");
                        startActivity(intent);
                        guestDialog.dismiss();
                        finish();
                    } catch (Exception e) {
                        Logger.e("888888888888888888",e.toString());
                    }



                }
            };

            HttpUtils.enqueue(request, callback);

        }

    }

}
