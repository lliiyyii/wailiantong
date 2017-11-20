package com.example.wailiantong.View.imagebrowse.windowsView;

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

import com.example.wailiantong.Activity.BaseActivity;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.SendHttpUtils;
import com.example.wailiantong.AliTest.client.HttpUtil;
import com.example.wailiantong.AliTest.client.constant.Constants;
import com.example.wailiantong.AliTest.demo.DisplayMessageActivity;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.AliTest.demo.DemoMainActivity.EXTRA_MESSAGE;

/**
 * Created by weike on 2017/7/2.
 */

public class CardTransmit extends BaseActivity {

    private ImageView back;
    private List<String> nowImages;//需要上传的图片数组
    private String mType, dateTime, partyName, certificateNum, mResultFirst, mResultSecond, picFirst, picSencond;   //需要向后台汇总上传的数据
    private int notary_office_id, mSalemanId;
    private String header;//请求头
    private CacheUtils cacheUtils;
    private StringBuilder backInfo;
    private StringBuilder summaryInfo;
    private int i;//记录现在是第几张图片正在上传
    private Boolean lastSend;
    private RotateLoading loading;
    private RelativeLayout mainLayout;
    private TextView nameTx,idTx;
    private Button ok;
    private TextView text;
    private SendHttpUtils utils=new SendHttpUtils();
    private android.os.Handler mainHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    Logger.i("图片的路径",nowImages.get(i));
                    if ((i & 1) != 1) {
                        //偶数则为上传正面  因为第一次上传为0
                        Logger.i("识别的是正面", "3333333333333");
                        sendFaceHttpPostBytes(utils.getbitmap(nowImages.get(i)));
                    } else {
                        //是奇数则为背面
                        Logger.i("识别的是背面", "3333333333333");
                        sendBackHttpPostBytes(utils.getbitmap(nowImages.get(i)));
                    }
                    break;
                case 2:
                    sendCard(nowImages, lastSend);
                    break;
                case 3:
                    backTotal(nowImages.get(i));
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

        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        notary_office_id=cacheUtils.getInt("notary_office_id",0);
        mSalemanId = cacheUtils.getInt("id",0);
        nowImages = getIntent().getStringArrayListExtra("nowImages");
        mType = getIntent().getStringExtra("mType");
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


        Logger.i("这是在图片上传第一次交互的开始", "1111111111111111111111111");

        final MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File f = new File(ImagesUrl);
        if (f != null) {
            Logger.i("这是正在上传文件的打印", f.getAbsolutePath().toString() + "");
            mbody.addFormDataPart("file", f.getName(), RequestBody.create(ComParameter.mJPEG, f));
        }

        mbody.addFormDataPart("t", mType);
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
                    Logger.i("这是图片上传的数据返回", responseData + "111111111111");
                    Logger.i("这是图片上传的数据返回", object.getJSONObject("data").get("url").toString() + "111111111111");

                    Logger.i("这个时候保存的是第几次的图片呢？",i+""+"     0000000");
                    if ((i & 1) != 1) {
                        picFirst = object.getJSONObject("data").get("url").toString();
                    } else {
                        //是奇数则为背面
                        picSencond = object.getJSONObject("data").get("url").toString();
                    }
                    Logger.i("这是在图片上传第一次交互的结束", "1111111111111111111111111");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message messageOne = new Message();
                messageOne.what = 1;
                Logger.i("hanlder1的数据", messageOne.toString() + "");
                mainHandler.sendMessage(messageOne);
            }
        }).start();
    }


    public void getInfoIdCardFace(net.sf.json.JSONObject mObject) {
        partyName = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("name").toString();
        certificateNum = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("num").toString();
        mResultFirst = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").toString();
    }

    public void getInfoIdCardBack(net.sf.json.JSONObject mObject) {

        Logger.i("这是身份证背面上传返回的信息", mObject.toString() + "222222222222222");

        mResultSecond = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").toString();
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

    public void sendBackHttpPostBytes(final Bitmap photo) {   //识别背面


        final Intent intent = new Intent(this, DisplayMessageActivity.class);
        Logger.i("这是在阿里云发送背面交互的开始", "1111111111111111111111111");
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
                    configObj.put("side", "back");
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
                        // Logger.i("打印啦啦啦啦", object.getJSONArray("output").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("name").toString());

                        Logger.i("阿里云返回的数据背面", object.toString() + "");

                        Logger.i("这是在阿里云发送背面交互的开始", "1111111111111111111111111");
                        getInfoIdCardBack(object);

                        Message messageThree = new Message();
                        messageThree.what = 3;
                        Logger.i("hanlder3的数据", messageThree.toString() + "");
                        mainHandler.sendMessage(messageThree);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }


    private void backTotal(String ImagesUrl) {


        summaryInfo = new StringBuilder();
        summaryInfo.append(mResultFirst);
        summaryInfo.append(mResultSecond);
        String endString = summaryInfo.toString();
        Logger.i("这是在图片上传第三次交互的开始", "传上去的result字段为" + endString);
        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
       // dateTime = "2017-06-27 07:54:07";
        Logger.i("这是在图片上传页面的cookie的打印", header + "");
        Map<String, String> params = new HashMap<String, String>();

        params.put("date_time", dateTime);
        params.put("type", mType);
        params.put("party_name", partyName);
        params.put("certificate_num", certificateNum);
        params.put("notary_office_id","8");
        params.put("pic_1", picFirst);
        params.put("pic_2", picSencond);
        params.put("consumption", "5");
        final JSONObject jsonObject = new JSONObject(params);

        try {
            JSONArray finResult = new JSONArray();
            finResult.put(mResultFirst+"");
            finResult.put(mResultSecond+"");
            jsonObject.put("result",finResult);
            jsonObject.put("notary_office_id",notary_office_id);
            jsonObject.put("saleman_id",mSalemanId);
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
                    Logger.i("这是打包上传的的数据是什么呢", jsonObject.toString() + "111111111111");
                    Request request = new Request.Builder().url(uploadUrl).addHeader("Cookie", header).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("这是打包上传的数据返回", responseData + "111111111111");
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);
                    i = i + 1;
                    lastSend = true;
                    Message messagefour = new Message();
                    messagefour.what = 4;
                    //Logger.i("hanlder3的数据", messageThree.toString() + "");
                    mainHandler.sendMessage(messagefour);
                    Logger.i("这是在图片上传第三次交互的结束", "1111111111111111111111111" + "这个时候上传的第几章图片呢?" + i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Looper.loop();

            }
        }).start();
    }


}
