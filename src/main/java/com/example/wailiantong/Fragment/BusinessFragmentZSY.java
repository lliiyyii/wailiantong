package com.example.wailiantong.Fragment;

import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wailiantong.Activity.ActiveIdentActivity;
import com.example.wailiantong.Activity.OrderCreateActivity;
import com.example.wailiantong.Activity.OrderDetail;
import com.example.wailiantong.Activity.SearchByname;
import com.example.wailiantong.Adapter.GirdDropDownAdapter;
import com.example.wailiantong.Adapter.RecyclerAllOrderAdapter;
import com.example.wailiantong.Adapter.RecyclerMessageAdapter;
import com.example.wailiantong.Dialog.ActiveDialog;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.Model.OrderModel;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.DropDownMenu;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.RefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by 蔚克 on 2017/11/10.
 */

public class BusinessFragmentZSY extends Fragment {
    private RecyclerView orderRecy;

    private DropDownMenu mDropDownMenu;
    private RefreshLayout recyclerView_refresh;
    private String headers[] = {"信用", "价格", "结束时间"};
    private List<View> popupViews = new ArrayList<>();
    private OrderModel orderModel = new OrderModel("我们需要一笔钱", "我们可以好好的合作请速度联系我们！！！", "2000", "90");
    private List<OrderModel> orderList = new ArrayList<>();
    private List<OrderModel> copyList;
    private RecyclerMessageAdapter adapter;
    private RecyclerAllOrderAdapter orderAdapter;
    private GirdDropDownAdapter firstDropAdapter;
    private GirdDropDownAdapter secondDropAdapter;
    private GirdDropDownAdapter thirdDropAdapter;
    private String[] fistOpt = {"信用", "信用优先"};
    private String[] secondOpt = {"价格", "价格从高到低", "价格从低到高"};
    private String[] thirdOpt = {"结束时间", "时间从近到远", "时间从远到近"};
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fab;
    private RelativeLayout searchLayout;
    private CacheUtils cacheUtils;
    private ActiveDialog dialog;
    private String listUrl = "/order/main", userId, type = "5", firstList = "0", nextList = "0";

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
                getList(userId, type, firstList);
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
                    orderAdapter.notifyDataSetChanged();
                    hideHandler.sendEmptyMessage(0);
                    break;
                case 2:
                    getList(userId, type, firstList);
                    break;
            }
        }
    };

    public static BusinessFragmentZSY newInstance() {
        Bundle bundle = new Bundle();
        BusinessFragmentZSY fragment = new BusinessFragmentZSY();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zsy_fragmen_business, container, false);
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        userId = cacheUtils.getValue("userId", "");


        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.maindropDownMenu);
        searchLayout = (RelativeLayout) view.findViewById(R.id.business_mainsearch);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击搜索跳转到下一页
                Intent BusinessSearchIntent = new Intent(getActivity(), SearchByname.class);
                startActivity(BusinessSearchIntent);

            }
        });


        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        initView();

        if (cacheUtils.getBoolean("isActive", false)) {
            getList(userId, type, firstList);
        } else {
            dialog.show();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog.isShowing()){
            dialog.dismiss();
        }

        if (cacheUtils.getBoolean("isActive", false)) {
            getList(userId, type, firstList);
        } else {
            dialog.show();
        }
    }

    private void initView() {

        dialog = new ActiveDialog(getContext());
        dialog.setListener(new ActiveDialog.OnCommitOnclickListener() {
            @Override
            public void onCommitClick() {
                Intent IdIdenIntent = new Intent(getActivity(), ActiveIdentActivity.class);
                startActivity(IdIdenIntent);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });

        //init city menu
        final ListView firstView = new ListView(this.getContext());
        firstDropAdapter = new GirdDropDownAdapter(this.getContext(), Arrays.asList(fistOpt));
        firstView.setDividerHeight(0);
        firstView.setAdapter(firstDropAdapter);

        //init age menu
        final ListView secondView = new ListView(this.getContext());
        secondView.setDividerHeight(0);
        secondDropAdapter = new GirdDropDownAdapter(this.getContext(), Arrays.asList(secondOpt));
        secondView.setAdapter(secondDropAdapter);

        //init sex menu
        final ListView thirdView = new ListView(this.getContext());
        thirdView.setDividerHeight(0);
        thirdDropAdapter = new GirdDropDownAdapter(this.getContext(), Arrays.asList(thirdOpt));
        thirdView.setAdapter(thirdDropAdapter);


        //init popupViews
        popupViews.add(firstView);
        popupViews.add(secondView);
        popupViews.add(thirdView);


        //add item click event
        firstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstDropAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : fistOpt[position]);
                Logger.i("点击了", position + fistOpt[position] + "   000");
                mDropDownMenu.initText("价格", "时间");
                secondDropAdapter.setCheckItem(0);
                thirdDropAdapter.setCheckItem(0);
                mDropDownMenu.closeMenu();
                switch (position) {
                    case 1:
                        //点击了信用优先
                        type = "1";
                        mHandler.sendEmptyMessage(2);
                        break;
                }
            }
        });

        secondView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secondDropAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : secondOpt[position]);
                Logger.i("点击了", position + secondOpt[position] + "   000");
                mDropDownMenu.initText("信用", "时间");
                thirdDropAdapter.setCheckItem(0);
                firstDropAdapter.setCheckItem(0);
                mDropDownMenu.closeMenu();
                switch (position) {
                    case 1:
                        //点击了价格从低到高
                        type = "3";
                        mHandler.sendEmptyMessage(2);
                        break;
                    case 2:
                        //点击了价格从高到低
                        type = "4";
                        mHandler.sendEmptyMessage(2);
                        break;
                }
            }
        });

        thirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                thirdDropAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : thirdOpt[position]);
                Logger.i("点击了", position + thirdOpt[position] + "   000");
                mDropDownMenu.initText("信用", "价格");
                firstDropAdapter.setCheckItem(0);
                secondDropAdapter.setCheckItem(0);
                mDropDownMenu.closeMenu();
                switch (position) {
                    case 1:
                        //点击了时间从近到远
                        type = "5";
                        mHandler.sendEmptyMessage(2);
                        break;
                    case 2:
                        //点击了时间从远到近
                        type = "6";
                        mHandler.sendEmptyMessage(2);
                        break;
                }
            }
        });


        //init context view
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View contentView = inflater.inflate(R.layout.zsy_recyclerviewlist, null, false);


        recyclerView_refresh = (RefreshLayout) contentView.findViewById(R.id.recyclerView_refresh);
        initRefreshLayout();
        orderRecy = (RecyclerView) contentView.findViewById(R.id.business_recyclerView);
        orderRecy.setLayoutManager(layoutManager);
        orderRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(orderRecy)) {
                    //当列表滑动到底部的时候
                    Logger.i("滑动到底部了", "滑动到底部了");
                    if (nextList.equals("")) {
                        Toast.makeText(getContext(), "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                        getList(userId, type, nextList);
                    }


                }
            }
        });

        orderAdapter = new RecyclerAllOrderAdapter(orderList);
        orderAdapter.setmOnItemClickListener(new RecyclerAllOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent BusinessDetailIntent = new Intent(getActivity(), OrderDetail.class);
                int mId = orderList.get(position).getId();
                BusinessDetailIntent.putExtra("Id", mId + "");
                BusinessDetailIntent.putExtra("type","1");
                startActivity(BusinessDetailIntent);
            }
        });
        orderRecy.setAdapter(orderAdapter);
//        adapter = new RecyclerMessageAdapter(messageList);
//        orderRecy.setAdapter(adapter);
        fab = (FloatingActionButton) contentView.findViewById(R.id.fab);
        fab.attachToRecyclerView(orderRecy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("点击了按钮", "点击了按钮");
                Intent BusinessIntent = new Intent(getActivity(), OrderCreateActivity.class);
                startActivity(BusinessIntent);
            }
        });
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }


    public void exitDropMenu() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();

        }
    }

    public void getList(String userId, String type, final String next) {
        final String URL = ComParameter.URL + ComParameter.GETMAINLIST;
        if (next.equals("")) {
            Toast.makeText(getContext(), "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
        } else {
            Logger.i("开始请求列表", "   000000000000000");
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", userId);
            params.put("type", type);
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



    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

}