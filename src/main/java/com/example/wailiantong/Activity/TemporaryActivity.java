package com.example.wailiantong.Activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.FaceData;
import com.example.wailiantong.Utills.Logger;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;

/**
 * Created by dell on 2017/7/2.
 */
public class TemporaryActivity extends BaseActivity {
    String s;
    private TextView mTextView, confidenceText;
    private CacheUtils cacheUtils;
    private String header, picFirst, picSecond;
    private String name, idNum, dateTime, confidence, type, bestImg, bestmImg;
    private int notary_office_id, mSalemanId;
    private StringBuilder summaryInfo;
    private Button button,commitBack;
    private RotateLoading rotateLoading;
    private RelativeLayout first, second,commitFinish;
    private android.os.Handler mainHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    break;
                case 2:
                    String test = bestmImg+"";
                    if (test.equals("null")){
                        com.example.wailiantong.Utills.Logger.i("身份证照片为空","00000");
                        mainHandler.sendEmptyMessage(4);
                    }else
                    sendImagesBack2(bestmImg);
                    break;
                case 3:
                    rotateLoading.stop();
                    second.setVisibility(View.GONE);
                    commitFinish.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    backTotal();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);


        cacheUtils = new CacheUtils(this, "UserInfo");
        header = cacheUtils.getValue("header", "");
        notary_office_id = cacheUtils.getInt("notary_office_id", 0);
        mSalemanId = cacheUtils.getInt("id", 0);
        type = getIntent().getStringExtra("type") + "";
        if (type.equals("活体检测+照片比对")) {
            com.example.wailiantong.Utills.Logger.i("本次是照片对比", "0000");
        } else type = "活体检测+身份证核身";
        name = getIntent().getStringExtra("name");
        idNum = getIntent().getStringExtra("idcard_number");
        bestImg = getIntent().getStringExtra("best_img");
        bestmImg = getIntent().getStringExtra("best_mImg");
        com.example.wailiantong.Utills.Logger.i("最佳图片", type + bestImg + bestmImg + "   00000");
        button = (Button) findViewById(R.id.photo_contrast_commit);
        first = (RelativeLayout) findViewById(R.id.temporary_first);
        second = (RelativeLayout) findViewById(R.id.temporary_second);
        commitFinish= (RelativeLayout) findViewById(R.id.commit_finish);
        commitBack= (Button) findViewById(R.id.commit_back);
        commitBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rotateLoading = (RotateLoading) findViewById(R.id.temporary_rotateloading);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                first.setVisibility(View.GONE);
//                second.setVisibility(View.VISIBLE);
//                rotateLoading.start();
//                sendImagesBack1(bestImg);
//            }
//        });
        confidenceText = (TextView) findViewById(R.id.temporary_confidence);
        try {
            initView();
        } catch (JSONException e) {
            Logger.e("jjjjjjjjjjjj", e.toString());
        }



