package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.wailiantong.Adapter.RecyclerRecverAdapter;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.Model.RecverModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.titleMannager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;

/**
 * Created by 蔚克 on 2017/11/20.
 */

public class OrderRecver extends BaseActivity {
    private String orderId;
    private RecyclerView recyclerView;
    private List<RecverModel> recverList = new ArrayList<>();
    private List<RecverModel> copyList;
    private RecyclerRecverAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String userId;
    private CacheUtils cacheUtils;

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_order_recver);
        titleMannager title = new titleMannager(OrderRecver.this);
        title.setTitle("查看接收者");
        title.titleSetOnClickListener(this);
        orderId = getIntent().getStringExtra("orderId");
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        userId = cacheUtils.getValue("userId", "");
        initView();
        getList(orderId);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.order_recver_recy);

        adapter = new RecyclerRecverAdapter(recverList);
        adapter.setmOnItemClickListener(new RecyclerRecverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent messageIntent = new Intent(getActivity(), OrderMessage.class);
                int mId = recverList.get(position).getRcv_cus_id();

                messageIntent.putExtra("userId", userId);
                messageIntent.putExtra("recverId", mId + "");
                messageIntent.putExtra("orderId", orderId);
                messageIntent.putExtra("type", "0");

                Logger.i("打印聊天发送信息",userId+mId+orderId);

                startActivity(messageIntent);
            }
        });


        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void getList(String order) {

        final String URL = ComParameter.URL + ComParameter.GETRECVER;
        Logger.i("开始请求列表", "   000000000000000");
        Map<String, String> params = new HashMap<String, String>();
        params.put("order_id", order);
        final org.json.JSONObject jsonObject = new org.json.JSONObject(params);
        Logger.i("地址不为空", "33333");

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
                    Request request = new Request.Builder().url(URL + "").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("打印请求返回", responseData + "");
                    JSONObject object = JSONObject.fromObject(responseData);
                    String data = object.getJSONArray("data").toString();
                    Logger.i("打印列表信息", data + "");
                    if (object.getInt("status") == 1) {
                        Gson gson = new Gson();
                        copyList = new ArrayList<>();
                        copyList = gson.fromJson(data, new TypeToken<List<RecverModel>>() {
                        }.getType());
                        recverList.clear();
                        recverList.addAll(copyList);
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
