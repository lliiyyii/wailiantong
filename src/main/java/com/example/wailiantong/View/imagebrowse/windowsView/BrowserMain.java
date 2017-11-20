package com.example.wailiantong.View.imagebrowse.windowsView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wailiantong.Activity.ActiveBusinessTransmit;
import com.example.wailiantong.Activity.ActiveImgTransmit;
import com.example.wailiantong.Activity.BaseActivity;
import com.example.wailiantong.Activity.ContinueBusiness;
import com.example.wailiantong.Activity.ContinueCardId;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.adapter.ImageRecyclerAdapter;
import com.example.wailiantong.View.imagebrowse.adapter.OnRecyclerItemClickListener;
import com.example.wailiantong.View.imagebrowse.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.wailiantong.Utills.ToastUtils.makeToast;

public class BrowserMain extends BaseActivity implements MainView {

    private RecyclerView rv;
    private List<String> images;
    private ImageRecyclerAdapter adapter;
    private MainPresenter presenter;
    private List<String> newImages, netUrlImages;
    private String folderUrl, type, mType, mSalemanId, dateTime, partyName, certificateNum, mResult, header, sendPicUrl;
    private int which;
    private Button upload,conti;
    private TextView back;
    private Bitmap waitSent;
    public static BrowserMain instance = null;
    private StringBuilder backInfo;
    private CacheUtils cacheUtils;
    private Boolean isSuccess;

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Logger.i("通过handler判断sendPicUrl是否赋值了?", sendPicUrl + "");
                    break;
                default:
                    Logger.i("handler没有正确处理", "     啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        backInfo = null;

        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        Intent getmIntent = getIntent();
        newImages = getmIntent.getStringArrayListExtra("newImages");
        folderUrl = getmIntent.getStringExtra("folderUrl");
        type = getmIntent.getStringExtra("type");
        which = Integer.parseInt(type);
        switch (which) {
            case 1:
                mType = "身份证文字识别";
                break;
            case 2:
                mType = "营业执照文字识别";
                break;
            case 3:
                mType = "户口本文字识别";
        }
        setContentView(R.layout.zsy_browser_main);
        initView();
        initPresenter();
        presenter.loadImage(newImages);
    }

    public void initView() {
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        upload = (Button) findViewById(R.id.view_upload);
        conti= (Button) findViewById(R.id.view_conti);
        back = (TextView) findViewById(R.id.view_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("本次点击是什么类型的识别呢？", mType + Integer.valueOf(which).toString() + "");

                switch (mType) {
                    case "身份证文字识别":
                        int i = newImages.size();
                        Logger.i("本次的上传类型是什么？", mType + Integer.valueOf(which).toString() + "");
                        if (i  == 1) {
                            //偶数可以上传
                            Intent upIdIntent = new Intent(BrowserMain.this,ActiveImgTransmit.class);
                            upIdIntent.putExtra("mType",mType);
                            upIdIntent.putStringArrayListExtra("nowImages", (ArrayList<String>) newImages);
                            startActivity(upIdIntent);
                            finish();
                        } else {
                            //是奇数不能上传
                            makeToast("要求只能上传一张照片，请检查后重试");

                        }
                        break;
                    case "营业执照文字识别":
                        Logger.i("本次的上传类型是什么？", mType + Integer.valueOf(which).toString() + "");
                        Intent upBusinessIntent = new Intent(BrowserMain.this,ActiveBusinessTransmit.class);
                        upBusinessIntent.putExtra("mType",mType);
                        upBusinessIntent.putStringArrayListExtra("nowImages", (ArrayList<String>) newImages);
                        startActivity(upBusinessIntent);
                        finish();
                        break;
                    case "户口本文字识别":
                        Logger.i("本次的上传类型是什么？", mType + Integer.valueOf(which).toString() + "");
                        Intent upHouseIntent = new Intent(BrowserMain.this,HouseTransmit.class);
                        upHouseIntent.putExtra("mType",mType);
                        upHouseIntent.putStringArrayListExtra("nowImages", (ArrayList<String>) newImages);
                        startActivity(upHouseIntent);
                        break;
                    default:
                        Logger.i("识别类型错误!!!!", "3333333333333");
                        break;
                }



            }
        });
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("本次点击是什么类型的识别呢？", mType + Integer.valueOf(which).toString() + "");
                switch (mType) {
                    case "身份证文字识别":
                        Intent contiIdIntent = new Intent(BrowserMain.this, ContinueCardId.class);
                        contiIdIntent.putExtra("folderUrl",folderUrl);
                        contiIdIntent.putStringArrayListExtra("images", (ArrayList<String>) newImages);
                        startActivity(contiIdIntent);
                        finish();
                        break;
                    case "营业执照文字识别":
                        Intent contiBusinessIntent = new Intent(BrowserMain.this, ContinueBusiness.class);
                        contiBusinessIntent.putExtra("folderUrl",folderUrl);
                        contiBusinessIntent.putStringArrayListExtra("images", (ArrayList<String>) newImages);
                        startActivity(contiBusinessIntent);
                        finish();
                        break;
//                    case "户口本文字识别":
//                        Logger.i("本次的上传类型是什么？", mType + Integer.valueOf(which).toString() + "");
//                        Intent upHouseIntent = new Intent(BrowserMain.this, HouseTransmit.class);
//                        upHouseIntent.putExtra("mType", mType);
//                        upHouseIntent.putStringArrayListExtra("nowImages", (ArrayList<String>) newImages);
//                        startActivity(upHouseIntent);
//                        break;
//                    default:
//                        Logger.i("识别类型错误!!!!", "3333333333333");
//                        break;
                }
            }
        });
    }
    public void initPresenter() {
        presenter = new MainPresenter(this);
    }

    @Override
    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public void initRecycler() {
        if (images == null || images.size() == 0) return;

        if (adapter == null) {
            rv.setLayoutManager(new GridLayoutManager(this, 3));
            rv.setItemAnimator(new DefaultItemAnimator());
            adapter = new ImageRecyclerAdapter(this, images);
            adapter.setItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void click(View item, int position) {
                    ImageBrowseActivity.startActivity(BrowserMain.this, (ArrayList<String>) images, position, folderUrl, type);
                }
            });
            rv.setAdapter(adapter);
        }
    }



}
