package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.Model.OrderModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

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
 * Created by 蔚克 on 2017/11/10.
 */

public class OrderDetail extends BaseActivity {
    private ImageView messageBtn;
    private TextView titleTx, personTx, gradeTx, contentTx, priceTx, deadlineTx;
    private String id, userId;
    private OrderModel model;
    private Button commitBtn;
    private String type = "1";

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    titleTx.setText(model.getTitle());
                    priceTx.setText(model.getPrice());
                    personTx.setText(model.getPerson());
                    gradeTx.setText(model.getGrade());
                    contentTx.setText(model.getContent());
                    deadlineTx.setText(model.getDeadline());
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getIntent().getStringExtra("Id");
        type = getIntent().getStringExtra("type");
        CacheUtils cacheUtils = new CacheUtils(getContext(), "UserInfo");
        userId = cacheUtils.getValue("userId", "");
        setContentView(R.layout.zsy_order_detail_layout);
        initView();
        getDetail(userId, id);
    }

    public void initView() {
        titleTx = (TextView) findViewById(R.id.order_title_text);
        personTx = (TextView) findViewById(R.id.order_person_text);
        gradeTx = (TextView) findViewById(R.id.order_person_grade_text);
        deadlineTx = (TextView) findViewById(R.id.order_deadline_text);
        contentTx = (TextView) findViewById(R.id.order_content_text);
        priceTx = (TextView) findViewById(R.id.order_price_text);
        commitBtn = (Button) findViewById(R.id.order_commit);

        messageBtn = (ImageView) findViewById(R.id.order_message_btn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(getContext(), OrderMessage.class);
                messageIntent.putExtra("userID", userId);
                messageIntent.putExtra("orderID", id);
                messageIntent.putExtra("type","1");
                startActivity(messageIntent);

            }
        });
        if (type.equals("0")) {
            //表明用户创建者查看
            commitBtn.setText("查看接受者");
            messageBtn.setClickable(false);
            commitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent = new Intent(getContext(), OrderRecver.class);
                    viewIntent.putExtra("orderId", id);
                    startActivity(viewIntent);
                }
            });
        } else {
            commitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commitOrder(userId, id);
                }
            });
        }


    }

    public void commitOrder(String userId, String orderId) {
        final String Url = ComParameter.URL + ComParameter.COMMITORDER;
        Logger.i("开始请求列表", "   000000000000000");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        final org.json.JSONObject jsonObject = new org.json.JSONObject(params);


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
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                    Logger.i("打印发送的信息", jsonObject.toString() + "");
                    Request request = new Request.Builder().url(Url + "").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("打印请求返回", responseData + "");
                    JSONObject object = JSONObject.fromObject(responseData);
                    Logger.i("打印列表信息", object.toString() + "");
                    if (object.getInt("status") == 1) {
                        Toast.makeText(getContext(), "接受订单成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "接受订单失败", Toast.LENGTH_SHORT).show();
                    }
//                        hideHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();


    }

    public void getDetail(String userId, String orderId) {
        final String Url = ComParameter.URL + ComParameter.GETDETAIL;
        Logger.i("开始请求列表", "   000000000000000");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        final org.json.JSONObject jsonObject = new org.json.JSONObject(params);


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
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                    Logger.i("打印发送的信息", jsonObject.toString() + "");
                    Request request = new Request.Builder().url(Url + "").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("打印请求返回", responseData + "");
                    JSONObject object = JSONObject.fromObject(responseData);
                    String data = object.getJSONObject("data").toString();
                    Logger.i("打印列表信息", data + "");
                    if (object.getInt("status") != 0) {
                        Gson gson = new Gson();
                        model = gson.fromJson(data, new TypeToken<OrderModel>() {
                        }.getType());
                        mHandler.sendEmptyMessage(1);
                    } else {
                        Toast.makeText(getContext(), "获取列表信息失败", Toast.LENGTH_SHORT).show();
                    }
//                        hideHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();


    }

}
