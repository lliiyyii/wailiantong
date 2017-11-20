package com.example.wailiantong.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.ChangPwdUtil;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.ToastUtils;
import com.example.wailiantong.Utills.TongHttpUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by 张 on 2017/7/10.
 */

public class ActiveActivity extends BaseActivity {

    private EditText phoneNum, password, activeCode;
    private RelativeLayout businessLayout,studentLayout;
    private Button active,getCode;
    private ImageView showPwd,studentImg,businessImg;
    private TextView login, forgetPwd;
    private String mPhone, mPwd, mCode, type="0";  //type 1表示商家，0表示学生
    private boolean isShow = false;  //默认密码输入框不显示
    private int time = 60;
    private TongHttpUtils utils = new TongHttpUtils();
    private ChangPwdUtil pwdUtil = new ChangPwdUtil();
    private Timer timer;
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
                    ToastUtils.makeToast("注册成功");
                    ActiveActivity.this.finish();
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
//        imei = telephonyManager.getDeviceId();
//        imei="123456";
//        Logger.i("打印手机唯一标识码", imei);
        setContentView(R.layout.zsy_active_layout);
        initView();
        setMonitor();
    }



    public void initView() {
        phoneNum = (EditText) findViewById(R.id.zsy_active_phone);
        password = (EditText) findViewById(R.id.zsy_active_pwd);
        activeCode = (EditText) findViewById(R.id.zsy_active_code);
        active = (Button) findViewById(R.id.zsy_active_button);
        getCode= (Button) findViewById(R.id.zsy_active_getcode);
        showPwd = (ImageView) findViewById(R.id.zsy_active_showpwd);
        login = (TextView) findViewById(R.id.zsy_active_login);
        forgetPwd = (TextView) findViewById(R.id.zsy_active_forget);
        businessLayout= (RelativeLayout) findViewById(R.id.zsy_active_business_layout);
        studentLayout= (RelativeLayout) findViewById(R.id.zsy_active_student_layout);
        studentImg= (ImageView) findViewById(R.id.zsy_active_student_img);
        businessImg= (ImageView) findViewById(R.id.zsy_active_business_img);
    }

    public void setMonitor() {
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer=new Timer();
                pwdUtil.changeButton(handler,timer);
                mPhone = phoneNum.getText().toString();
//                getPhoneCode(mPhone);
            }
        });
        businessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessImg.setImageResource(R.mipmap.select_selected);
                studentImg.setImageResource(R.mipmap.select_unselected);
                type="1";
            }
        });
        studentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentImg.setImageResource(R.mipmap.select_selected);
                businessImg.setImageResource(R.mipmap.select_unselected);
                type="0";
            }
        });
        //密码框可以隐藏或者显示密码
        showPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                if (isShow) {
                    //点击后使密码显示
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPwd.setImageResource(R.mipmap.display_pwd);
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPwd.setImageResource(R.mipmap.hide_pwd);
                }
                CharSequence charSequence = password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = phoneNum.getText().toString();
                mPwd = password.getText().toString();
                mCode = activeCode.getText().toString();

                activeSend(mPhone,mPwd,mCode,type);
//                TongHttpUtils utils = new TongHttpUtils();
//                utils.activeSend(mPhone, mPwd, mCode, imei,handler);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveActivity.this.finish();
            }
        });

        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findPwdIntent = new Intent(ActiveActivity.this, FindPwdActivity.class);
                startActivity(findPwdIntent);
            }
        });
    }



    public void activeSend(String phone, String pwd, String code,String type) {
        final String url = ComParameter.URL + ComParameter.ACTIVE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("pwd", pwd);
        params.put("code", code);
        params.put("type",type);
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
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                    else ToastUtils.makeToast(mobject.getString("msg"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
    }
    public void getPhoneCode(String phoneNum){
        final String url = ComParameter.URL + ComParameter.GETCODE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneNum);
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
}
