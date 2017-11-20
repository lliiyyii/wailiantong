package com.example.wailiantong.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wailiantong.MainActivity;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static com.example.wailiantong.Model.ComParameter.ISLOGINNO;
import static com.example.wailiantong.Model.ComParameter.ISLOGINYES;
import static com.example.wailiantong.Model.ComParameter.JSON;
import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by weike on 2017/7/9.
 */

public class LoginActivity extends BaseActivity {

    private EditText phoneNum;
    private EditText password;
    private Button loginBtn;
    private TextView active, forgetPwd;
    private ImageView showPwd;
    private boolean isShow = false;  //默认密码输入框不显示
    private String loginName, loginPwd, type, userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_login_layout);
        initView();
        setMonitor();
        if (Build.VERSION.SDK_INT > LOLLIPOP_MR1) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        0);
            }
        }
    }


    public void initView() {
        phoneNum = (EditText) findViewById(R.id.zsy_login_phone);
        password = (EditText) findViewById(R.id.zsy_login_pwd);
        loginBtn = (Button) findViewById(R.id.zsy_login_button);
        active = (TextView) findViewById(R.id.zsy_login_active);
        forgetPwd = (TextView) findViewById(R.id.zsy_login_forget);
        showPwd = (ImageView) findViewById(R.id.zsy_login_showpwd);


    }

    public void setMonitor() {
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
                //切换后将EditText光标置于末尾
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
                Intent activeIntent = new Intent(LoginActivity.this, ActiveActivity.class);
                startActivity(activeIntent);
            }
        });
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findPwdIntent = new Intent(LoginActivity.this, FindPwdActivity.class);
                startActivity(findPwdIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//                LoginActivity.this.finish();
                
                loginPwd = password.getText().toString();
                loginName = phoneNum.getText().toString();
                loginAct(loginName, loginPwd);
            }
        });

    }

    public void loginAct(String name, String pwd) {
        final String url = ComParameter.URL + ComParameter.LOGIN;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", name);
        params.put("pwd", pwd);
        final JSONObject jsonObject = new JSONObject(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url).post(requestbody).build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    net.sf.json.JSONObject mobject = net.sf.json.JSONObject.fromObject(responseData);

                    if (mobject.getInt("status") == 1) {
                        makeToast("您登陆成功！");
                        Logger.i("打印登录返回的日志", mobject.toString());

                        userId=mobject.getJSONObject("data").getString("user_id");
                        loginName=mobject.getJSONObject("data").getString("phone");
                        type=mobject.getJSONObject("data").getInt("type")+"";

                        CacheUtils cacheUtils = new CacheUtils(LoginActivity.this, "UserInfo");
                        cacheUtils.putValue("userId", userId);//将sessionId放入缓存中phone type
                        cacheUtils.putValue("phone", loginName);
                        cacheUtils.putValue("userType", type);

                        cacheUtils.putValue("isActive",ISLOGINNO);
                        cacheUtils.putValue("isLogin", ISLOGINYES);
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        LoginActivity.this.finish();
                    } else {
                        makeToast(mobject.getJSONObject("data").getString("msg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();

    }
}
