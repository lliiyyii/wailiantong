package com.example.wailiantong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wailiantong.Activity.BusinessDetail;
import com.example.wailiantong.Activity.CalendarActivity;
import com.example.wailiantong.Activity.SearchByname;
import com.example.wailiantong.Adapter.RecyclerBusinessAdapter;
import com.example.wailiantong.Model.BusinessModel;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.RefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
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
 * Created by 蔚克 on 2017/6/2.
 */

public class BusinessFragment extends Fragment {
    RefreshLayout recyclerView_refresh;
    public static int CALENDAR = 101;
    private RecyclerBusinessAdapter adapter;
    private TabLayout mTabLayout;

    private TabItem photo, status, businesslicense, house, idcard;

    private List<BusinessModel> modellist = new ArrayList<>();
    private List<BusinessModel> typeCopyList = new ArrayList<>();
    private List<BusinessModel> dateCopyList = new ArrayList<>();
    private List<BusinessModel> copyList, typeList;
    private ArrayList<BusinessModel> tempList;
    private String header;
    private CacheUtils cacheUtils;
    private RelativeLayout searchMain;
    private String nextListUrl, dateTo,typeTo;
    private RecyclerView recyclerView;
    private Boolean first;
    private TextView date;
    final String typeUrl = ComParameter.URL + ComParameter.GETLISTBYTYPE;
    final String dateUrl = ComParameter.URL + ComParameter.GETLISTBYDATE;

    private int typeId,dateId;
    Handler hideHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideRefresh();
        }
    };

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

                    Toast.makeText(getContext(), "正在加载下一页", Toast.LENGTH_SHORT).show();
                    if ((nextListUrl + "").equals("null")) {
                        hideHandler.sendEmptyMessage(0);
                    } else {
                        getList(nextListUrl + "");
                    }

                    break;
                case 3:
                    typeTo="活体检测+照片比对";
                    Logger.i("点击了活体照片对比选项卡", "1");
                    getListByType("活体检测+照片比对", typeUrl);
                    typeId = 3;
                    break;
                case 4:
                    typeTo="活体检测+身份证核身";
                    Logger.i("点击了活体身份审核选项卡", "1");
                    getListByType("活体检测+身份证核身", typeUrl);
                    typeId = 4;
                    break;
                case 5:
                    typeTo="身份证文字识别";
                    Logger.i("点击了身份证文字识别", "1");
                    getListByType("身份证文字识别", typeUrl);
                    typeId = 5;
                    break;
                case 6:
                    typeTo="营业执照文字识别";
                    Logger.i("点击了营业执照文字识别", "1");
                    getListByType("营业执照文字识别", typeUrl);
                    typeId = 6;
                    break;
                case 7:
                    typeTo="户口本文字识别";
                    Logger.i("点击了户口本文字识别选项卡", "1");
                    getListByType("户口本文字识别", typeUrl);
                    typeId = 7;
                    break;
                case 8:
                    getList(nextListUrl + "");
                    break;
                case 9:
                    Logger.i("点击了活体照片对比选项卡", "1");
                    getListByType("活体检测+照片比对", nextListUrl);
                    typeId = 3;
                    break;
                case 10:
                    Logger.i("点击了活体身份审核选项卡", "1");
                    getListByType("活体检测+身份证核身", nextListUrl);
                    typeId = 4;
                    break;
                case 11:
                    Logger.i("点击了营业执照检测选项卡", "1");
                    getListByType("身份证文字识别", nextListUrl);
                    typeId = 5;
                    break;
                case 12:
                    Logger.i("点击了户口本检测选项卡", "1");
                    getListByType("营业执照文字识别", nextListUrl);
                    typeId = 6;
                    break;
                case 13:
                    Logger.i("点击了身份证照片选项卡", "1");
                    getListByType("户口本文字识别", nextListUrl);
                    typeId = 7;
                    break;
                case 14:
                    dateId = 9;
                    Logger.i("进行了日期搜索", dateTo + "      1");
                    getListByDate(dateTo, dateUrl,typeTo);
                    break;
                case 15:
                    getListByDate(dateTo, nextListUrl,typeTo);
                    break;


            }
        }
    };

    public static BusinessFragment newInstance() {
        Bundle bundle = new Bundle();
        BusinessFragment fragment = new BusinessFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        typeId = 2;
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        View view = inflater.inflate(R.layout.zsy_fragmen_business, container, false);
        setHasOptionsMenu(true);
        nextListUrl = "";
       // getList(ComParameter.URL + ComParameter.GETLIST);   //第一次请求列表1
        //initData();
        getListByType("活体检测+照片比对", typeUrl);
        initView(view);
        getTab();
        return view;
    }


    public void getTab() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            final int j = i;
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab == null) {
                continue;
            }
            Class c = tab.getClass();
            try {
                Field field = c.getDeclaredField("mView");
                if (field == null) {
                    continue;
                }

                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    continue;
                }
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        typeCopyList.clear();
                        mHandler.sendEmptyMessage(j + 3);
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public void initView(View view) {
        recyclerView_refresh = (RefreshLayout) view.findViewById(R.id.recyclerView_refresh);
//        date = (TextView) view.findViewById(R.id.business_frag_date);
//        mTabLayout = (TabLayout) view.findViewById(R.id.business_type_title);
//        photo = (TabItem) view.findViewById(R.id.title_Photo);
//        status = (TabItem) view.findViewById(R.id.title_status);
//        businesslicense = (TabItem) view.findViewById(R.id.title_businesslicense);
//        house = (TabItem) view.findViewById(R.id.title_house);
//        idcard = (TabItem) view.findViewById(R.id.title_idcard);
        searchMain = (RelativeLayout) view.findViewById(R.id.business_mainsearch);
        searchMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), SearchByname.class);

                startActivity(searchIntent);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IdIdenIntent = new Intent(getActivity(), CalendarActivity.class);
                IdIdenIntent.putExtra("intent_data", 1);
                startActivityForResult(IdIdenIntent, CALENDAR);

            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.business_recyclerView);
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
                        Toast.makeText(getContext(), "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast toast=Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT);
                        toast.show();
                        if ((nextListUrl + "").equals("null")) {
                            Toast.makeText(getContext(), "已经没有更多的数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dateId==9) mHandler.sendEmptyMessage(dateId + 6);
                            else mHandler.sendEmptyMessage(typeId + 6);
                        }

                    }


                }

            }

        });
        initRefreshLayout();


    }
    public void initRefreshLayout() {

        recyclerView_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        recyclerView_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                typeCopyList.clear();
                mHandler.sendEmptyMessage(typeId);
            }
        });
        // 加载监听器
        recyclerView_refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

