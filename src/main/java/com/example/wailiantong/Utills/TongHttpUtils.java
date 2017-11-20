package com.example.wailiantong.Utills;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.wailiantong.Model.ComParameter;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by weike on 2017/7/9.
 */

public class TongHttpUtils {


    /**
     * 账号激活工具
     * @param phone
     * @param pwd
     * @param code
     * @param mImei
     * @param handler
     */
    public void activeSend(String phone, String pwd, String code, String mImei, final Handler handler) {
        final String url = ComParameter.URL + ComParameter.ACTIVE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", phone);
        params.put("password", pwd);
        params.put("authorization_code", code);
        params.put("IMEI", mImei);
        final JSONObject jsonObject = new JSONObject(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Logger.i("激活发送的数据", jsonObject.toString());
                    Request request = new Request.Builder().url(url).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("激活返回的数据", responseData);
                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);
                    if (mobject.getInt("status")==1){
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    else ToastUtils.makeToast("输入数据有误");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }

    /**
     * 登录后获取个人信息
     * @param mActivity
     */
    public void getInfo(Activity mActivity) {
        final Activity activity = mActivity;
        final String header = new CacheUtils(activity, "UserInfo").getValue("header", "");
        final String getInfoUrl = ComParameter.URL + ComParameter.GETINFO;
        Logger.i("打印请求业务员信息的地址", getInfoUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(getInfoUrl).addHeader("Cookie", header).get().build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseData);
                    if (object.getInt("status") == 1) {
                        Logger.i("打印请求业务员信息的请求头", request.toString());
                        Logger.i("打印请求成功返回的日志", responseData);
                        CacheUtils cacheUtils = new CacheUtils(activity, "UserInfo");
                        cacheUtils.putValue("id", object.getJSONObject("data").getInt("id"));//将业务员id放入缓存中
                        cacheUtils.putValue("name", object.getJSONObject("data").getString("name"));//将业务员姓名放入缓存中
                        cacheUtils.putValue("gender", object.getJSONObject("data").getString("gender"));//将业务员性别放入缓存中
                        cacheUtils.putValue("license_num", object.getJSONObject("data").getString("license_num"));//将业务员证件号放入缓存中
                        cacheUtils.putValue("IMEI", object.getJSONObject("data").getString("IMEI"));//将账号绑定的手机标识码放入缓存中
                        cacheUtils.putValue("notary_office_id", object.getJSONObject("data").getInt("notary_office_id"));//将账号绑定的手机标识码放入缓存中
                        cacheUtils.putValue("phone_num", object.getJSONObject("data").getJSONObject("user").getString("username") + "");//将账号绑定的手机标识码放入缓存中
                        Logger.i("打印手机号码", object.getJSONObject("data").getJSONObject("user").getString("username") + "2222222222");
                    } else {
                        Logger.i("获取业务员信息失败", "11111111111");
                    }
                } catch (IOException e) {
                    Logger.i("获取业务员信息失败", "11111111111");
                    // makeToast("获取业务员信息失败");
                }
                Looper.loop();

            }
        }).start();
    }
    public void saveOnline(final String murl,String name, String sex, final String header) {
//        String sex;
//        if (gender == 1) {
//            sex = "男";
//        } else sex = "女";


//        final String url = murl + mId + "/";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("gender", sex);
        final JSONObject jsonObject = new JSONObject(params);


        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Logger.i("保存用户信息发送的请求",murl+header+jsonObject.toString()+"");
                    Request request = new Request.Builder().url(murl).addHeader("Cookie", header).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("保存用户信息返回的信息",responseData);

                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);
                    if (mobject.getInt("status") == 1) {
                        makeToast("信息保存成功");


                    } else {
                        makeToast("保存失败");

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }
    public void getPhoneCode(String phoneNum, final Handler handler){
        final String url = ComParameter.URL + ComParameter.GETCODE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", phoneNum);
        final JSONObject jsonObject = new JSONObject(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {

                    OkHttpClient client = new OkHttpClient();
                    Logger.i("请求验证码发送的信息",jsonObject.toString());
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseHeader = response.headers().get("Set-Cookie");
                    String forgetHeader = StringUtils.substringBefore(responseHeader, ";");
                    handler.obtainMessage(3, (Object) forgetHeader).sendToTarget();   //发送id为3的消息把forgetHeader发过去
                    String responseData = response.body().string();
                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);
                    makeToast(mobject.getString("msg"));
                    Logger.i("返回请求验证码的数据",responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }

    public void resetPwd(String name, String pwd, String code,final String header,final Handler handler) {
        final String url = ComParameter.URL + ComParameter.RESETPWD;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", name);
        params.put("new_passwd", pwd);
        params.put("ver", code);
        final JSONObject jsonObject = new JSONObject(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 在子线程中展示Toast
                 * looper.prepare();
                 * makeToast();
                 * looper.loop();
                 */
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Logger.i("修改密码发送信息",jsonObject.toString()+"");
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).addHeader("Cookie", header).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("修改密码返回信息",responseData+"");
                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);
                    if (mobject.getInt("status") == 1) {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    } else makeToast("验证码错误");
                    Logger.i("修改密码返回的数据", responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

}
