package com.example.wailiantong.Utills;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.R;




public class titleMannager {

    private ImageView back_btn;
    private TextView title_text;
    private RelativeLayout back_layout;

    public titleMannager(Activity context){

        back_btn = (ImageView) context.findViewById(R.id.title_back);
        title_text = (TextView) context.findViewById(R.id.top_title);
        back_layout = (RelativeLayout) context.findViewById(R.id.title_back_layout);
    }

    public titleMannager setTitle(String str){
        title_text.setText(str);
        return this;
    }

    public void titleSetOnClickListener(final Activity activity){

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Logger.i("执行了点击","执行了点击");
            }
        });


    }

}
