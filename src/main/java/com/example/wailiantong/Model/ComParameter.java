package com.example.wailiantong.Model;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;

public class ComParameter {
    public static String FILE_PATH = "/gongzhengtong/liveness/";
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType mJPEG = MediaType.parse("image/jpeg");
    String VERSION = "1.0";
    //    0:汉语  1:英语
    public static int LANGUAGE = 0;

    public static Boolean ISLOGINYES = true;
    public static Boolean ISLOGINNO = false;

    /**
     * 游客接口
     */
    public static Boolean GUEST = false;

    public static String URL = "http://112.74.169.37:8081";
    public static String FACEID_URL = "https://api.megvii.com/faceid";
    /**
     * 登陆接口
     */
    public static String ACTIVE = "/usr/register ";
    public static String LOGIN = "/usr/login";
    public static String RESETPWD="/accounts/forget_pwd/";
    public static String GETCODE="/accounts/send_ver/";
    /**
     * 上传图片
     */
    public static String UPLOAD = "/resources/upload_f";
    /**
     * 创建记录
     */
    public static String CREATE = "/order/new";
    public static String GETALLINFO="/notifications/get_notifications/";
    public static String COMMITORDER="/rcv/new";
    public static String NEWMSG="/msg/new";
    public static String VIEWMSG="/msg/view";

    /**
     * 获取列表
     */
    public static String GETMAINLIST="/order/main";
    public static String GETDETAIL = "/order/detail";
    public static String GETMYLIST="/order/mySend";
    public static String GETRECVER="/rcv/view";


    /**
     * 搜索
     */
    public static String SEARCHBYNAME="/order/search";
    public static String GETLISTBYTYPE="/service/get_service_by_type/";//根据类型获取列表
    public static String GETLISTBYDATE="/service/get_service_by_date/";//根据日期获取列表

    /**
     * 用户信息更新
     */
    public static String UPDATE = "/omtcp/users/update";
    /**
     * 获取业务员的信息
     */
    public static String GETINFO = "/accounts/get_own_info";
    public static String GETLIST="/service/list";
    public static String GETLISTDETAIL="/service/service_detail/";

    public static String GETMESSAGE="/notifications/get_notifications/";

    public static String CHANGEINFO = "/accounts/saleman_modify/";//修改个人信息接口

    public static String FACE_VERIFYL = "/v2/verify";

    public static String OCRIDCARD = "/v1/ocridcard";












    //byte[]转为文件
    public String getFileFromBytes(byte[] b)
    {
        String ffffff;
        ffffff = ComParameter.FILE_PATH + System.currentTimeMillis() + ".jpg";
        BufferedOutputStream stream = null;
        File file = null;
        try
        {
            // 判断SDcard状态
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            {
                // 错误提示
                return null;
            }

            // 检查SDcard空间
            File SDCardRoot = Environment.getExternalStorageDirectory();
            if (SDCardRoot.getFreeSpace() < 10000)
            {
                // 弹出对话框提示用户空间不够
                Log.e("Utils", "存储空间不够");
                return null;
            }

            // 在SDcard创建文件夹及文件
            File bitmapFile = new File(SDCardRoot.getPath() + ffffff);
            bitmapFile.getParentFile().mkdirs();// 创建文件夹

            FileOutputStream fstream = new FileOutputStream(bitmapFile);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        return Environment.getExternalStorageDirectory().getPath()+ffffff;
    }

    /**
     * 字符串是否可用
     * @return
     */
    public static Boolean useStr(String s) {
        if (s != null && s.length() != 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 获得UUID
     * @return
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s;
    }



}
