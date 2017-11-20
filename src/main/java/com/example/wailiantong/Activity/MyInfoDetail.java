package com.example.wailiantong.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by weike on 2017/6/12.
 */

public class MyInfoDetail extends BaseActivity {
    private Button save;
    private TextView name;
    private TextView businessId, phoneNum,personName;
    private ImageView man, woman, back;
    private int gender = 1;//默认1表示男性
    private String mName, mGender, mBusinessId, mPhoneNum, mId;
    private static String URL = ComParameter.URL + ComParameter.CHANGEINFO;
    private CacheUtils cacheUtils;
    private String header,mainName;
private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);


    }
};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_my_info_layout);
        cacheUtils=new CacheUtils(getContext(),"UserInfo");
        initView();
        initListen();

    }

    public void initData() {
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        mName = cacheUtils.getValue("name", "未命名");
        mGender = cacheUtils.getValue("gender", "男");
        mBusinessId = cacheUtils.getValue("license_num", "");
        mPhoneNum = cacheUtils.getValue("phone", "");
        mId=cacheUtils.getValue("id","")+"";



    }

    public void initView() {
        mPhoneNum = cacheUtils.getValue("phone", "");
        mBusinessId=cacheUtils.getValue("companyCode","");
        mainName=cacheUtils.getValue("companyName","");
        mName=cacheUtils.getValue("partyName","");
        Logger.i("打印个人信息",mPhoneNum+"   "+mBusinessId+"   "+mainName);
        personName= (TextView) findViewById(R.id.myinfo_detail_mainname);
        name = (TextView) findViewById(R.id.myinfo_detail_name);
        businessId = (TextView) findViewById(R.id.myinfo_detail_business);
        phoneNum = (TextView) findViewById(R.id.myinfo_detail_phonenum);
        save = (Button) findViewById(R.id.myinfo_save);
        back = (ImageView) findViewById(R.id.zsy_my_info_back);
        name.setText(mName);
        businessId.setText(mBusinessId);
        phoneNum.setText(mPhoneNum);
        personName.setText(mainName);


        handler.sendEmptyMessage(1);


    }

    public void initListen() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }




    public void saveOnline(String murl) {
        String sex;
        if (gender == 1) {
            sex = "男";
        } else sex = "女";


        final String url = murl + mId + "/";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name.getText().toString());
        params.put("gender", sex);
        final JSONObject jsonObject = new JSONObject(params);


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
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).addHeader("Cookie", header).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);
                    if (mobject.getInt("status") == 1) {
                        makeToast("信息保存成功");


                    } else {
                        makeToast("保存失败");

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }

}
