package com.example.wailiantong;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.TabLayout;

import com.example.wailiantong.Adapter.FragMainAdapter;
import com.example.wailiantong.Fragment.BusinessFragmentZSY;
import com.example.wailiantong.Fragment.MessageFrament;
import com.example.wailiantong.Fragment.MyFragment;
import com.example.wailiantong.Fragment.MyOrderFragmentZSY;
import com.example.wailiantong.Utills.ActivityCollector;
import com.example.wailiantong.Utills.TongHttpUtils;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

public class MainActivity extends AppCompatActivity {

    private String[] titles = new String[]{"首页", "订单", "消息", "我的"};

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private FragMainAdapter fragmentAdapter;
    public LinearLayoutManager layoutmanager;
    //ViewPage选项卡页面集合
    private List<Fragment> mFragment;
    //Tab标题集合
    private List<String> mTitles;
    ///图片数组 添加selector实现点击与不点击的动态变化
    private int[] mImg = new int[]{R.drawable.businesssel, R.drawable.homesel, R.drawable.messagesel, R.drawable.mysel,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityCollector collector = new ActivityCollector();
        collector.addActivity(this);
        if (Build.VERSION.SDK_INT > LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                        0);
                //权限还没有授予，需要在这里写申请权限的代码
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                        0);
                //权限还没有授予，需要在这里写申请权限的代码
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                        0);
                //权限还没有授予，需要在这里写申请权限的代码
            }

        }


        SystemBar();


        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTablayout = (TabLayout) findViewById(R.id.main_tablayout);

        mTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTitles.add(titles[i]);
        }

        mFragment = new ArrayList<>();
        mFragment.add(BusinessFragmentZSY.newInstance());
        mFragment.add(MyOrderFragmentZSY.newInstance());
        mFragment.add(MessageFrament.newInstance());
        mFragment.add(MyFragment.newInstance());
//        mFragment.add(MainFragment.newInstance());
//       mFragment.add(BusinessFragment.newInstance());
//        mFragment.add(MessageFrament.newInstance());
//        mFragment.add(MyFragment.newInstance());


        fragmentAdapter = new FragMainAdapter(getSupportFragmentManager(), mFragment, mTitles);
        mViewPager.setAdapter(fragmentAdapter);
        /**
         * 设置viewpager最多保存4个页面的状态
         */
        mViewPager.setOffscreenPageLimit(4);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setSelectedTabIndicatorHeight(0);


        for (int i = 0; i < mTitles.size(); i++) {
            //获取对应位置的Tab
            TabLayout.Tab itemTab = mTablayout.getTabAt(i);
            if (itemTab != null) {
                //设置自定义的标题

                itemTab.setCustomView(R.layout.bottom_itemtab);
                TextView textView = (TextView) itemTab.getCustomView().findViewById(R.id.bottom_tab_text);
                textView.setText(mTitles.get(i));
                ImageView imageView = (ImageView) itemTab.getCustomView().findViewById(R.id.bottom_tab_img);
                imageView.setImageResource(mImg[i]);
            }


        }
        mTablayout.getTabAt(0).getCustomView().setSelected(true);
        TongHttpUtils mUtil = new TongHttpUtils();
        mUtil.getInfo(this);
    }

    private void SystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }




    }


