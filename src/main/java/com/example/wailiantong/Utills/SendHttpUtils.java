package com.example.wailiantong.Utills;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wailiantong.AliTest.client.Base64Encoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by weike on 2017/7/10.
 */

public class SendHttpUtils {


    public Bitmap getbitmap(String jpgUrl) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(jpgUrl);
        } catch (FileNotFoundException e) {
            Logger.i("文件没有找到", "1111111111111111111");
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        return bitmap;
    }


    public JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public String bitmaptoString(Bitmap bitmap) {


        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bStream);

        byte[] bytes = bStream.toByteArray();
        Base64Encoder base = new Base64Encoder();
        string = base.encode(bytes); //

        //  string = Base64.encodeToString(bytes, Base64.DEFAULT);

        return string;

    }
}
