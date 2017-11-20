package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wailiantong.Adapter.RecyclerAllOrderAdapter;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.Model.OrderModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
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
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by 蔚克 on 2017/7/3.
 */

public class SearchByname extends BaseActivity {
    private EditText search;
    RecyclerView recyclerView;
    private String searchContent="";
    ImageView searchCommit;
    private List<OrderModel> orderList = new ArrayList<>();
    private List<OrderModel> copyList;
    private RecyclerAllOrderAdapter adapter;
    private CacheUtils cacheUtils;
    private String userId,nextUrl="0";
    private String url = ComParameter.URL + ComParameter.SEARCHBYNAME;

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    adapter.notifyDataSetChanged();
                    makeToast("搜索完成");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seach_detail_layout);
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        userId = cacheUtils.getValue("userId", "");

        initView();
//        initListen();
    }

    public void initView() {
        search = (EditText) findViewById(R.id.search_detail_edit);
        recyclerView = (RecyclerView) findViewById(R.id.search_detail_recyclerview);
        searchCommit = (ImageView) findViewById(R.id.search_detail_commit);


        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutmanager);
        adapter = new RecyclerAllOrderAdapter(orderList);
        setItemClick(adapter);
        recyclerView.setAdapter(adapter);

        initListen();

    }
    public void initListen() {
        searchCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent = search.getText().toString();
                if (searchContent.equals("")) makeToast("请输入搜索内容");
                else searchList(userId,searchContent,nextUrl);


            }
        });
    }

    public void setItemClick(RecyclerAllOrderAdapter myAdapter) {
        myAdapter.setmOnItemClickListener(new RecyclerAllOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent BusinessDetailIntent = new Intent(getActivity(), OrderDetail.class);
                int mId = orderList.get(position).getId();
                BusinessDetailIntent.putExtra("Id", mId + "");
                startActivity(BusinessDetailIntent);
            }
        });
    }

    public void searchList(String userId, String keywords, String next) {
        final String URL = url;
        Logger.i("开始请求列表", "   000000000000000");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("keywords", keywords);
        params.put("next", next);
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
                    String data = object.getJSONObject("data").getJSONArray("list").toString();
                    Logger.i("打印列表信息", data + "");
//                        nextList = object.getInt("next") + "";
                    if (object.getInt("status") == 1) {
                        Gson gson = new Gson();
                        copyList = new ArrayList<>();
                        copyList = gson.fromJson(data, new TypeToken<List<OrderModel>>() {
                        }.getType());
                        orderList.clear();
                        orderList.addAll(copyList);
                        Logger.i("打印modellist的信息", data.toString() + "1111" + orderList.get(0).getContent().toString());
                        mHandler.sendEmptyMessage(1);
                    } else {
                        Toast.makeText(getContext(), "获取列表信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();


    }

}
