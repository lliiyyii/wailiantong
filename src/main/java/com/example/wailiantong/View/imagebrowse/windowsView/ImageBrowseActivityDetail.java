package com.example.wailiantong.View.imagebrowse.windowsView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.example.wailiantong.Activity.BaseActivity;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.View.imagebrowse.adapter.ViewPageAdapter;
import com.example.wailiantong.View.imagebrowse.presenter.ImageBrowsePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jelly on 2016/9/3.
 */
public class ImageBrowseActivityDetail extends BaseActivity implements ViewPager.OnPageChangeListener, ImageBrowseView {

    private ViewPager vp;
    private TextView hint;
    private ViewPageAdapter adapter;
    private ImageBrowsePresenter presenter;
    private int nowPosition;
    private ArrayList<String> nowImages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_image_browse_detail);
        vp = (ViewPager) this.findViewById(R.id.detail_viewPager);
        hint = (TextView) this.findViewById(R.id.detail_hint);


        initPresenter();
        presenter.loadImage();
        nowPosition = getIntent().getIntExtra("position", -1);
        nowImages = getIntent().getStringArrayListExtra("images");

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



    public static void startActivity(Context context, ArrayList<String> images, int position) {
        Intent intent = new Intent(context, ImageBrowseActivityDetail.class);
        intent.putStringArrayListExtra("images", images);
        intent.putExtra("position", position);

        context.startActivity(intent);
    }


}
