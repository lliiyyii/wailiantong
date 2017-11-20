package com.example.wailiantong.View.imagebrowse.windowsView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wailiantong.Activity.BaseActivity;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.AliTest.client.Base64Encoder;
import com.example.wailiantong.AliTest.client.HttpUtil;
import com.example.wailiantong.AliTest.client.constant.Constants;
import com.example.wailiantong.AliTest.demo.DisplayMessageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class HouseTransmit extends BaseActivity {

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
    private Button ok;
    private TextView text;
    private android.os.Handler mainHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    sendHouseHttpPostBytes(getbitmap(nowImages.get(i)));

                    break;
                case 2:
                    backTotal(nowImages.get(i));
                    break;
                case 3:
                    sendHouse(nowImages, lastSend);
                    break;

                case 4:
                    sendHouse(nowImages, lastSend);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastSend = true;
        setContentView(R.layout.sendbusiness_main_layout);

        back = (ImageView) findViewById(R.id.sendbusiness_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text = (TextView) findViewById(R.id.sendbusiness_message);
        ok = (Button) findViewById(R.id.sendbusiness_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        notary_office_id=cacheUtils.getInt("notary_office_id",0);
        mSalemanId = cacheUtils.getInt("id",0);

        nowImages = getIntent().getStringArrayListExtra("nowImages");
        mType = getIntent().getStringExtra("mType");
        i = 0;//从第一张图片开始传

        sendHouse(nowImages, lastSend);
    }


    public void sendHouse(List<String> images, Boolean isTrue) {
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
            text.setText("上传完成，请点击确定退出！");
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


                    picFirst = object.getJSONObject("data").get("url").toString();


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


    public Bitmap getbitmap(String jpgUrl) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(jpgUrl);
        } catch (FileNotFoundException e) {
            Logger.i("文件没有找到", "1111111111111111111");
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        return bitmap;
    }


    public JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public String bitmaptoString(Bitmap bitmap) {


        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bStream);

        byte[] bytes = bStream.toByteArray();
        Base64Encoder base = new Base64Encoder();
        string = base.encode(bytes); //

        //  string = Base64.encodeToString(bytes, Base64.DEFAULT);

        return string;

    }

    public void getInfoBusiness(net.sf.json.JSONObject mObject) {

        partyName = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("name").toString();
        certificateNum = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").get("reg_num").toString();
        mResultFirst = mObject.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getJSONObject("dataValue").toString();

    }

    public void sendHouseHttpPostBytes(final Bitmap mbitmap) {

        final Intent intent = new Intent(this, DisplayMessageActivity.class);
        Logger.i("这是在阿里云发送营业执照的开始", "1111111111111111111111111");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String getPath = "/rest/160601/ocr/ocr_general.json";
//
//                //对图片进行压缩
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                mbitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
//                ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//                Bitmap mybitmap = BitmapFactory.decodeStream(isBm, null, null);

                String imgBase64 = bitmaptoString(mbitmap);
                // 拼装请求body的json字符串
                JSONObject requestObj = new JSONObject();
                try {
                    JSONObject obj = new JSONObject();
                    JSONArray inputArray = new JSONArray();
                    obj.put("image", getParam(50, imgBase64));
                    inputArray.put(obj);
                    requestObj.put("inputs", inputArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String content = requestObj.toString();
                Logger.i("这是户口本识别上传的信息", content + "1111111111111111");

                HttpUtil.getInstance().httpPostBytesToHouse(getPath, null, null, content.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        intent.putExtra(EXTRA_MESSAGE, e.getMessage());
                        startActivity(intent);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        backInfo = new StringBuilder();
                        backInfo.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
                        Logger.i("阿里云户口本识别返回的数据", backInfo.toString() + "22222222222222222");

                        net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(backInfo.toString());
//                        getInfoBusiness(object);

                        Logger.i("这是在阿里云发送户口本的结束", "1111111111111111111111111");
//                        Message messageTwo = new Message();
//                        messageTwo.what = 2;
//                        Logger.i("hanlder2的数据", messageTwo.toString() + "");
//                        mainHandler.sendMessage(messageTwo);
//
//                        Logger.i("阿里云营业执照识别返回的数据", object.getJSONObject("output").toString() + "");
                    }
                });

            }
        };
        new Thread(runnable).start();
    }

    private void backTotal(String ImagesUrl) {
        summaryInfo = new StringBuilder();
        summaryInfo.append(mResultFirst);
        String endString = summaryInfo.toString();
        Logger.i("这是在图片上传第三次交互的开始", "传上去的result字段为" + endString);
        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        //dateTime = "2017-06-27 07:54:07";
        Logger.i("这是在图片上传页面的cookie的打印", header + "");
        Map<String, String> params = new HashMap<String, String>();

        params.put("date_time", dateTime);
        params.put("type", mType);
        params.put("party_name", partyName);
        params.put("certificate_num", certificateNum);
        params.put("result", endString);
        params.put("pic_1", picFirst);
        params.put("consumption", "5");
        final JSONObject jsonObject = new JSONObject(params);
        try {
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
                    messagefour.what = 3;
                    //Logger.i("hanlder3的数据", messageThree.toString() + "");
                    mainHandler.sendMessage(messagefour);
                    Logger.i("这是在图片上传第三次交互的结束", "1111111111111111111111111" + "这个时候上传的第几章图片呢?" + i);

                    i = i + 1;//第i张图片上传完成


                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Looper.loop();

            }
        }).start();
    }


}
