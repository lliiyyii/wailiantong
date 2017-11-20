package com.example.wailiantong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wailiantong.R;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class LiveDialog extends Dialog {

    private Button takePic;//身份证识别按钮
    private Button choosePic;//选择身份证按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private  OnTakePicclickListener onTakePicclickListener;//取消按钮被点击了的监听器
    private OnChoosePicOnclickListener onChoosePicOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *

     * @param onTakePicclickListener
     */
    public void setOnTakePicclickListener( OnTakePicclickListener onTakePicclickListener) {

        this.onTakePicclickListener = onTakePicclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *

     * @param onChoosePicOnclickListener
     */
    public void setOnChoosePicOnclickListener( OnChoosePicOnclickListener onChoosePicOnclickListener) {

        this.onChoosePicOnclickListener = onChoosePicOnclickListener;
    }

    public LiveDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_dialog_live);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);

        //初始化界面控件
        initView();
        //初始化界面数据
//        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTakePicclickListener != null) {
                    onTakePicclickListener.onTakePicClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoosePicOnclickListener != null) {
                    onChoosePicOnclickListener.onChoosePicClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
//    private void initData() {
//        //如果用户自定了title和message
//        if (titleStr != null) {
//            titleTv.setText(titleStr);
//        }
//        if (messageStr != null) {
//            messageTv.setText(messageStr);
//        }
//        //如果设置按钮的文字
//        if (yesStr != null) {
//            yes.setText(yesStr);
//        }
//        if (noStr != null) {
//            no.setText(noStr);
//        }
//    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        takePic = (Button) findViewById(R.id.live_dialog_take);
        choosePic = (Button) findViewById(R.id.live_dialog_choose);

    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }



    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnTakePicclickListener {
        public void onTakePicClick();
    }

    public interface OnChoosePicOnclickListener {
        public void onChoosePicClick();
    }
}