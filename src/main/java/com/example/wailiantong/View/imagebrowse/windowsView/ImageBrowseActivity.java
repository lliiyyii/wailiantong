package com.example.wailiantong.View.imagebrowse.windowsView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;


import com.example.wailiantong.Activity.BaseActivity;
import com.example.wailiantong.Activity.ReTakeBusiness;
import com.example.wailiantong.Activity.ReTakeCard;
import com.example.wailiantong.Activity.ReTakeHouse;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.adapter.ViewPageAdapter;
import com.example.wailiantong.View.imagebrowse.presenter.ImageBrowsePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jelly on 2016/9/3.
 */
public class ImageBrowseActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ImageBrowseView {

    private ViewPager vp;
    private TextView hint;
    private TextView reTake, delete;
    private ViewPageAdapter adapter;
    private ImageBrowsePresenter presenter;
    private int nowPosition;
    private ArrayList<String> nowImages;
    private String folderUrl, picUrl;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_image_browse);
        vp = (ViewPager) this.findViewById(R.id.viewPager);
        hint = (TextView) this.findViewById(R.id.hint);
        reTake = (TextView) this.findViewById(R.id.reTake);
        delete = (TextView) findViewById(R.id.browse_delete);
        initListen();
        initPresenter();
        presenter.loadImage();
        nowPosition = getIntent().getIntExtra("position", -1);
        nowImages = getIntent().getStringArrayListExtra("images");
        folderUrl = getIntent().getStringExtra("folderUrl");
        type = Integer.parseInt(getIntent().getStringExtra("type"));
        Logger.i("这是图片预览页面", Integer.valueOf(nowPosition).toString() + nowImages.toString());
    }

    public void initPresenter() {

        presenter = new ImageBrowsePresenter(this);
    }

    @Override
    public Intent getDataIntent() {
        Logger.i("这是Intent的打印", getIntent().toString());
        return getIntent();
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void setImageBrowse(List<String> images, int position) {
        if (adapter == null && images != null && images.size() != 0) {
            adapter = new ViewPageAdapter(this, images);
            vp.setAdapter(adapter);
            vp.setCurrentItem(position);
            vp.addOnPageChangeListener(this);
            hint.setText(position + 1 + "/" + images.size());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Logger.i("打印反转时候的位置", Integer.valueOf(position).toString() + "111111111" + Integer.valueOf(nowPosition).toString());
    }

    @Override
    public void onPageSelected(int position) {
        nowPosition = position;
        Logger.i("打印反转时候的位置11111111111", Integer.valueOf(position).toString() + "111111111" + Integer.valueOf(nowPosition).toString());
        presenter.setPosition(position);
        hint.setText(position + 1 + "/" + presenter.getImages().size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void initListen() {
        reTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("这是类型的打印",Integer.valueOf(type).toString());
                switch (type) {
                    case 1:
                        Intent reTakeIntent = new Intent(getContext(), ReTakeCard.class);
                        reTakeIntent.putStringArrayListExtra("images", nowImages);
                        reTakeIntent.putExtra("position", nowPosition);
                        reTakeIntent.putExtra("folderUrl", folderUrl);
                        startActivity(reTakeIntent);
                        break;
                    case 2:
                        Intent reTakeBusinessIntent = new Intent(getContext(), ReTakeBusiness.class);
                        reTakeBusinessIntent.putStringArrayListExtra("images", nowImages);
                        reTakeBusinessIntent.putExtra("position", nowPosition);
                        reTakeBusinessIntent.putExtra("folderUrl", folderUrl);
                        startActivity(reTakeBusinessIntent);
                        break;
                    case 3:
                        Intent reTakeHouseIntent = new Intent(getContext(), ReTakeHouse.class);
                        reTakeHouseIntent.putStringArrayListExtra("images", nowImages);
                        reTakeHouseIntent.putExtra("position", nowPosition);
                        reTakeHouseIntent.putExtra("folderUrl", folderUrl);
                        startActivity(reTakeHouseIntent);
                        break;
                }

                BrowserMain.instance.finish();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File deleteFile =new File(nowImages.get(nowPosition));
                deleteFile.delete();
                nowImages.remove(nowPosition);
                Intent deleteIntent = new Intent(getContext(), BrowserMain.class);
                deleteIntent.putStringArrayListExtra("newImages", nowImages);
                deleteIntent.putExtra("folderUrl", folderUrl);
                deleteIntent.putExtra("type",Integer.valueOf(type).toString());
                startActivity(deleteIntent);
                BrowserMain.instance.finish();
                finish();
            }
        });
    }

    public static void startActivity(Context context, ArrayList<String> images, int position, String folderUrl, String type) {
        Intent intent = new Intent(context, ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images", images);
        intent.putExtra("position", position);
        intent.putExtra("folderUrl", folderUrl);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }


}
