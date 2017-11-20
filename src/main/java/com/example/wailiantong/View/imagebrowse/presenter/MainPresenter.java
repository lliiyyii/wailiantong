package com.example.wailiantong.View.imagebrowse.presenter;

import com.example.wailiantong.View.imagebrowse.windowsView.MainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jelly on 2016/9/3.
 */
public class MainPresenter {
    private MainView view;
    private List<String> images;


    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void loadImage( List<String> folder) {

        if (images == null) images = new ArrayList<>();
        images.addAll(folder);
        view.setImages(images);
        view.initRecycler();

    }


}
