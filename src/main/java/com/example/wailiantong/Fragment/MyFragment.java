package com.example.wailiantong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.Activity.MyInfoDetail;
import com.example.wailiantong.Activity.ResetPwdActivity;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;

/**
 * Created by weike on 2017/6/12.
 */

public class MyFragment extends Fragment {
    RelativeLayout myInfo, resetPwd,exit;
    TextView name,phone;
    CacheUtils cacheUtils;

    public static MyFragment newInstance() {
        Bundle bundle = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zsy_fragment_my_layout, container, false);
        cacheUtils=new CacheUtils(getContext(),"UserInfo");

        myInfo = (RelativeLayout) view.findViewById(R.id.zsy_frag_my_info);
        resetPwd = (RelativeLayout) view.findViewById(R.id.zsy_frag_my_changepwd);
        name= (TextView) view.findViewById(R.id.zsy_frag_my_name);
        phone= (TextView) view.findViewById(R.id.zsy_frag_my_phone);

        name.setText(cacheUtils.getValue("companyName","未激活"));
        phone.setText(cacheUtils.getValue("phone",""));

        exit= (RelativeLayout) view.findViewById(R.id.zsy_frag_my_exit);

        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("11111111111111", "11111111111111111");
                Intent infoIntent = new Intent(getActivity(), MyInfoDetail.class);
                startActivity(infoIntent);
            }
        });
        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetIntent = new Intent(getActivity(), ResetPwdActivity.class);
                startActivity(resetIntent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent reloginIntent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(reloginIntent);
                getActivity().finish();
            }
        });

        return view;
    }


}