//        first.setVisibility(View.GONE);
        second.setVisibility(View.VISIBLE);
        rotateLoading.start();
        sendImagesBack1(bestImg);
    }

    private void initView() throws JSONException {
        s = getIntent().getStringExtra("json");
        mTextView = (TextView) findViewById(R.id.temporary_text);

        if (type.equals("活体检测+照片比对")) {
            mTextView.setText("本次智能照片比对识别的置信度为：");
        } else             mTextView.setText("本次智能公安库核身的置信度为：");
        FaceData faceData = new Gson().fromJson(s, FaceData.class);
        StringBuffer sBuffer = new StringBuffer(s);
        com.example.wailiantong.Utills.Logger.i("打印facedata", s.toString() + "   00000");

        sBuffer.append("最佳图片地址：");
        sBuffer.append(bestImg + "          " + bestmImg);

        sBuffer.append(getIntent().getStringExtra("type"));
        sBuffer.append("姓名");
        sBuffer.append(getIntent().getStringExtra("name"));
        sBuffer.append("身份证号");
        sBuffer.append(getIntent().getStringExtra("idcard_number"));
        sBuffer.append("/n");
        sBuffer.append("置信度：");


        if (faceData.getResult_ref1() == null) {
            sBuffer.append(faceData.getResult_faceid().getConfidence() + "");
            confidence = faceData.getResult_faceid().getConfidence() + "";
            sBuffer.append("/n");
            sBuffer.append("置信度阈值：");
            sBuffer.append("勉强可以认为是同一个人：");
            sBuffer.append(faceData.getResult_faceid().getThresholds().getThousandth());
            sBuffer.append("/n");
            sBuffer.append("可以认为是同一个人：");
            sBuffer.append(faceData.getResult_faceid().getThresholds().getTenThouand());
//            mTextView.setText("置信度：" + faceData.getResult_faceid().getConfidence() +
//                    "/n" + "置信度阈值：" + faceData.getResult_faceid().getThresholds().getThousandth()
//                    + "/n" + "可以认为是同一个人：" + faceData.getResult_faceid().getThresholds().getTenThouand());
        } else {
            sBuffer.append(faceData.getResult_ref1().getConfidence() + "");
            confidence = faceData.getResult_ref1().getConfidence() + "";

            sBuffer.append("/n");
            sBuffer.append("置信度阈值：");
            sBuffer.append("勉强可以认为是同一个人：");
            sBuffer.append(faceData.getResult_ref1().getThresholds().getThousandth());
            sBuffer.append("/n");
            sBuffer.append("可以认为是同一个人：");
            sBuffer.append(faceData.getResult_ref1().getThresholds().getTenThouand());
//            mTextView.setText("置信度："+faceData.getResult_ref1().getConfidence()+
//                    "/n"+"置信度阈值："+faceData.getResult_ref1().getThresholds().getThousandth()
//                    +"/n"+"可以认为是同一个人："+faceData.getResult_ref1().getThresholds().getTenThouand());
        }
        // mTextView.setText(sBuffer);
        confidenceText.setText(confidence + "");
    }

    private void sendImagesBack1(String ImagesUrl) {


        com.example.wailiantong.Utills.Logger.i("这是在图片上传第一次交互的开始", "1111111111111111111111111");

        final MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File f = new File(ImagesUrl);
        if (f != null) {
            com.example.wailiantong.Utills.Logger.i("这是正在上传文件的打印", f.getAbsolutePath().toString() + "");
            mbody.addFormDataPart("file", f.getName(), RequestBody.create(ComParameter.mJPEG, f));
        }

        mbody.addFormDataPart("t", type);
        final String uploadUrl = ComParameter.URL + ComParameter.UPLOAD;

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(uploadUrl).addHeader("Cookie", header).post(mbody.build()).build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);
                    com.example.wailiantong.Utills.Logger.i("这是图片上传的数据返回", responseData + "111111111111");
                    com.example.wailiantong.Utills.Logger.i("这是图片上传的数据返回", object.getJSONObject("data").get("url").toString() + "111111111111");
                    picFirst = object.getJSONObject("data").get("url").toString();
                    com.example.wailiantong.Utills.Logger.i("这是在图片上传第一次交互的结束", "1111111111111111111111111");


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Message messageOne = new Message();
                messageOne.what = 2;
                com.example.wailiantong.Utills.Logger.i("hanlder1的数据", messageOne.toString() + "");
                mainHandler.sendMessage(messageOne);
            }
        }).start();
    }

    private void sendImagesBack2(String ImagesUrl) {


        com.example.wailiantong.Utills.Logger.i("这是在图片上传第一次交互的开始", "1111111111111111111111111");

        final MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File f = new File(ImagesUrl);
        if (f != null) {
            com.example.wailiantong.Utills.Logger.i("这是正在上传文件的打印", f.getAbsolutePath().toString() + "");
            mbody.addFormDataPart("file", f.getName(), RequestBody.create(ComParameter.mJPEG, f));
        }

        mbody.addFormDataPart("t", type);
        final String uploadUrl = ComParameter.URL + ComParameter.UPLOAD;

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(uploadUrl).addHeader("Cookie", header).post(mbody.build()).build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);
                    com.example.wailiantong.Utills.Logger.i("这是图片上传的数据返回", responseData + "111111111111");
                    com.example.wailiantong.Utills.Logger.i("这是图片上传的数据返回", object.getJSONObject("data").get("url").toString() + "111111111111");
                    picSecond = object.getJSONObject("data").get("url").toString();
                    com.example.wailiantong.Utills.Logger.i("这是在图片上传第一次交互的结束", "1111111111111111111111111");


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Message messageOne = new Message();
                messageOne.what = 4;
                com.example.wailiantong.Utills.Logger.i("hanlder1的数据", messageOne.toString() + "");
                mainHandler.sendMessage(messageOne);
            }
        }).start();
    }

    private void backTotal() {

        Map<String, String> first = new HashMap<String, String>();

        first.put("confidence", confidence);
        JSONObject firstobj = new JSONObject(first);
        String firstResult = firstobj.toString();
        com.example.wailiantong.Utills.Logger.i("看看置信度得到了吗？", firstResult);

        summaryInfo = new StringBuilder();
        summaryInfo.append(firstResult);
        String endString = summaryInfo.toString();

        com.example.wailiantong.Utills.Logger.i("这是在图片上传第三次交互的开始", "传上去的result字段为" + endString);
        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        //dateTime = "2017-06-27 07:54:07";
        com.example.wailiantong.Utills.Logger.i("这是在图片上传页面的cookie的打印", header + "");


        Map<String, String> params = new HashMap<String, String>();
        params.put("date_time", dateTime);
        params.put("type", type);
        params.put("party_name", name);
        params.put("certificate_num", idNum);
        params.put("pic_1", picFirst);
        params.put("pic_2", picSecond);
        params.put("consumption", "5");
        final JSONObject jsonObject = new JSONObject(params);

        try {
            JSONArray finResult = new JSONArray();
            finResult.put(endString + "");
            jsonObject.put("result", finResult);
            jsonObject.put("notary_office_id", notary_office_id);
            jsonObject.put("saleman_id", mSalemanId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String uploadUrl = ComParameter.URL + ComParameter.CREATE;

        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                //Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    com.example.wailiantong.Utills.Logger.i("这是打包上传的的数据是什么呢", jsonObject.toString() + "111111111111");
                    Request request = new Request.Builder().url(uploadUrl).addHeader("Cookie", header).post(requestbody).build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    com.example.wailiantong.Utills.Logger.i("这是打包上传的数据返回", responseData + "111111111111");
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);


                    Message messagefour = new Message();
                    messagefour.what = 3;
                    //Logger.i("hanlder3的数据", messageThree.toString() + "");
                    mainHandler.sendMessage(messagefour);
                    com.example.wailiantong.Utills.Logger.i("这是在图片上传第三次交互的结束", "1111111111111111111111111" + "这个时候上传的第几章图片呢?");


                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Looper.loop();

            }
        }).start();
    }

}
