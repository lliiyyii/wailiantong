package com.example.wailiantong.Utills;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔚克 on 2017/3/15.
 */

public class ActivityCollector {   //活动管理器
    public static Context ACTIVITY;   //设置ACTIVITY为全局变量，即用户当前看到的activity
    public static List<Activity> activities = new ArrayList<>();  //存放活动的list

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {   //清除list中所有活动，并将他们finish（）
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void finishActivity(Activity activity)   //finish（）特定活动
    {
        try{
            if (!activity.isFinishing())
            {
                activity.finish();
            }
        }catch (Exception e)
        {

        }
    }
}
