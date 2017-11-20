package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.ActivityCollector;
import com.example.wailiantong.Utills.ChangPwdUtil;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.ToastUtils;
import com.example.wailiantong.Utills.TongHttpUtils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by 蔚克 on 2017/6/10.
 */

public class FindPwdActivity extends BaseActivity {
    private EditText phoneNum, newPwd, newPwdTwice, code;
    private Button findPwd, getCode;
    private Timer timer;
    private String mphoneNum, mNewPwd, mNewPwdAgain, mCode;
    private int time = 60;
    private boolean isSame;
    private String forgetHeader;
    private TongHttpUtils utils = new TongHttpUtils();
    private ChangPwdUtil pwdUtil = new ChangPwdUtil();
    private ImageView back;
    private Handler handler = new Handler() { //修改倒计时


        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    if (time > 0) {
                        Logger.i("这是时间的打印", Integer.valueOf(time).toString());
                        getCode.setClickable(false);
                        getCode.setText(String.valueOf(time));
                        time--;
                    } else {
                        time = 60;
                        getCode.setText("获取验证码");
                        getCode.setClickable(true);
                        timer.cancel();
                    }
                    break;

                case 2:
                    //将之前的页面清除，避免可以用返回键返回上一个页面
                    ActivityCollector collector = new ActivityCollector();
                    collector.finishAll();


                    Intent reLogin = new Intent(FindPwdActivity.this, LoginActivity.class);
                    startActivity(reLogin);
                    FindPwdActivity.this.finish();
                    ToastUtils.makeToast(getContext(), "修改成功，请您重新登录");
                    break;
                case 3:
                    forgetHeader = (String) msg.obj;

            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_find_pwd_layout);
        initView();
        setListen();
    }



    public void initView() {

        phoneNum = (EditText) findViewById(R.id.zsy_find_pwd_phone);
        newPwd = (EditText) findViewById(R.id.zsy_find_pwd_new);
        newPwdTwice = (EditText) findViewById(R.id.zsy_find_pwd_newagain);
        code = (EditText) findViewById(R.id.zsy_find_pwd_code);

        findPwd = (Button) findViewById(R.id.zsy_find_pwd_btn);
        getCode = (Button) findViewById(R.id.zsy_find_pwd_getcode);

        back = (ImageView) findViewById(R.id.zsy_find_pwd_back);

    }

    public void setListen() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeButton();
                timer=new Timer();
                pwdUtil.changeButton(handler,timer);
                mphoneNum = phoneNum.getText().toString();
                utils.getPhoneCode(mphoneNum, handler);
                //  getmCode(mphoneNum);
            }
        });

        findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isSame();

                if (pwdUtil.theSameEdit(newPwd, newPwdTwice)) {
                    mphoneNum = phoneNum.getText().toString();
                    mCode = code.getText().toString();
                    mNewPwd = newPwd.getText().toString();
                    utils.resetPwd(mphoneNum, mNewPwd, mCode, forgetHeader, handler);
//                    reset(mphoneNum, mNewPwd, mCode);

                } else ToastUtils.makeToast(getContext(), "您两次输入的密码不一致，请检查并重新输入");

            }
        });

    }


    /**
     * 改变发送验证码按钮
     */
    public void changeButton() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();//   在特定延迟后计划重复固定延迟执行的任务。间隔为1s
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }, 0, 1000);

            }
        }).start();

    }

    public boolean isSame() {
        mphoneNum = phoneNum.getText().toString();
        mCode = code.getText().toString();
        mNewPwd = newPwd.getText().toString();
        mNewPwdAgain = newPwdTwice.getText().toString();
        if (mNewPwd.equals(mNewPwdAgain)) {
            isSame = true;
        } else isSame = false;
        return isSame;
    }

    public void reset(String name, String pwd, String code) {
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
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).addHeader("Cookie", forgetHeader).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
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

    public void getmCode(String phoneNum) {
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
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).post(requestbody).build();
                    Response response = client.newCall(request).execute();
                    String responseHeader = response.headers().get("Set-Cookie");
                    forgetHeader = StringUtils.substringBefore(responseHeader, ";");
                    String responseData = response.body().string();
                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);
                    makeToast(mobject.getString("msg"));
                    Logger.i("返回请求验证码的数据", responseData);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }
}
