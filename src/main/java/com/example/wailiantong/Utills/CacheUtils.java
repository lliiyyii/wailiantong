package com.example.wailiantong.Utills;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Set;

public class CacheUtils {
    String fileName;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public CacheUtils(Context context, String fileName){
        this.fileName = fileName;
        preferences = context.getSharedPreferences(this.fileName,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 向Cache存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     * @param key
     * @param value
     */

    public void putValue(String key, int value){  //存储整型
        editor.putInt(key,value);
        editor.commit();
    }

    public void putValue(String key, boolean value){   //存储布尔型
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void putValue(String key, String value){   //存储字符串型
        editor.putString(key,value);
        editor.commit();
    }

    public void putValue(String key, List<String> value){   //存储字符串列表
        editor.putStringSet(key, (Set<String>) value);
        editor.commit();
    }

    /**
     * 获取Cache数据里指定key对应的value。如果key不存在，则返回默认值def。
     * @param key
     * @param def
     * @return
     */

    public String getValue(String key, String def){
        return preferences.getString(key,def);
    }
    public int getInt(String key,int def){
        return preferences.getInt(key, def);
    }
    public Boolean getBoolean(String key,Boolean def){
        return preferences.getBoolean(key, def);
    }

    /**
     * 清空缓存
     */
    public void clearCache(){
        editor.clear();
        editor.commit();
    }
}
