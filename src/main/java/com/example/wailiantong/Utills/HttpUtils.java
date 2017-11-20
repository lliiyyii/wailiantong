package com.example.wailiantong.Utills;


import com.example.wailiantong.Model.ComParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lucky_Xiao on 2017/1/9.
 */
public class HttpUtils {



    public static boolean isSuccess(JSONObject s)//判断Http请求是否成功
    {
        try {
            Boolean rescode=dealJsonObjectItemMsgs(s,"success");
            if(rescode)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static boolean isSuccess(String s)//判断Http请求是否成功
    {
        try {
            JSONObject data = new JSONObject(s);
            Boolean rescode=dealJsonObjectItemMsgs(data,"success");
            if(rescode)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static Boolean dealJsonObjectItemMsgs(JSONObject jsonObject, String strKey)
    {
        if (jsonObject.toString().contains(strKey))
        {
            try {
                return jsonObject.getBoolean(strKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param jsonObject
     * @param strKey
     * @return
     */
    public static String dealJsonObjectItemMsg(JSONObject jsonObject, String strKey)
    {
        if (jsonObject.toString().contains(strKey))
        {
            try {
                return jsonObject.getString(strKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String getErrorMsg(JSONObject s)
    {
        String msg="";
        try {
            msg=dealJsonObjectItemMsg(s,"err_msg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })//此处最初为.connectTimeout(10, TimeUnit.SECONDS).build();有一半的图片无法发送，报error: java.net.SocketTimeoutException。后改为当前代码，一些本来无法发送的就可以了
            .connectTimeout(10, TimeUnit.SECONDS)
//            .addNetworkInterceptor(new StethoInterceptor()) //增加调试查看
            .readTimeout(10000, TimeUnit.SECONDS).build();

    public static Request createRequest(String url, Map<String,String> dataMap)
    {
        FormBody.Builder builder=new FormBody.Builder();
        for(String key: dataMap.keySet())
        {
            String value= String.valueOf(dataMap.get(key));
            builder.add(key,value);
        }

        RequestBody formBody=builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return request;
    }
    //创建带含有cookie的request
    public static Request createRequestWithCookie(String url, Map<String,String> dataMap, String cookie)
    {
        FormBody.Builder builder=new FormBody.Builder();
        for(String key: dataMap.keySet())
        {
            String value= String.valueOf(dataMap.get(key));
            builder.add(key,value);
        }

        RequestBody formBody=builder.build();
        Request request = new Request.Builder().addHeader("cookie",cookie).url(url).post(formBody).build();
        return request;
    }

    public static Request createRequestWithHeader(String url, String key, String value)
    {
        Request.Builder builder=new Request.Builder().url(url);
        builder.addHeader(key,value);
        Request request=builder.build();
        return request;
    }


    /**
     * 不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request)  {
        try {
            return mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * 从登录响应中获取头像的地址
     *
     * @param object
     * @return
     *//*
    public static String getObjectId(JSONObject object)//从登录响应中获取头像的地址
    {
        try {

            String data=dealJsonObjectItemMsg(object,"data");
            JSONObject dataObj=new JSONObject(data);
            String objectId=dealJsonObjectItemMsg(dataObj,"objectId");
            return objectId;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getCreatedDate(JSONObject object)
    {
        try {

            String data=dealJsonObjectItemMsg(object,"data");
            JSONObject dataObj=new JSONObject(data);
            String date=dealJsonObjectItemMsg(dataObj,"createdDate");
            return date;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getLastModifiedDate(JSONObject object)
    {
        try {

            String data=dealJsonObjectItemMsg(object,"data");
            JSONObject dataObj=new JSONObject(data);
            String date=dealJsonObjectItemMsg(dataObj,"lastModifiedDate");
            return date;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDeleted(JSONObject object)
    {
        try {

            String data=dealJsonObjectItemMsg(object,"data");
            JSONObject dataObj=new JSONObject(data);
            String date=dealJsonObjectItemMsg(dataObj,"deleted");
            return date;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getUsersPhone(JSONObject object)
    {
        try {

            String data=dealJsonObjectItemMsg(object,"data");
            JSONObject dataObj=new JSONObject(data);
            String date=dealJsonObjectItemMsg(dataObj,"users_Phone");
            return date;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //// TODO: 2017/2/8 此处还有许多信息没有填入 */
    public static String getData(JSONObject object) {
        try {

            String data=dealJsonObjectItemMsg(object,"data");

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getData(String object) {
        try {
            JSONObject s = new JSONObject(object);
            String data=dealJsonObjectItemMsg(s,"data");

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String changeURL(String url) {
        String newURL = url.replace(ComParameter.URL, ComParameter.URL);
        return newURL;
    }
    public static String changeURL2(String url) {
        if (!HttpUtils.needUpload(url)) {
            return "";
        }
//        int size = url.indexOf("/files/");
//        if (size == -1) {
////            在不符合截取条件时为避免报异常，直接返回原字符串。
//            return url;
//        } else {
//            String substr = url.substring(size);
//            String newURL = "http://123.206.49.101:8080/"+substr;
//            return newURL;
//        }
        return url;
    }

    /**
     * 是否需要上传，为空则不需要
     * @return
     */
    public static Boolean needUpload(String s) {
        if (s != null && s.length() != 0) {
            return true;
        } else {
            return false;
        }
    }


}