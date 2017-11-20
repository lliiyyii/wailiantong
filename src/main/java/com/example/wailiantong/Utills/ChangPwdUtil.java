package com.example.wailiantong.Utills;

import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by weike on 2017/7/10.
 */

public class ChangPwdUtil {

    public void changeButton(final Handler handler,final Timer timer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
               //   在特定延迟后计划重复固定延迟执行的任务。间隔为1s
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

    public boolean theSameEdit(EditText first,EditText second) {

        String firstText = first.getText().toString();
        String secondText = second.getText().toString();
        if (firstText.equals(secondText)) {
            return true;
        } else return false;
    }

}
