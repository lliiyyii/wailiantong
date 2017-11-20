package com.example.wailiantong.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.AliTest.client.HttpUtil;
import com.example.wailiantong.AliTest.client.constant.Constants;
import com.example.wailiantong.AliTest.demo.DisplayMessageActivity;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.SendHttpUtils;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.wailiantong.AliTest.demo.DemoMainActivity.EXTRA_MESSAGE;

/**
 * Created by weike on 2017/7/2.
 */

public class ActiveImgTransmit extends BaseActivity {

    private ImageView back;
    private List<String> nowImages;//需要上传的图片数组
    private String mType, dateTime, partyName, certificateNum, mResultFirst, mResultSecond, picFirst, picSencond;   //需要向后台汇总上传的数据
    private int notary_office_id, mSalemanId;
    private String header;//请求头
    private RelativeLayout mainLayout;
    private TextView nameTx,idTx;
    private CacheUtils cacheUtils;
    private StringBuilder backInfo;
    private StringBuilder summaryInfo;
    private int i;//记录现在是第几张图片正在上传
    private Boolean lastSend;
    private RotateLoading loading;
    private Button ok;
    private TextView text;
    private SendHttpUtils utils=new SendHttpUtils();
    private android.os.Handler mainHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                        sendFaceHttpPostBytes(utils.getbitmap(nowImages.get(i)));

                    break;
                case 2:
                    sendCard(nowImages, lastSend);
                    break;
                case 3:

                    break;

                case 4:
                    sendCard(nowImages, lastSend);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastSend = true;
        setContentView(R.layout.sendidcard_main_layout);
        mainLayout= (RelativeLayout) findViewById(R.id.sendidcard_mainlayout);
        nameTx= (TextView) findViewById(R.id.sendidcard_name);
        idTx= (TextView) findViewById(R.id.sendidcard_id);
        back = (ImageView) findViewById(R.id.sendidcard_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text = (TextView) findViewById(R.id.sendidcard_message);
        ok = (Button) findViewById(R.id.sendidcard_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loading= (RotateLoading) findViewById(R.id.sendcard_loading);
        loading.start();
        nowImages = getIntent().getStringArrayListExtra("nowImages");
        cacheUtils = new CacheUtils(getContext(), "UserInfo");

        i = 0;//从第一张图片开始传

        sendCard(nowImages, lastSend);
    }


    public void sendCard(List<String> images, Boolean isTrue) {
        Logger.i("新的一轮上传开始看一下i是多少", i + "是这个");
        if (i < images.size()) //判断是否上传完了所有图片
        {
            if (isTrue == true) {
                sendImagesBack(images.get(i));
                lastSend = false;
                //开始本次发送 置标志位false
            } else {
                Logger.i("上次发送还没有完成", "222222222");
            }
        } else {
            loading.stop();
            text.setText("点击确定返回");
            idTx.setText(certificateNum+"");
            nameTx.setText(partyName);
            cacheUtils.putValue("certificateNum",certificateNum);
            cacheUtils.putValue("partyName",partyName);
            mainLayout.setVisibility(View.VISIBLE);
            ok.setVisibility(View.VISIBLE);
        }


    }

    private void sendImagesBack(String ImagesUrl) {
        Message messageOne = new Message();
        messageOne.what = 1;
        Logger.i("hanlder1的数据", messageOne.toString() + "");
        mainHandler.sendMessage(messageOne);

//        Logger.i("这是在图片上传第一次交互的开始", "1111111111111111111111111");
//
//        final MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        File f = new File(ImagesUrl);
//        if (f != null) {
//            Logger.i("这是正在上传文件的打印", f.getAbsolutePath().toString() + "");
//            mbody.addFormDataPart("file", f.getName(), RequestBody.create(ComParameter.mJPEG, f));
//        }
//
//        mbody.addFormDataPart("t", mType);
//        final String uploadUrl = ComParameter.URL + ComParameter.UPLOAD;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder().url(uploadUrl).addHeader("Cookie", header).post(mbody.build()).build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);
//                    Logger.i("这是图片上传的数据返回", responseData + "111111111111");
//                    Logger.i("这是图片上传的数据返回", object.getJSONObject("data").get("url").toString() + "111111111111");
//
//                    Logger.i("这个时候保存的是第几次的图片呢？",i+""+"     0000000");
//                    if ((i & 1) != 1) {
//                        picFirst = object.getJSONObject("data").get("url").toString();
//                    } else {
//                        //是奇数则为背面
//                        picSencond = object.getJSONObject("data").get("url").toString();
//                    }
//                    Logger.i("这是在图片上传第一次交互的结束", "1111111111111111111111111");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Message messageOne = new Message();
//                messageOne.what = 1;
//                Logger.i("hanlder1的数据", messageOne.toString() + "");
//                mainHandler.sendMessage(messageOne);
//            }
//        }).start();
    }


    public void getInfoIdCardFace(net.sf.json.JSONObject mObject) {
        partyName = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("name").toString();
        certificateNum = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("num").toString();
        mResultFirst = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").toString();
    }


    public void sendFaceHttpPostBytes(final Bitmap photo) {   //识别正面

        Logger.i("这是在阿里云发送正面交互的开始", "1111111111111111111111111");

        final Intent intent = new Intent(this, DisplayMessageActivity.class);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String getPath = "/rest/160601/ocr/ocr_idcard.json";
                String imgBase64 = utils.bitmaptoString(photo);
                // 拼装请求body的json字符串
                JSONObject requestObj = new JSONObject();
                try {
                    JSONObject configObj = new JSONObject();
                    JSONObject obj = new JSONObject();
                    JSONArray inputArray = new JSONArray();
                    configObj.put("side", "face");
                    obj.put("image", utils.getParam(50, imgBase64));
                    obj.put("configure", utils.getParam(50, configObj.toString()));
                    inputArray.put(obj);
                    requestObj.put("inputs", inputArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String content = requestObj.toString();
                HttpUtil.getInstance().httpPostBytes(getPath, null, null, content.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        intent.putExtra(EXTRA_MESSAGE, e.getMessage());
                        startActivity(intent);

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        backInfo = new StringBuilder();
                        backInfo.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));


                        net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(backInfo.toString());
                        Logger.i("打印啦啦啦啦", object.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("name").toString() + "1111111111111");
//                        getJSONObject("output").getJSONArray(0).getJSONObject("outputValue").getJSONObject("dataValue").get("name").toString()
                        Logger.i("阿里云返回的数据正面", object.toString() + "");
                        getInfoIdCardFace(object);

                        i++;   //正面图片上传完成后不上传后台 转为上传第二张图片
                        lastSend = true;

                        Message messageTwo = new Message();
                        messageTwo.what = 2;
                        Logger.i("hanlder2的数据", messageTwo.toString() + "");
                        mainHandler.sendMessage(messageTwo);

                        Logger.i("这是在阿里云发送正面交互的结束", "1111111111111111111111111");
                    }
                });
            }
        };
        new Thread(runnable).start();
//        Logger.i("判断是否请求成功了呢?", String.valueOf(isSuccess.get(0))+"");

    }





}