//                PAGE++;
//                getResult(++PAGE);
            }
        });

    }
    private void hideRefresh() {
        try {
            if (recyclerView_refresh.isRefreshing())
                recyclerView_refresh.setRefreshing(false);
//        if (refreshLayout.isLoading)
//            refreshLayout.setLoading(false);
            if (recyclerView_refresh.isLoading)
                recyclerView_refresh.setLoading(false);
        } catch (Exception e) {
            Logger.e("iiiiiiiiiiiiiiiii",e.toString());
        }

    }
    public void setItemClick(RecyclerBusinessAdapter myAdapter) {
        myAdapter.setmOnItemClickListener(new RecyclerBusinessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent BusinessDetailIntent = new Intent(getActivity(), BusinessDetail.class);
                int mPositon = modellist.get(position).getId();
                BusinessDetailIntent.putExtra("position", mPositon + "");
                startActivity(BusinessDetailIntent);
            }
        });
    }


    public void getList(String Url) {
        Logger.i("开始请求列表", "   000000000000000");


        final String getInfoUrl = Url + "";

        if (getInfoUrl.equals(null) || getInfoUrl == null || getInfoUrl.isEmpty() || getInfoUrl.equals("null")) {
            makeToast("没有更多的数据了");
        } else {
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
                        Request request = new Request.Builder().url(getInfoUrl + "").addHeader("Cookie", header).get().build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject object = JSONObject.fromObject(responseData);
                        Logger.i("打印下一页的地址", object.getString("next") + "   /00000000000000000000000" + object.toString());
                        String data = object.getJSONArray("data").toString();
                        nextListUrl = object.getString("next") + "";


                        if (object.getInt("status") == 1) {
                            Gson gson = new Gson();
                            copyList = new ArrayList<>();
                            copyList = gson.fromJson(data, new TypeToken<List<BusinessModel>>() {
                            }.getType());
                            modellist.addAll(copyList);
                            Logger.i("打印modellist的信息", data.toString() + "1111" + modellist.get(0).getCertificate_num() + modellist.get(0).getParty_name());
                            mHandler.sendEmptyMessage(1);
                        } else {
                            Toast.makeText(getContext(), "获取业务员信息失败", Toast.LENGTH_SHORT).show();
                        }
                        hideHandler.sendEmptyMessage(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Looper.loop();

                }
            }).start();
        }

    }

    public void getListByType(String type, final String url) {

Logger.i("打印请求的地址",url+"");
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
                    JSONObject object = JSONObject.fromObject(responseData);
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

                    }
                    hideHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }


    public void getListByDate(String date, final String url,String mtype) {


        Map<String, String> params = new HashMap<String, String>();
        params.put("date_time", date);
        params.put("type",mtype);
        final org.json.JSONObject jsonObject = new org.json.JSONObject(params);
        Logger.i("日期筛选发送的数据",jsonObject.toString());
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
                    JSONObject object = JSONObject.fromObject(responseData);
                    String data = object.getJSONArray("data").toString();
                    nextListUrl = object.getString("next") + "";
Logger.i("日期筛选返回的数据",responseData);
                    if (object.getInt("status") == 1) {
                        Gson gson = new Gson();
                        dateCopyList = new ArrayList<>();
                        dateCopyList = gson.fromJson(data, new TypeToken<List<BusinessModel>>() {
                        }.getType());

                        modellist.clear();
                        modellist.addAll(dateCopyList);
                        mHandler.sendEmptyMessage(1);
                    } else {

                    }
                    hideHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALENDAR && resultCode == -1) {
            dateTo = data.getStringExtra("data");
            mHandler.sendEmptyMessage(14);
        }
    }
}


