package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.wailiantong.R;
import com.example.wailiantong.Utills.titleMannager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class CalendarActivity extends BaseActivity{
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_calendar_layout);
        titleMannager title = new titleMannager(CalendarActivity.this);
        title.setTitle("选择日期");
        title.titleSetOnClickListener(this);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.materialCalendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String theDate = getTheDate(date);
                Intent intent = new Intent();
                intent.putExtra("data", theDate);
                setResult(-1,intent);
                finish();
            }
        });
    }
    /**
     * 获取点击后的日期数
     */
    public String getTheDate(CalendarDay currentDate) {
        if (currentDate != null) {
            int year = currentDate.getYear();
            int month = currentDate.getMonth() + 1; //月份跟系统一样是从0开始的，实际获取时要加1
            int day = currentDate.getDay();
            return year + "-" + month + "-" + day;
        } else {
            return "";
        }
    }

}
