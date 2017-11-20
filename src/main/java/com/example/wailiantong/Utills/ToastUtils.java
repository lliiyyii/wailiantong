package com.example.wailiantong.Utills;

import android.content.Context;
import android.widget.Toast;



public class ToastUtils {     //封装toast工具类
    public static final int LEVEL = 0;
    public  static final int SHOW_LEVEL = 1;

    public static void makeToast(Context mContext, String info){
        if(LEVEL < SHOW_LEVEL){
            Toast.makeText(mContext,info,Toast.LENGTH_SHORT).show();
        }
    }

    public static void makeToast(String info){
        if(LEVEL<SHOW_LEVEL){
            Toast.makeText(ActivityCollector.ACTIVITY,info,Toast.LENGTH_SHORT).show();
        }
    }
}
