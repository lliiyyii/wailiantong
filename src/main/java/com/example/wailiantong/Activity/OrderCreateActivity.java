package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.ToastUtils;
import com.example.wailiantong.Utills.titleMannager;

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
 * Created by weike on 2017/11/13.
 */

public class OrderCreateActivity extends BaseActivity {
    private String title = "", price = "", deadline = "", content = "", sendPersonId;
    private String imgurl = "";
    private CacheUtils cacheUtils;
    private EditText titleEdit, priceEdit, contentEdit;
    private Button commitBtn;
    private ImageView getImgBtn, getDateBtn;
    private TextView dateText;
    public static int CALENDAR = 101;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dateText.setText(deadline);
                    break;

            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_create_new_order);
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        titleMannager title = new titleMannager(OrderCreateActivity.this);
        title.setTitle("创建新订单");
        title.titleSetOnClickListener(this);
        sendPersonId = cacheUtils.getValue("userId", "");
        initView();
    }

    private void initView() {
        titleEdit = (EditText) findViewById(R.id.create_title_edit);
        priceEdit = (EditText) findViewById(R.id.create_price_edit);
        contentEdit = (EditText) findViewById(R.id.create_content_edit);
        commitBtn = (Button) findViewById(R.id.create_commit_btn);
        getImgBtn = (ImageView) findViewById(R.id.create_upload_img);
        getDateBtn = (ImageView) findViewById(R.id.create_date_select);
        dateText = (TextView) findViewById(R.id.create_date_text);
        getDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IdIdenIntent = new Intent(getActivity(), CalendarActivity.class);
                IdIdenIntent.putExtra("intent_data", 1);
                startActivityForResult(IdIdenIntent, CALENDAR);

            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleEdit.getText().toString();
                price = priceEdit.getText().toString();
                content = contentEdit.getText().toString();
                if (title.equals("")) ToastUtils.makeToast("标题不能为空");
                else if (price.equals("")) ToastUtils.makeToast("价格不能为空");
                else if (content.equals("")) content = "这家伙很懒，什么都没有写";
                else if (deadline.equals("")) ToastUtils.makeToast("您还没有选择日期");
                else {
                    createOrder(title, imgurl, price, content, sendPersonId, deadline);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALENDAR && resultCode == -1) {
            deadline = data.getStringExtra("data");
            handler.sendEmptyMessage(0);
        }
    }

    public void createOrder(String title, String imgurl, String price, String content, String sendId, String deadline) {
        final String url = ComParameter.URL + ComParameter.CREATE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", title);
        params.put("img_url", imgurl);
        params.put("price", price);
        params.put("content", content);
        params.put("send_id", sendId);
        params.put("all_cus", "5");
        params.put("deadline", deadline);
        final JSONObject jsonObject = new JSONObject(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).post(requestbody).build();
                    Logger.i("打印订单发布发送内容", requestbody.toString() + "");
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);

                    if (mobject.getInt("status") == 1) {
                        makeToast("订单发布成功！");
                        Logger.i("打印登录返回的日志", mobject.toString());
                        getActivity().finish();


                    } else {
                        makeToast(mobject.getJSONObject("data").getString("msg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }
}
