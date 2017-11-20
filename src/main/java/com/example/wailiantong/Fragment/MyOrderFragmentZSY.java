package com.example.wailiantong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wailiantong.Activity.OrderDetail;
import com.example.wailiantong.Adapter.RecyclerAllOrderAdapter;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.Model.OrderModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
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

/**
 * Created by 蔚克 on 2017/11/12.
 */

public class MyOrderFragmentZSY extends Fragment {

    private RecyclerAllOrderAdapter adapter;
    private RecyclerView recyclerView;
    private OrderModel orderModel = new OrderModel("我们需要一笔钱", "我们可以好好的合作请速度联系我们！！！", "2000", "90");
    private List<OrderModel> orderList = new ArrayList<>();
    private List<OrderModel> copyList;
    private LinearLayoutManager layoutManager;
    private RefreshLayout recyclerView_refresh;
    private String userId,nextList="0",next="0";
    private CacheUtils cacheUtils;
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

    public void initRefreshLayout() {
        recyclerView_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        recyclerView_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList(userId,next);
//                typeCopyList.clear();
//                mHandler.sendEmptyMessage(typeId);
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

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    hideHandler.sendEmptyMessage(0);
                    break;
                case 2:
                    getList(userId, next);
                    break;
            }
        }
    };

    public static MyOrderFragmentZSY newInstance() {
        Bundle bundle = new Bundle();
        MyOrderFragmentZSY fragment = new MyOrderFragmentZSY();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zsy_fragment_myorder, container, false);
        initView(view);
        cacheUtils=new CacheUtils(getContext(),"UserInfo");
        userId=cacheUtils.getValue("userId","");


        return view;
    }

    public void initView(View view) {
        for (int i = 0; i <3; i++) {
            orderList.add(orderModel);
        }
        recyclerView_refresh= (RefreshLayout) view.findViewById(R.id.recyclerView_refresh_myorder);
        initRefreshLayout();
        recyclerView = (RecyclerView) view.findViewById(R.id.myorder_recyclerView);

        adapter = new RecyclerAllOrderAdapter(orderList);
//        adapter.setmOnItemClickListener(new RecyclerAllOrderAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent BusinessDetailIntent = new Intent(getActivity(), OrderDetail.class);
//                startActivity(BusinessDetailIntent);
//            }
//        });

        adapter.setmOnItemClickListener(new RecyclerAllOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent BusinessDetailIntent = new Intent(getActivity(), OrderDetail.class);
                int mId = orderList.get(position).getId();
                BusinessDetailIntent.putExtra("Id", mId + "");
                BusinessDetailIntent.putExtra("type","0");
                startActivity(BusinessDetailIntent);
            }
        });

        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    //当列表滑动到底部的时候
                    Logger.i("滑动到底部了", "滑动到底部了");
                    if (nextList.equals("")) {
                        Toast.makeText(getContext(), "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                        getList(userId,nextList);
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


    public void getList(String userId, final String next) {
        final String URL = ComParameter.URL + ComParameter.GETMYLIST;
        if (next.equals("")) {
            Toast.makeText(getContext(), "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
        } else {
            Logger.i("开始请求列表", "   000000000000000");
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", userId);
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
                        nextList = object.getJSONObject("data").getString("next");
                        if (object.getInt("status") == 1) {
                            Gson gson = new Gson();
                            copyList = new ArrayList<>();
                            copyList = gson.fromJson(data, new TypeToken<List<OrderModel>>() {
                            }.getType());
                            orderList.clear();
                            orderList.addAll(copyList);
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
}
