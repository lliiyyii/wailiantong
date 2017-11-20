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

import com.example.wailiantong.Adapter.RecyclerBusinessAdapter;
import com.example.wailiantong.Model.BusinessModel;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by 蔚克 on 2017/7/3.
 */

public class LiveSearchMain extends BaseActivity {
    EditText search;
    RecyclerView recyclerView;
    String searchContent;
    ImageView searchCommit;
    private List<BusinessModel> modellist = new ArrayList<>();
    private List<BusinessModel> copyList,typeList;
    private List<BusinessModel> typeCopyList = new ArrayList<>();
    private String nextListUrl;
    private RecyclerBusinessAdapter adapter;
    private CacheUtils cacheUtils;
    private String header;
    private String url= ComParameter.URL+ComParameter.SEARCHBYNAME;
    final String typeUrl = ComParameter.URL + ComParameter.GETLISTBYTYPE;

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    adapter.notifyDataSetChanged();
                    makeToast("加载完成");
                    break;
                case 2:
                    getListByType("身份证文字识别",nextListUrl);
            }
        }
    };
    int intent_data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seach_detail_layout);
        intent_data = getIntent().getIntExtra("intent_data", -1);


        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");

        initView();
        initListen();
        getListByType("身份证文字识别",typeUrl);
    }
    public void initView(){
        search= (EditText) findViewById(R.id.search_detail_edit);
        recyclerView= (RecyclerView) findViewById(R.id.search_detail_recyclerview);
        searchCommit= (ImageView) findViewById(R.id.search_detail_commit);


        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutmanager);
        adapter = new RecyclerBusinessAdapter(modellist);
        setItemClick(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (isSlideToBottom(recyclerView)) {   //如果列表滑动到了底部
                    Logger.i("这个时候请求的是什么呢？", nextListUrl + "   /99999999999999");
                    if (nextListUrl == null) {
                        makeToast("已经没有更多的数据了");
                    } else {
                        makeToast("正在加载");
                        if ((nextListUrl + "").equals("null")) {
                            makeToast("没有更多数据了");
                        } else {
                            mHandler.sendEmptyMessage(2);
                        }

                    }


                }

            }

        });

    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public void initListen(){
        searchCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent=search.getText().toString();
                if (isNumeric(searchContent)){
                    Logger.i("搜索的是证件号",searchContent+"     111111111"+url);
                    searchList(url,searchContent);
                }
                else {
                    Logger.i("姓名",searchContent+"     111111111"+url);
                    searchList(url,searchContent);
                }
            }
        });
    }
    public void setItemClick(RecyclerBusinessAdapter myAdapter) {
        myAdapter.setmOnItemClickListener(new RecyclerBusinessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (intent_data == 2) {
                    Intent intent = new Intent();
                    intent.putExtra("name", modellist.get(position).getParty_name());
                    intent.putExtra("idcard_number", modellist.get(position).getCertificate_num());
                    setResult(201, intent);
                    finish();
                } else {
                    Intent BusinessDetailIntent = new Intent(getActivity(), LivePicUrl.class);
                    int mPositon = modellist.get(position).getId();
                    BusinessDetailIntent.putExtra("position", mPositon + "");
                    BusinessDetailIntent.putExtra("name", modellist.get(position).getParty_name());
                    BusinessDetailIntent.putExtra("idcard_number", modellist.get(position).getCertificate_num());
                    startActivity(BusinessDetailIntent);
                    finish();
                }

            }
        });
    }

    public void searchList(String Url,String content){
        Map<String, String> params = new HashMap<String, String>();

        params.put("s", content);
        final org.json.JSONObject jsonObject = new org.json.JSONObject(params);

        Logger.i("开始请求列表","   000000000000000");
        final String getInfoUrl = Url + "";

        if (getInfoUrl.equals(null)||getInfoUrl==null||getInfoUrl.isEmpty()||getInfoUrl.equals("null")){
            makeToast("没有更多的数据了");
        }else {
            Logger.i("地址不为空", "33333");
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
                        RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                        Request request = new Request.Builder().url(getInfoUrl + "").addHeader("Cookie", header).post(requestbody).build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Logger.i("打印返回的数据", responseData+"  111111");
                        //// TODO: 2017/7/4  
                        JSONObject object = new JSONObject(responseData);
                        String data = object.getJSONArray("data").toString();


                        if (data.isEmpty()) {
                            makeToast("没有搜索到数据");
                        } else {
                            Gson gson = new Gson();
                            copyList = new ArrayList<>();
                            copyList = gson.fromJson(data, new TypeToken<List<BusinessModel>>() {
                            }.getType());
                            modellist.clear();
                            modellist.addAll(copyList);
                            mHandler.sendEmptyMessage(1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        ToastUtils.makeToast(LiveSearchMain.this,"未找对结果");
                        e.printStackTrace();
                    }
                    Looper.loop();

                }
            }).start();
        }
    }

    public void getListByType(String type,final String url) {



        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);

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
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).addHeader("Cookie", header).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);
                    String data = object.getJSONArray("data").toString();
                    nextListUrl = object.getString("next") + "";

                    if (object.getInt("status") == 1) {
                        Gson gson = new Gson();
                        typeList = new ArrayList<>();
                        typeList = gson.fromJson(data, new TypeToken<List<BusinessModel>>() {
                        }.getType());
                        typeCopyList.addAll(typeList);
                        modellist.clear();
                        modellist.addAll(typeCopyList);
                        mHandler.sendEmptyMessage(1);
                    } else {
                        makeToast("获取业务员信息失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }

}
