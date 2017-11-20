package com.example.wailiantong.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.example.wailiantong.Activity.TakeCardId;
import com.example.wailiantong.Activity.TakeBusiness;
import com.example.wailiantong.Activity.TemporaryActivity;
import com.example.wailiantong.Activity.LiveSearchMain;
import com.example.wailiantong.Dialog.LiveDialog;
import com.example.wailiantong.Model.ComParameter;
import com.example.wailiantong.R;
import com.example.wailiantong.Utills.ErrorResponse;
import com.example.wailiantong.Utills.HttpUtils;
import com.example.wailiantong.Utills.Logger;
import com.example.wailiantong.Utills.OcridCared;
import com.example.wailiantong.Utills.ToastUtils;
import com.google.gson.Gson;
import com.megvii.idcardproject.LoadingActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by 蔚克 on 2017/6/11.
 */

public class MainFragment extends Fragment {
    String name = "";
    String idcard_number = "";

    OcridCared ocridCared12;
    OcridCared ocridCared3;
    String PortraitImg = "";
    String idcardImg = "";
    String RealFilePath = "";
    String delta = "";
    public static int IDCard = 101;
    public static int LIVE = 102;
    public static int IDCard2 = 103;
    public static int LIVE2 = 104;
    public static int IDCard3 = 105;
    public static int LIVE3 = 106;
    RelativeLayout livingTestPic, livingTestVideo, idCardIden, licenseIden, houseIden;
    private AlertDialog pic_dialog;
    private LiveDialog liveDialog;
    private Button btn;

    public static MainFragment newInstance() {
        Bundle bundle = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zsy_fragment_home_layout, container, false);

        livingTestPic = (RelativeLayout) view.findViewById(R.id.zsy_home_test1);
        livingTestVideo = (RelativeLayout) view.findViewById(R.id.zsy_home_test2);
        idCardIden = (RelativeLayout) view.findViewById(R.id.zsy_home_test3);
        licenseIden = (RelativeLayout) view.findViewById(R.id.zsy_home_test4);
        houseIden = (RelativeLayout) view.findViewById(R.id.zsy_home_test5);

        setLisen();
        return view;
    }

    public void setLisen() {
        livingTestPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("点击了活体","000000");
                liveDialog = new LiveDialog(getContext());
                liveDialog.show();

                liveDialog.setOnTakePicclickListener(new LiveDialog.OnTakePicclickListener() {
                    @Override
                    public void onTakePicClick() {

                        Intent IdIdenIntent = new Intent(getActivity(), LoadingActivity.class);
                        IdIdenIntent.putExtra("intent_data", 1);
                        startActivityForResult(IdIdenIntent, IDCard);
                        liveDialog.dismiss();
                    }
                });
                liveDialog.setOnChoosePicOnclickListener(new LiveDialog.OnChoosePicOnclickListener() {
                    @Override
                    public void onChoosePicClick() {

                        Intent IdIdenIntent = new Intent(getActivity(), LiveSearchMain.class);
                        startActivity(IdIdenIntent);
                        liveDialog.dismiss();
                    }

                });


            }
        });


        livingTestVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                liveDialog = new LiveDialog(getContext());
                liveDialog.show();

                liveDialog.setOnTakePicclickListener(new LiveDialog.OnTakePicclickListener() {
                    @Override
                    public void onTakePicClick() {
                        Intent IdIdenIntent = new Intent(getActivity(), LoadingActivity.class);
                        IdIdenIntent.putExtra("intent_data", 2);
                        startActivityForResult(IdIdenIntent, IDCard2);
                        liveDialog.dismiss();
                    }
                });
                liveDialog.setOnChoosePicOnclickListener(new LiveDialog.OnChoosePicOnclickListener() {
                    @Override
                    public void onChoosePicClick() {
                        Intent IdIdenIntent = new Intent(getActivity(), LiveSearchMain.class);
                        IdIdenIntent.putExtra("intent_data", 2);
                        startActivityForResult(IdIdenIntent, IDCard3);
                        liveDialog.dismiss();
                    }

                });

            }
        });

        idCardIden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IdCardIdenIntent = new Intent(getActivity(), TakeCardId.class);
                startActivity(IdCardIdenIntent);

            }
        });
        licenseIden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent licenseIdenIntent = new Intent(getActivity(), TakeBusiness.class);
                startActivity(licenseIdenIntent);

            }

        });
        houseIden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent houseIdenIntent = new Intent(getActivity(),TakeHouse.class);
