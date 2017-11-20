package com.example.wailiantong.Fragment;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wailiantong.Adapter.RecyclerMessageAdapter;
import com.example.wailiantong.Model.MessageModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by weike on 2017/6/13.
 */

public class MessageFrament extends Fragment {
    private RecyclerView messageRecy;
    private List<MessageModel> messageList=  new ArrayList<>();
    private MessageModel model =new MessageModel("紧急通知","2017-12-10","由于系统升级，登录入口将于明日8点到12点关闭，请各位用户保存数据，敬请谅解！");

    private List<MessageModel> copyList;
    private RecyclerMessageAdapter adapter;
    private String header;
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

    public static MessageFrament newInstance() {
        Bundle bundle = new Bundle();
        MessageFrament fragment = new MessageFrament();
        fragment.setArguments(bundle);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zsy_fragmen_message, container, false);
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        messageList.add(model);

        messageRecy= (RecyclerView) view.findViewById(R.id.message_recy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecy.setLayoutManager(layoutManager);
         adapter = new RecyclerMessageAdapter(messageList);
        messageRecy.setAdapter(adapter);

//        getList(ComParameter.URL + ComParameter.GETMESSAGE);

        return view;
    }
    public void getList(String Url) {



            final String getInfoUrl = Url + "";
            Logger.i("打印请求列表的地址", getInfoUrl);
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
                        Request request = new Request.Builder().url(getInfoUrl).addHeader("Cookie", header).get().build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject object = JSONObject.fromObject(responseData);
                        String data = object.getJSONArray("data").toString();



                        if (object.getInt("status") == 1) {
                            Gson gson = new Gson();
                            copyList = new ArrayList<>();
                            copyList = gson.fromJson(data, new TypeToken<List<MessageModel>>() {
                            }.getType());
                            messageList.addAll(copyList);
                            mHandler.sendEmptyMessage(1);
                        } else {
                            makeToast("获取消息列表错误");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Looper.loop();

                }
            }).start();
        }


}