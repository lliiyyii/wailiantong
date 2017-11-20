package com.example.wailiantong;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.wailiantong.Activity.LoginActivity;
import com.example.wailiantong.Utills.CacheUtils;

/**
 * Created by weike on 2017/7/5.
 */

public class SplashActivity extends Activity {
    private Boolean isLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheUtils cacheUtils = new CacheUtils(this, "UserInfo");
        isLogin = cacheUtils.getBoolean("isLogin", false);
        setContentView(R.layout.splash_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (isLogin) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }


    }


}

