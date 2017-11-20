package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;

/**
 * Created by 蔚克 on 2017/11/19.
 */

public class ActiveResultID extends BaseActivity {
    RelativeLayout idLayout, businessLayout;
    String partyName, certificateNum, companyName, companyCode;
    Button commitBtn;
    TextView partyNameTx, certificateNumTx, companyNameTx, companyCodeTx;
    String type = "";  //  1代表是身份证识别   0代表是营业执照识别
    CacheUtils cacheUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsy_active_result_layout);
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        type = getIntent().getStringExtra("type");
        initView();
    }

    private void initView() {
        partyNameTx = (TextView) findViewById(R.id.active_viewid_name);
        certificateNumTx = (TextView) findViewById(R.id.active_viewid_id);
        companyNameTx = (TextView) findViewById(R.id.active_viewbusiness_name);
        companyCodeTx = (TextView) findViewById(R.id.active_viewbusiness_id);

        idLayout = (RelativeLayout) findViewById(R.id.active_result_id_layout);
        businessLayout = (RelativeLayout) findViewById(R.id.active_result_business_layout);

        commitBtn = (Button) findViewById(R.id.active_result_commit);

        switch (type) {
            case "1":
                partyName = cacheUtils.getValue("partyName", "");
                certificateNum = cacheUtils.getValue("certificateNum", "");
                partyNameTx.setText(partyName);
                certificateNumTx.setText(certificateNum);
                idLayout.setVisibility(View.VISIBLE);
                businessLayout.setVisibility(View.GONE);
                break;
            case "0":
                companyCode = cacheUtils.getValue("companyCode", "");
                companyName = cacheUtils.getValue("companyName", "");
                companyNameTx.setText(companyName);
                companyCodeTx.setText(companyCode);
                idLayout.setVisibility(View.GONE);
                businessLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type){
                    case "1":
                        Intent idIntent=new Intent(ActiveResultID.this,ActiveCardId.class);
                        getActivity().finish();
                        startActivity(idIntent);
                        break;
                    case "0":
                        Intent businessIntent=new Intent(ActiveResultID.this,ActiveBusiness.class);
                        getActivity().finish();
                        startActivity(businessIntent);
                        break;
                    default:
                        break;
                }
            }
        });


    }


}
