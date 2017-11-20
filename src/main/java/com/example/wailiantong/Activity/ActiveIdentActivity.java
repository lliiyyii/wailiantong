package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;

/**
 * Created by 蔚克 on 2017/11/19.
 */

public class ActiveIdentActivity extends BaseActivity {
    private Boolean firstVerify=false,secondVerify=false,finalVerify=false;
    private RelativeLayout idRelativelayout,businessRelativelayout;
    private ImageView firstTick,secondTick;
    private Button commitBtn;
    private CacheUtils cacheUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_active_identent_layout);
        cacheUtils=new CacheUtils(getContext(),"UserInfo");
        initView();
        changeVerify();
    }
    public void initView(){
        idRelativelayout= (RelativeLayout) findViewById(R.id.acitive_ident_id);
        idRelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstVerify){
                    Intent idIntent=new Intent(ActiveIdentActivity.this,ActiveResultID.class);
                    idIntent.putExtra("type","1");
                    startActivity(idIntent);
                }else {
                    Intent idIntent=new Intent(ActiveIdentActivity.this,ActiveCardId.class);
                    startActivity(idIntent);
                }

            }
        });

        businessRelativelayout= (RelativeLayout) findViewById(R.id.acitive_ident_business);
        businessRelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secondVerify){
                    Intent idIntent=new Intent(ActiveIdentActivity.this,ActiveResultID.class);
                    idIntent.putExtra("type","0");
                    startActivity(idIntent);
                }else {
                    Intent idIntent=new Intent(ActiveIdentActivity.this,ActiveBusiness.class);
                    startActivity(idIntent);
                }
            }
        });
        firstTick= (ImageView) findViewById(R.id.acitive_ident_id_tick);
        secondTick= (ImageView) findViewById(R.id.acitive_ident_business_tick);
        commitBtn= (Button) findViewById(R.id.active_ident_commit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUtils cacheUtils = new CacheUtils(ActiveIdentActivity.getContext(),"UserInfo");
                cacheUtils.putValue("isActive",true);
                getActivity().finish();
            }
        });
        changeVerify();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeVerify();

    }
    private void changeVerify(){
        if (!(cacheUtils.getValue("partyName","").equals(""))) {
            firstVerify =true;
            firstTick.setVisibility(View.VISIBLE);
        }
        if (!(cacheUtils.getValue("companyName","").equals(""))){
            secondVerify=true;
            secondTick.setVisibility(View.VISIBLE);
        }
        if (secondVerify&&firstVerify){

            commitBtn.setVisibility(View.VISIBLE);


//            if (cacheUtils.getValue("partyName","0").equals(cacheUtils.getValue("companyPerson","1")))
//            {
//                commitBtn.setVisibility(View.VISIBLE);
//            }
//            else {
//                commitBtn.setVisibility(View.VISIBLE);
//                commitBtn.setClickable(false);
//                commitBtn.setText("对不起，验证未通过，请重试");
//            }
        }



    }
}
