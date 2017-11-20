package com.example.wailiantong.View.imagebrowse.windowsView;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wailiantong.Activity.BaseActivity;
import com.example.wailiantong.R;
import com.example.wailiantong.View.imagebrowse.adapter.ImageRecyclerAdapter;
import com.example.wailiantong.View.imagebrowse.adapter.OnRecyclerItemClickListener;
import com.example.wailiantong.View.imagebrowse.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.wailiantong.Utills.ToastUtils.makeToast;

public class BrowserMainDetail extends BaseActivity implements MainView {

    private RecyclerView rv;
    private List<String> images;
    private ImageRecyclerAdapter adapter;
    private MainPresenter presenter;
    private List<String> newImages;
    private TextView back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        newImages = getIntent().getStringArrayListExtra("newImages");


        setContentView(R.layout.browser_main_detail);
        initView();
        initPresenter();
        presenter.loadImage(newImages);
    }

    public void initView() {
        rv = (RecyclerView) findViewById(R.id.detail_recyclerView);

        back = (TextView) findViewById(R.id.view_detail_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    ImageBrowseActivityDetail.startActivity(BrowserMainDetail.this, (ArrayList<String>) images, position);
                }
            });
            rv.setAdapter(adapter);
        }
    }



}
