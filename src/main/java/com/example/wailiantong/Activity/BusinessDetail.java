package com.example.wailiantong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.CacheUtils;
import com.example.wailiantong.Utills.Logger;

import net.sf.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.wailiantong.Utills.ToastUtils.makeToast;

/**
 * Created by weike on 2017/6/13.
 */

public class BusinessDetail extends BaseActivity {
    private ImageView back;
    private String header, position;
    private String type, partyName, certificate_num, representName, deadTime,
            address, viewPic, picFirst, picSecond, person, cost, nation, office, cardDeadline,confidence;
    private TextView mType, mPartyName, mRepersentName, mViewpic;
    private TextView mDeadTime, mCode, mCompanyAdress, mCost;
    private TextView mCertificate_num, mNation, mAdress, mCardDeadline, mOffice;
    private TextView mConfidence,mliveNum;
    private CacheUtils cacheUtils;
    private LinearLayout business, idCard,confidenceLayout;
    private RelativeLayout back_layout;


    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initTextBusiness();
                    break;
                case 2:
                    initTextCard();
                    break;

                case 3:
                    initLive();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheUtils = new CacheUtils(getContext(), "UserInfo");
        header = cacheUtils.getValue("header", "");
        position = getIntent().getStringExtra("position");
        getDetail();
        setContentView(R.layout.business_detail_layout);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.zsy_businessdetail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        business = (LinearLayout) findViewById(R.id.business_detail_business);
        idCard = (LinearLayout) findViewById(R.id.business_detail_cardid);
        confidenceLayout= (LinearLayout) findViewById(R.id.business_detail_confidencelayout);
//        back_layout= (RelativeLayout) findViewById(R.id.business_layout_back);
//        back_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        //相同字段
        mType = (TextView) findViewById(R.id.business_detail_type);
        mPartyName = (TextView) findViewById(R.id.business_detail_party);
        mViewpic = (TextView) findViewById(R.id.business_detail_pic);


        //营业执照字段
        mRepersentName = (TextView) findViewById(R.id.business_detail_company_people);
        mDeadTime = (TextView) findViewById(R.id.business_detail_deadline);
        mCode = (TextView) findViewById(R.id.business_detail_code_business);
        mCompanyAdress = (TextView) findViewById(R.id.business_detail_adress_business);
        mCost = (TextView) findViewById(R.id.business_detail_cost_business);


        //身份证字段
        mCertificate_num = (TextView) findViewById(R.id.business_detail_code);
        mNation = (TextView) findViewById(R.id.business_detail_nation);
        mAdress = (TextView) findViewById(R.id.business_detail_liveadress);
        mCardDeadline = (TextView) findViewById(R.id.business_detail_carddeadline);
        mOffice = (TextView) findViewById(R.id.business_detail_cardoffice);

        //活体识别字段
        mConfidence= (TextView) findViewById(R.id.business_detail_confidence);
        mliveNum= (TextView) findViewById(R.id.business_detail_liveidnum);
    }

    private void initTextBusiness() {
        business.setVisibility(View.VISIBLE);
        mType.setText(type);
        mPartyName.setText(partyName);
        mViewpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewDetailPicIntent = new Intent(BusinessDetail.this, PhotoLoading.class);
                viewDetailPicIntent.putExtra("picFirst", picFirst + "");
                viewDetailPicIntent.putExtra("picSecond", picSecond + "");
                startActivity(viewDetailPicIntent);
            }
        });

        //营业执照字段
        mCode.setText(certificate_num);
        mRepersentName.setText(representName);
        mCost.setText(cost);
        mDeadTime.setText(deadTime);
        mCompanyAdress.setText(address);


    }

    private void initTextCard() {
        idCard.setVisibility(View.VISIBLE);
        mType.setText(type);
        mPartyName.setText(partyName);
        mViewpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewDetailPicIntent = new Intent(BusinessDetail.this, PhotoLoading.class);
                viewDetailPicIntent.putExtra("picFirst", picFirst + "");
                viewDetailPicIntent.putExtra("picSecond", picSecond + "");
                startActivity(viewDetailPicIntent);
            }
        });
        mCertificate_num.setText(certificate_num);
        mAdress.setText(address);
        mNation.setText(nation);
        mCardDeadline.setText(cardDeadline);
        mOffice.setText(office);
    }
private void initLive(){
    confidenceLayout.setVisibility(View.VISIBLE);
    mType.setText(type);
    mPartyName.setText(partyName);
    mViewpic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent viewDetailPicIntent = new Intent(BusinessDetail.this, PhotoLoading.class);
            viewDetailPicIntent.putExtra("picFirst", picFirst + "");
            viewDetailPicIntent.putExtra("picSecond", picSecond + "");
            startActivity(viewDetailPicIntent);
        }
    });
    mConfidence.setText(confidence);
    mliveNum.setText(certificate_num);
}




    public void getDetail() {
        final String getInfoDetailUrl = ComParameter.URL + ComParameter.GETLISTDETAIL + position;
        Logger.i("打印请求业务详情的地址", getInfoDetailUrl);
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
                    Request request = new Request.Builder().url(getInfoDetailUrl).addHeader("Cookie", header).get().build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Logger.i("打印每次详情返回的数据", responseData);
                    if (responseData.equals("{\"detail\":\"Not found.\"}")) {
                        makeToast("请求数据失败");
                    } else {
                        JSONObject object = JSONObject.fromObject(responseData);
                        type = object.getJSONObject("data").getString("type") + "";
                        partyName = object.getJSONObject("data").getString("party_name") + "";
                        certificate_num = object.getJSONObject("data").getString("certificate_num") + "";

                        picFirst = object.getJSONObject("data").getString("pic_1") + "";
                        picSecond = object.getJSONObject("data").getString("pic_2") + "";
                        if (type.equals("营业执照文字识别")) {

                            address = object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("address") + "";
                            representName = object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("person") + "";
                            cost = object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("capital") + "";
                            deadTime = object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("valid_period") + "";
                            Message message = new Message();
                            message.what = 1;
                            mainHandler.sendMessage(message);
                        } else if(type.equals("身份证文字识别")){
                            address = object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("address") + "";
                            nation = object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("nationality") + "";
                            office = object.getJSONObject("data").getJSONArray("result").getJSONObject(1).getString("issue") + "";
                            cardDeadline = object.getJSONObject("data").getJSONArray("result").getJSONObject(1).getString("end_date") + "止";
                            Message message2 = new Message();
                            message2.what = 2;
                            mainHandler.sendMessage(message2);
                        }else {
                            Logger.i("打印类型",type+"");
                            if (type.equals("活体检测+照片比对")) type="真人与证件照对比";
                            if (type.equals("活体检测+身份证核身"))type="真人与公安库对比";
                            Logger.i("打印识别结果",type+""+object.getJSONObject("data").getJSONArray("result").toString());
                            confidence=object.getJSONObject("data").getJSONArray("result").getJSONObject(0).getString("confidence")+"";
                            Message message3 = new Message();
                            message3.what = 3;
                            mainHandler.sendMessage(message3);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

}
