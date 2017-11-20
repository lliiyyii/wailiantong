package com.example.wailiantong.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wailiantong.Adapter.RecyclerOrderMessageAdapter;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.Model.OrderMessageModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.ToastUtils;
import com.example.wailiantong.Utills.titleMannager;
import com.example.wailiantong.View.RefreshLayout;
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
 * Created by 蔚克 on 2017/11/12.
 */

public class OrderMessage extends BaseActivity {
    private RecyclerView recyclerView;
    private RefreshLayout recyclerView_refresh;
    private EditText edit;
    private Button commit;
    private List<OrderMessageModel> orderMessageList = new ArrayList<>();
    private List<OrderMessageModel> copylist;
    private OrderMessageModel mModel = new OrderMessageModel("2017-10-12 12:10:30", "小郡肝串串", "第一次聊天，很高兴遇到你，希望合作愉快");
    private RecyclerOrderMessageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String userID, orderID, content,type,recverId="";

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getMsg(userID, orderID);
                    hideHandler.sendEmptyMessage(0);
                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    if (orderMessageList.size()!=0){
                        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                    }

                    hideHandler.sendEmptyMessage(0);
                    break;

            }
        }
    };

    private Handler hideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideRefresh();
        }
    };

    private void hideRefresh() {
        try {
            if (recyclerView_refresh.isRefreshing())
                recyclerView_refresh.setRefreshing(false);
            if (recyclerView_refresh.isLoading)
                recyclerView_refresh.setLoading(false);
        } catch (Exception e) {
            Logger.e("iiiiiiiiiiiiiiiii", e.toString());
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_order_message_board);
        userID = getIntent().getStringExtra("userID");
        orderID = getIntent().getStringExtra("orderID");
        type=getIntent().getStringExtra("type");  //0代表是查看自己的订单  1代表查看别人订单
        if (type=="0"){
            recverId=getIntent().getStringExtra("recverId");
        }

        initView();
        Logger.i("打印聊天发送参数",userID+orderID+type+recverId);
        getMsg(userID, orderID);
    }

    public void initView() {
        titleMannager title = new titleMannager(this);
        title.setTitle("留言板");
        title.titleSetOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.order_message_recy);
        adapter = new RecyclerOrderMessageAdapter(orderMessageList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
//        layoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(adapter);

        recyclerView_refresh = (RefreshLayout) findViewById(R.id.order_message_refresh);
        initRefreshLayout();

        edit = (EditText) findViewById(R.id.order_message_edit);
        commit = (Button) findViewById(R.id.order_message_commit);


        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = edit.getText().toString();
                if (content.isEmpty()) ToastUtils.makeToast("您输入的内容为空");
                else {
                    sendMsg(userID, orderID, content);
                    edit.setText("");
                    edit.clearFocus();
                    recyclerView.setFocusable(true);
                    recyclerView.requestFocus();
                    hideKeyboard();

                }

            }
        });


    }
    protected void hideKeyboard() {
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    public void initRefreshLayout() {
        recyclerView_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        recyclerView_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新时候重载列表
                getMsg(userID, orderID);
            }
        });
        // 加载监听器
        recyclerView_refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Logger.i("正在加载", "正在加载");
            }
        });

    }

    public void sendMsg(String userId, String orderId, String content) {
        final String Url = ComParameter.URL + ComParameter.NEWMSG;
        Logger.i("开始请求列表", "   000000000000000");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        params.put("content", content);
        if (!(recverId.equals(""))){
            params.put("rcv_id",recverId);
        }
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
                        mHandler.sendEmptyMessage(0);
                    } else {
                        Toast.makeText(getContext(), "发送失败", Toast.LENGTH_SHORT).show();
                    }
//                        hideHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();


    }

    public void getMsg(String userId, String orderId) {

        final String Url = ComParameter.URL + ComParameter.VIEWMSG;
        Logger.i("开始请求列表", "   000000000000000");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("order_id", orderId);
        if (!(recverId.equals(""))){
            params.put("rcv_id",recverId);
        }
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
                    JSONObject object = JSONObject.fromObject(responseData);
                    String data = object.getJSONArray("data").toString();
                    Logger.i("打印列表信息", data + "");
                    if (object.getInt("status") == 1) {
                        Gson gson = new Gson();
                        orderMessageList.clear();
                        copylist = new ArrayList<>();
                        copylist = gson.fromJson(data, new TypeToken<List<OrderMessageModel>>() {
                        }.getType());
                        orderMessageList.addAll(copylist);

                        mHandler.sendEmptyMessage(1);
                    } else {
                        Toast.makeText(getContext(), "获取消息列表失败", Toast.LENGTH_SHORT).show();
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