//                Logger.i("点击了户口本识别","aaaaaaaaaaaaaaaaaaaa");
//                startActivity(houseIdenIntent);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("          正在研发中...敬请期待！  ");
                guestDialog = builder.create();
                guestDialog.show();
            }
        });
    }

    AlertDialog guestDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IDCard && resultCode == 199) {
            PortraitImg = data.getStringExtra("portraitImg");
            idcardImg = data.getStringExtra("idcardImg");
            Intent IdIdenIntent = new Intent(getActivity(), com.megvii.livenessproject.LoadingActivity.class);
            startActivityForResult(IdIdenIntent, LIVE);
        }
        if (requestCode == LIVE && resultCode == 200) {
            RealFilePath = data.getStringExtra("RealFilePath");
            delta = data.getStringExtra("delta");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("正在加载中...");
            guestDialog = builder.create();
            guestDialog.show();
            getIDInfo2();
//            verify();
//            Intent IdIdenIntent = new Intent(getActivity(), com.example.livenessproject.LoadingActivity.class);
//            startActivityForResult(IdIdenIntent,LIVE);
        }
        if (requestCode == IDCard2 && resultCode == 199) {
//            PortraitImg = data.getStringExtra("portraitImg");
            idcardImg = data.getStringExtra("idcardImg");
            Intent IdIdenIntent = new Intent(getActivity(), com.megvii.livenessproject.LoadingActivity.class);
            startActivityForResult(IdIdenIntent, LIVE2);
//            com.megvii.livenesslib.util.Logger.i("99999999999999999", data.getStringExtra("RealFilePath"));
//            RealFilePath = data.getStringExtra("RealFilePath");
//            delta = data.getStringExtra("delta");
//            verify();
//            Intent IdIdenIntent = new Intent(getActivity(), com.example.livenessproject.LoadingActivity.class);
//            startActivityForResult(IdIdenIntent,LIVE);
        }
        if (requestCode == LIVE2 && resultCode == 200) {
            Logger.i("99999999999999999", data.getStringExtra("RealFilePath"));
            RealFilePath = data.getStringExtra("RealFilePath");
            delta = data.getStringExtra("delta");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("请稍候...");
            guestDialog = builder.create();
            guestDialog.show();
            getIDInfo();
//            Intent IdIdenIntent = new Intent(getActivity(), com.example.livenessproject.LoadingActivity.class);
//            startActivityForResult(IdIdenIntent,LIVE);
        }
        if (requestCode == IDCard3 && resultCode == 201) {
//            com.megvii.livenesslib.util.Logger.i("99999999999999999", data.getStringExtra("RealFilePath"));

            name = data.getStringExtra("name");
            idcard_number = data.getStringExtra("idcard_number");
            ocridCared12 = new OcridCared(idcard_number, name);
            Intent IdIdenIntent = new Intent(getActivity(), com.megvii.livenessproject.LoadingActivity.class);
            startActivityForResult(IdIdenIntent, LIVE3);
//            verify2(ocridCared);
//            Intent IdIdenIntent = new Intent(getActivity(), com.example.livenessproject.LoadingActivity.class);
//            startActivityForResult(IdIdenIntent,LIVE);
        }
        if (requestCode == LIVE3 && resultCode == 200) {
            Logger.i("99999999999999999", data.getStringExtra("RealFilePath"));
            RealFilePath = data.getStringExtra("RealFilePath");
            delta = data.getStringExtra("delta");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("正在加载中...");
            guestDialog = builder.create();
            guestDialog.show();
            verify2(ocridCared12);
//            Intent IdIdenIntent = new Intent(getActivity(), com.example.livenessproject.LoadingActivity.class);
//            startActivityForResult(IdIdenIntent,LIVE);
        }
    }

    private void getIDInfo2() {
        if (ComParameter.useStr(idcardImg)) {
            File PortraitImgFile = new File(idcardImg);
            if (PortraitImgFile == null) {
                Logger.e("kkkkkkkkkkkkkkkkkkkkkkkkkkk", "文件为空");
                return;
            }
            String url = ComParameter.FACEID_URL + ComParameter.OCRIDCARD;
            //构造上传请求，类似web表单
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("api_key", getResources().getString(R.string.api_key))
                    .addFormDataPart("api_secret", getResources().getString(R.string.api_secret))
                    .addFormDataPart("image", PortraitImgFile.getName(), RequestBody.create(
                            null, PortraitImgFile))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(requestBody).build();
            Callback callback = new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "加载失败");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    String res = response.body().string();
                    Logger.i("身份证信息", res);
                    ocridCared3 = new Gson().fromJson(res, OcridCared.class);
                    name = ocridCared3.getName();
                    idcard_number = ocridCared3.getId_card_number();
                    verify();

                }
            };

            HttpUtils.enqueue(request, callback);

        }
    }

    private void verify() {
        if (ComParameter.useStr(PortraitImg) && ComParameter.useStr(RealFilePath)) {
            File PortraitImgFile = new File(PortraitImg);
            final File RealFilePathFile = new File(RealFilePath);
            if (PortraitImgFile == null || RealFilePathFile == null) {
                Logger.e("kkkkkkkkkkkkkkkkkkkkkkkkkkk", "文件为空");
                return;
            }
            String url = ComParameter.FACEID_URL + ComParameter.FACE_VERIFYL;
            //构造上传请求，类似web表单
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("api_key", getResources().getString(R.string.api_key))
                    .addFormDataPart("api_secret", getResources().getString(R.string.api_secret))
                    .addFormDataPart("comparison_type", "0")//无源比对
                    .addFormDataPart("face_image_type", "meglive")
                    .addFormDataPart("uuid", ComParameter.getUUID())
//                    .addFormDataPart("idcard_number", idcard_number)
                    .addFormDataPart("image_ref1", PortraitImgFile.getName(), RequestBody.create(
                            null, PortraitImgFile))
                    .addFormDataPart("delta", delta)
                    .addFormDataPart("image_best", RealFilePathFile.getName(), RequestBody.create(
                            null, RealFilePathFile))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";" +
//                            "filename=\"another.dex\""), RequestBody.create
//                            (MediaType.parse("application/octet-stream"), file))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(requestBody).build();
            Callback callback = new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "加载失败");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    try {

                        String res = response.body().string();
                        Logger.i("身份证人脸对比", res);
                        Logger.i("身份证人脸对比2", RealFilePath + "   111    " + PortraitImg + "   0000");
                        Intent intent = new Intent(getContext(), TemporaryActivity.class);
                        intent.putExtra("best_mImg", idcardImg);
                        intent.putExtra("type", "活体检测+照片比对");
                        intent.putExtra("json", res);
                        intent.putExtra("name", name);
                        intent.putExtra("best_img", RealFilePath);
                        intent.putExtra("idcard_number", idcard_number);
                        startActivity(intent);
                        guestDialog.dismiss();
                    } catch (Exception e) {
                        Logger.e("888888888888888888", e.toString());
                    }


                }
            };

            HttpUtils.enqueue(request, callback);

        }

    }

    private void getIDInfo() {
        if (ComParameter.useStr(idcardImg)) {
            File PortraitImgFile = new File(idcardImg);
            if (PortraitImgFile == null) {
                Logger.e("kkkkkkkkkkkkkkkkkkkkkkkkkkk", "文件为空");
                return;
            }
            String url = ComParameter.FACEID_URL + ComParameter.OCRIDCARD;
            //构造上传请求，类似web表单
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("api_key", getResources().getString(R.string.api_key))
                    .addFormDataPart("api_secret", getResources().getString(R.string.api_secret))
                    .addFormDataPart("image", PortraitImgFile.getName(), RequestBody.create(
                            null, PortraitImgFile))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(requestBody).build();
            Callback callback = new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "加载失败");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    String res = response.body().string();
                    Logger.i("身份证信息", res);
                    OcridCared ocridCared = new Gson().fromJson(res, OcridCared.class);
                    verify2(ocridCared);

                }
            };

            HttpUtils.enqueue(request, callback);

        }

    }

    private void verify2(final OcridCared ocridCared) {
//        if (ocridCared.getId_card_number().length())
        if (ComParameter.useStr(RealFilePath)) {
            File RealFilePathFile = new File(RealFilePath);
            if (RealFilePathFile == null) {
                Logger.e("kkkkkkkkkkkkkkkkkkkkkkkkkkk", "文件为空");
                return;
            }
            String url = ComParameter.FACEID_URL + ComParameter.FACE_VERIFYL;
            //构造上传请求，类似web表单
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("api_key", getResources().getString(R.string.api_key))
                    .addFormDataPart("api_secret", getResources().getString(R.string.api_secret))
                    .addFormDataPart("comparison_type", "1")//有源比对
                    .addFormDataPart("face_image_type", "meglive")
                    .addFormDataPart("idcard_name", ocridCared.getName())
                    .addFormDataPart("idcard_number", ocridCared.getId_card_number())
                    .addFormDataPart("delta", delta)
                    .addFormDataPart("image_best", RealFilePathFile.getName(), RequestBody.create(
                            null, RealFilePathFile))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";" +
//                            "filename=\"another.dex\""), RequestBody.create
//                            (MediaType.parse("application/octet-stream"), file))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(requestBody).build();
            Callback callback = new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "加载失败");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    try {
                        String res = response.body().string();
                        ErrorResponse errorResponse = new Gson().fromJson(res, ErrorResponse.class);
//                        JSONObject jsonObject = new JSONObject(res);
                        if (ComParameter.useStr(errorResponse.getError_message())) {
                            ToastUtils.makeToast("出现异常，" + errorResponse.getError_message() + "请重新进行");
                            return;
                        }
                        Logger.i("身份证人脸对比", res);
                        Logger.i("身份证人脸对比2", RealFilePath + "   111    " + PortraitImg + "   0000");
                        Intent intent = new Intent(getContext(), TemporaryActivity.class);
                        intent.putExtra("name", ocridCared.getName());
                        intent.putExtra("idcard_number", ocridCared.getId_card_number());
                        intent.putExtra("best_img", RealFilePath);
                        intent.putExtra("json", res);
                        startActivity(intent);
                        guestDialog.dismiss();
                    } catch (Exception e) {
                        Logger.e("888888888888888888", e.toString());
                    }
                }
            };
            HttpUtils.enqueue(request, callback);
        }
    }

}
